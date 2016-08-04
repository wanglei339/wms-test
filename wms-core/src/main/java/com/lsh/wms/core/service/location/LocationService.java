package com.lsh.wms.core.service.location;

import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.core.dao.baseinfo.BaseinfoLocationDao;
import com.lsh.wms.core.dao.baseinfo.BaseinfoLocationShelfDao;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.baseinfo.BaseinfoLocationShelf;
import com.lsh.wms.model.baseinfo.IBaseinfoLocaltionModel;
import com.lsh.wms.model.stock.StockQuant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by fengkun on 16/7/11.
 */

@Service
@Transactional(readOnly = true)
public class LocationService {
    private static final Logger logger = LoggerFactory.getLogger(LocationService.class);
    @Autowired
    private BaseinfoLocationDao locationDao;
    @Autowired
    private StockQuantService stockQuantService;


    // location类型定义
    public static final Map<String, Long> LOCATION_TYPE = new HashMap<String, Long>() {
        {

            put("warehouse", new Long(1)); // 1仓库
            put("area", new Long(2)); // 2区域
            put("passage", new Long(3));   //3通道
            //各区域
            put("inventoryLost", new Long(4));    //4盘盈盘亏
            put("shelf_area", new Long(5)); //5货架区
            put("loft_area", new Long(6)); //6阁楼区
            put("floor", new Long(7)); // 7 地堆区
            put("temporary", new Long(8)); // 8 暂存区
            put("collection_area", new Long(9)); // 9 集货区
            put("back_area", new Long(10)); // 10 退货区
            put("defective_area", new Long(11)); // 11残次区
            put("dock_area", new Long(12)); // 12 码头区

            //货架和阁楼隶属货架区,阁楼区
            put("shelf", new Long(13));  //13货架(个体)
            put("loft", new Long(14));   //14阁楼(个体)

            //所有的货位
            put("bin", new Long(15)); // 15 所有的货位(存有货架|阁楼|区域的id)
            //所有的货位
            put("shelf_collection_bin", new Long(16)); //16货架拣货位
            put("shelf_store_bin", new Long(17));   //19 货架存货位货位
            put("loft_collection_bin", new Long(18)); //17阁楼拣货位
            put("loft_store_bin", new Long(19));   //18阁楼存货位

            /*其他功能区的货位*/
            put("floor_bin", new Long(20)); // 20 地堆货位
            put("temporary_bin", new Long(21)); // 21 暂存货位
            put("collection_bin", new Long(22)); // 22 集货货位
            put("back_bin", new Long(23)); // 23 退货货位
            put("defective_bin", new Long(24));// 24 残次货位
        }
    };

    //计数
    //valid一定是1 未删除的
    public int countLocation(Map<String, Object> params) {
        params.put("isValid", 1);
        return locationDao.countBaseinfoLocation(params);
    }

    // 获取location
    public BaseinfoLocation getLocation(long locationId) {
        Map<String, Object> params = new HashMap<String, Object>();
        BaseinfoLocation location;
        params.put("locationId", locationId);
        params.put("isValid", 1);
        List<BaseinfoLocation> locations = locationDao.getBaseinfoLocationList(params);
        if (locations.size() == 1) {
            location = locations.get(0);
        } else {
            return null;
        }
        return location;
    }

    /**
     * 插入location方法,TODO 需要插入商品的四维坐标
     *
     * @param location
     * @return
     */
    @Transactional(readOnly = false)
    public BaseinfoLocation insertLocation(BaseinfoLocation location) {

        if (location.getLocationId() == 0) {
            //添加locationId
            int iLocationId = 0;
            location.setLocationId((long) iLocationId);
        }
        //添加新增时间
        long createdAt = DateUtils.getCurrentSeconds();
        location.setCreatedAt(createdAt);
        locationDao.insert(location);
        return location;
    }

    @Transactional(readOnly = false)
    public BaseinfoLocation updateLocation(IBaseinfoLocaltionModel iBaseinfoLocaltionModel) {
        BaseinfoLocation baseinfoLocation = (BaseinfoLocation) iBaseinfoLocaltionModel;
        if (this.getLocation(baseinfoLocation.getLocationId()) == null) {
            return null;
        }
        long updatedAt = DateUtils.getCurrentSeconds();
        baseinfoLocation.setUpdatedAt(updatedAt);
        locationDao.update(baseinfoLocation);
        return baseinfoLocation;
    }

    @Transactional(readOnly = false)
    public BaseinfoLocation insertLocation(IBaseinfoLocaltionModel iBaseinfoLocaltionModel) {
        BaseinfoLocation baseinfoLocation = (BaseinfoLocation) iBaseinfoLocaltionModel;
        if (this.getLocation(baseinfoLocation.getLocationId()) == null) {
            return null;
        }
        long updatedAt = DateUtils.getCurrentSeconds();
        baseinfoLocation.setUpdatedAt(updatedAt);
        locationDao.update(baseinfoLocation);
        return baseinfoLocation;
    }

    // 获取节点location_id
    public List<Long> getLocationIds(List<BaseinfoLocation> locations) {
        List<Long> locationIds = new ArrayList<Long>();
        for (BaseinfoLocation location : locations) {
            locationIds.add(location.getLocationId());
        }
        return locationIds;
    }

    // 获取一个location下一层的子节点
    public List<BaseinfoLocation> getChildrenLocations(Long locationId) {
        Map<String, Object> params = new HashMap<String, Object>();
        Map<Long, BaseinfoLocation> childrenLocations = new HashMap<Long, BaseinfoLocation>();
        // 判断是否已为子节点
        BaseinfoLocation curLocation = this.getLocation(locationId);
        if (curLocation.getIsLeaf() == 1) {
            return null;
        }
        params.put("fatherId", locationId);
        params.put("isValid", 1);
        List<BaseinfoLocation> locations = locationDao.getBaseinfoLocationList(params);
        return locations;
    }

    // 获取一个location下一层的子节点id
    public List<Long> getChildrenLocationIds(Long locationId) {
        List<BaseinfoLocation> locations = this.getChildrenLocations(locationId);
        return this.getLocationIds(locations);
    }

    // 获取一个location下所有是存储位的子节点
    public List<BaseinfoLocation> getStoreLocations(Long locationId) {
        List<BaseinfoLocation> locations = new ArrayList();
        BaseinfoLocation curLocation = this.getLocation(locationId);
        if (curLocation == null) {
            return null;
        }
        if (curLocation.getCanStore() == 1) {
            locations.add(curLocation);
        }
        if (curLocation.getIsLeaf() == 0) {
            List<BaseinfoLocation> childrenLocations = this.getChildrenLocations(locationId);
            // 深度优先,递归遍历
            for (BaseinfoLocation location : childrenLocations) {
                List<BaseinfoLocation> childrenStoreLocations = this.getStoreLocations(location.getLocationId());
                locations.addAll(childrenStoreLocations);
            }
        }
        return locations;
    }

    /**
     * 根据节点locationid获取该节点下所有可储存位置
     *
     * @param locationId
     * @return
     */
    public List<Long> getStoreLocationIds(Long locationId) {
        List<BaseinfoLocation> locations = this.getStoreLocations(locationId);
        return this.getLocationIds(locations);
    }

    /**
     * 查找父级节点
     *
     * @param locationId
     * @return 位置
     */
    public BaseinfoLocation getFatherLocation(Long locationId) {
        BaseinfoLocation curLocation = this.getLocation(locationId);
        Long fatherId = curLocation.getFatherId();
        if (fatherId.equals(0)) {
            return null;
        }
        return this.getLocation(fatherId);
    }

    /**
     * 获取父亲的type
     *
     * @param locationId
     * @return
     */
    public Long getFatherLocationType(Long locationId) {
        BaseinfoLocation curLocation = this.getLocation(locationId);
        Long fatherId = curLocation.getFatherId();
        if (fatherId == 0) {
            return null;
        }
        return this.getLocation(fatherId).getType();
    }

    /**
     * 获取父亲的编码
     *
     * @param locationId
     * @return
     */
    public String getFatherLocationCode(Long locationId) {
        BaseinfoLocation curLocation = this.getLocation(locationId);
        Long fatherId = curLocation.getFatherId();
        if (fatherId == 0) {
            return null;
        }
        return this.getLocation(fatherId).getLocationCode();
    }


    /**
     * 根据所在位置的locationId
     * 获取指定type祖先级(包含上一级)的location节点
     *
     * @param locationId 所在位置id
     * @param type       位置类型
     * @return
     */
    public BaseinfoLocation getFatherByType(Long locationId, String type) {
        BaseinfoLocation curLocation = this.getLocation(locationId);
        Long fatherId = curLocation.getFatherId();
        if (curLocation.getType().equals(this.LOCATION_TYPE.get(type))) {
            return curLocation;
        }
        if (fatherId == 0) {
            return null;
        }
        return this.getFatherByType(fatherId, type);
    }

    /**
     * 找全路径
     * @param locationId
     * @return
     */
    public List<BaseinfoLocation> getFatherList(Long locationId) {
        List<BaseinfoLocation> baseinfoLocationList = new ArrayList<BaseinfoLocation>();
        BaseinfoLocation curLocation = this.getLocation(locationId);
        Long fatherId = curLocation.getFatherId();
        if (curLocation.getType().equals(this.LOCATION_TYPE.get(LocationConstant.Warehouse))) {
            baseinfoLocationList.add(curLocation);
            return baseinfoLocationList;
        }
        if (fatherId == 0) {
            return null;
        }
        return this.getFatherList(fatherId);
    }



    /**
     * 获取祖先级别的区域location节点id
     * 根据指定的祖先级别的type
     *
     * @param locationId
     * @param type
     * @return
     */
    public Long getFatherIdByType(Long locationId, String type) {
        BaseinfoLocation fatherLocation = this.getFatherByType(locationId, type);
        return fatherLocation.getLocationId();
    }

    /**
     * 获取父级区域所有大区的节点
     *
     * @param locationId
     * @return
     */
    public BaseinfoLocation getAreaFather(Long locationId) {
        BaseinfoLocation areaFather = this.getFatherByType(locationId, "area");
        return areaFather;
    }

    // 获取父级区域节点id
    public Long getAreaFatherId(Long locationId) {
        BaseinfoLocation areaFatherId = this.getAreaFather(locationId);
        return areaFatherId.getLocationId();
    }

    // 按类型获取location节点
    public List<BaseinfoLocation> getLocationsByType(String type) {
        if (type == null || type.equals("")) {
            return null;
        }
        Map<String, Object> params = new HashMap<String, Object>();
        Long LOCATION_TYPE = this.LOCATION_TYPE.get(type);
        params.put("type", LOCATION_TYPE);
        params.put("isValid", 1);
        List<BaseinfoLocation> locations = locationDao.getBaseinfoLocationList(params);
        return locations;
    }

    // 获取可用仓库根节点
    public BaseinfoLocation getWarehouseLocation() {
        List<BaseinfoLocation> locations = this.getLocationsByType("warehouse");
        if (locations.size() > 0) {
            return locations.get(0);
        } else {
            return null;
        }
    }

    // 获取可用仓库根节点id
    public Long getWarehouseLocationId() {
        BaseinfoLocation location = this.getWarehouseLocation();
        return location.getLocationId();
    }

    // 获取可用盘亏盘盈节点
    public BaseinfoLocation getInventoryLostLocation() {
        List<BaseinfoLocation> locations = this.getLocationsByType("inventoryLost");
        if (locations.size() > 0) {
            return locations.get(0);
        } else {
            return null;
        }
    }

    // 获取可用盘亏盘盈节点id
    public Long getInventoryLostLocationId() {
        BaseinfoLocation location = this.getInventoryLostLocation();
        return location.getLocationId();
    }

    //获取可用残次区的节点
    public BaseinfoLocation getDefectiveLocation() {
        List<BaseinfoLocation> locations = this.getLocationsByType("defective_area");
        if (locations.size() > 0) {
            return locations.get(0);
        } else {
            return null;
        }
    }

    // 获取可用残次区节点id
    public Long getDefectiveLocationId() {
        BaseinfoLocation location = this.getDefectiveLocation();
        return location.getLocationId();
    }

    //获取可用退货区节点
    public BaseinfoLocation getBackLocation() {
        List<BaseinfoLocation> locations = this.getLocationsByType("back_area");
        if (locations.size() > 0) {
            return locations.get(0);
        } else {
            return null;
        }
    }

    //获取可用退货区节点id
    public Long getBackLocationId() {
        BaseinfoLocation location = this.getBackLocation();
        return location.getLocationId();
    }

    // 分配可用暂存区location
    public BaseinfoLocation getAvailableLocationByType(String type) {
        List<BaseinfoLocation> locations = this.getLocationsByType(type);
        if (locations.size() > 0) {
            for (BaseinfoLocation location : locations) {
                Long locationId = location.getLocationId();
                List<Long> containerIds = stockQuantService.getContainerIdByLocationId(locationId);
                if (location.getContainerVol() - containerIds.size() > 0) {
                    return location;
                }
            }
        }
        return null;
    }

    // 获取可用暂存区节点id
    public Long getAvailableLocationId(String type) {
        BaseinfoLocation location = this.getAvailableLocationByType(type);
        return location.getLocationId();
    }


    //分配可用集货区节点
    public BaseinfoLocation getCollectionLocation() {
        return this.getAvailableLocationByType("collection_area");
    }

    //获取可用的集货节点id
    public Long getCollectionLocationId() {
        BaseinfoLocation location = this.getCollectionLocation();
        return location.getLocationId();
    }

    //分配码头dock
    // TODO 分配节点以后在调整怎么分配
    public BaseinfoLocation getDockLocation() {
        List<BaseinfoLocation> locations = this.getLocationsByType("dock_area");
        if (locations.size() > 0) {
            return locations.get(0);
        } else {
            return null;
        }
    }

    //获取码头节点id
    public Long getDockLocationId() {
        BaseinfoLocation location = this.getDockLocation();
        return location.getLocationId();
    }

    //获取货位节点的id
    public List<BaseinfoLocation> getBaseinfoLocationList(Map<String, Object> mapQuery) {
        return locationDao.getBaseinfoLocationList(mapQuery);
    }

    // 获取拣货位最近的存储位
    public BaseinfoLocation getNearestStorageByPicking(BaseinfoLocation pickingLocation) {
        return null;
    }


    public void getImpLocation(long iType, long locationId) {
        Map<String, Object> params = new HashMap<String, Object>();
        BaseinfoLocation location;
        params.put("locationId", locationId);
        List<BaseinfoLocation> locations = locationDao.getBaseinfoLocationList(params);
        //return locations.size() == 1 ? locations.get(0) : null;
    }

    //获取code
    public String getCodeById(Long locationId) {
        String code = null;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("locationId", locationId);
        List<BaseinfoLocation> baseinfoLocationList = locationDao.getBaseinfoLocationList(params);
        if (baseinfoLocationList.size() > 0) {
            code = baseinfoLocationList.get(0).getLocationCode();
        }
        return code;
    }

    /**
     * 根据type,isvalid和或者code获取location的集合,主要和查询有关
     *
     * @param mapQuery 前端传过来的map参数
     * @return
     */
    public BaseinfoLocation getLocationListByType(Map<String, Object> mapQuery) {
        mapQuery.put("isValid", 1);
        List<BaseinfoLocation> list = locationDao.getBaseinfoLocationList(mapQuery);
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 位置是否已占用
     *
     * @param locationId
     * @return
     */
    public Boolean isLocationInUse(Long locationId) {
        List<StockQuant> quants = stockQuantService.getQuantsByLocationId(locationId);
        if (quants.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 获取级别的type,包含货架区5、阁楼区6、地堆区7、残存区8、集货区9、退货区10、残次区11
     */
    private static final long[] REGIONTYPE = {5, 6, 7, 8, 9, 10, 11};

    private static boolean flag = true;
    private static String RegionName = "";

    /**
     * 根据现有的位置,获取区域的位置,一直找到区的一层
     * TODO 返回父亲的type类型,然后,根据类型在外面拼写
     *
     * @param baseinfoLocation
     * @return
     */
    //获取区域的name,拼接字符串
    public String getRegionName(BaseinfoLocation baseinfoLocation) {
        if (flag == false) {
            return RegionName;
        }
        //先排序
        Arrays.sort(REGIONTYPE);
        //获取父亲对象
        BaseinfoLocation fatherLocation = this.getFatherLocation(baseinfoLocation.getLocationId());
        //没有父亲
        if (null == fatherLocation) {
            return RegionName;
        }
        Long fatherLocationType = fatherLocation.getType();
        //找到区域的一层不找了
        if (fatherLocationType < 5L) {
            return RegionName;
        }
        //向上查找直到type属于货架区、阁楼区、暂存区、地堆区、退货区
        if (!(Arrays.binarySearch(REGIONTYPE, fatherLocationType) < 0)) {
            String regionCode = fatherLocation.getLocationCode();
            String regionTypeName = fatherLocation.getTypeName();
            RegionName = regionTypeName + regionCode + "区";
            flag = false;
            return RegionName;
        } else {
            this.getRegionName(fatherLocation);
        }
        return RegionName;
    }

    /**
     * 全局变量,开关使用完置为原来的值
     *
     * @param flag
     */
    public static void setFlag(boolean flag) {
        LocationService.flag = flag;
    }

    /**
     * 获取location子代集合的开关
     */
    public static boolean LocationFlag = true;

    public static void setLocationFlag(boolean locationFlag) {
        LocationFlag = locationFlag;
    }

//    public static List<BaseinfoLocation> targetList = new ArrayList<BaseinfoLocation>();

    /**
     * 根据当前的locationId获取,指定type的子集
     * 如果fatherId不是locationId,那就是祖先的id
     */
    public List<BaseinfoLocation> getSubLocationList(Long locationId, Long type) {
        List<BaseinfoLocation> targetList = new ArrayList<BaseinfoLocation>();
        //遍历整棵树
        List<BaseinfoLocation> subList = this.getStoreLocations(locationId);
        //然后然后遍历这颗子树,找出指定的type的list
        for (BaseinfoLocation baseinfoLocation : subList) {
            if (baseinfoLocation.getType() == type) {
                targetList.add(baseinfoLocation);
            }
        }
        return targetList;
    }

}
