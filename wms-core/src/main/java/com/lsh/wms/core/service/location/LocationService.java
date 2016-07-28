package com.lsh.wms.core.service.location;

import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.core.dao.baseinfo.BaseinfoLocationDao;
import com.lsh.wms.core.dao.baseinfo.BaseinfoLocationShelfDao;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.baseinfo.BaseinfoLocationShelf;
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
    @Autowired
    private BaseinfoLocationShelfDao shelfDao;
    @Autowired
    private BaseinfoLocationService baseinfoLocationService;


    // location类型定义
    public static final Map<String, Long> LOCATION_TYPE = new HashMap<String, Long>() {
        {
            put("warehouse", new Long(1)); // 1仓库
            put("area", new Long(2)); // 2区域
            put("inventoryLost", new Long(3));    //3盘盈盘亏
            put("goods_area", new Long(4)); //4货区(下级是 货架或者阁楼)
            put("floor", new Long(5)); // 5 地堆区
            put("temporary", new Long(6)); // 6 暂存区
            put("collection_area", new Long(7)); // 7 集货区
            put("back_area", new Long(8)); // 8 退货区
            put("defective_area", new Long(9)); // 9 残次区
            put("dock_area", new Long(10)); // 10 码头区
            put("bin", new Long(11)); // 11 货位
            put("packing_bin", new Long(12)); // 12 拣货位
            put("stock_bin", new Long(13)); // 13 存货位
            put("floor_bin", new Long(14)); // 14 地堆货位
            put("temporary_bin", new Long(15)); // 15 暂存货位
            put("collection_bin", new Long(16)); // 16 集货货位
            put("back_bin", new Long(17)); // 17 退货货位
            put("defective_bin", new Long(18));// 18 残次货位
            put("passage", new Long(19));   //19通道
        }
    };

    //计数
    public int countLocation(Map<String, Object> params) {
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

//        LocationModelFactory locationFactory = new LocationModelFactory(new BaseinfoLocationService());
//        locationFactory.insert(location);
        if (location.getLocationId() == 0) {
            //添加locationId
            int iLocationId = 0;
            location.setLocationId((long) iLocationId);
        }
        //添加新增时间
        long createdAt = DateUtils.getCurrentSeconds();
        location.setCreatedAt(createdAt);
        locationDao.insert(location);
//        locationFactory.insert(location);
        return location;
    }

    @Transactional(readOnly = false)
    public BaseinfoLocation updateLocation(BaseinfoLocation location) {
        LocationFactory locationFactory = new LocationFactory(new BaseinfoLocationService());
        locationFactory.insert(location);
        if (this.getLocation(location.getLocationId()) == null) {
            return null;
        }
        long updatedAt = DateUtils.getCurrentSeconds();
        location.setUpdatedAt(updatedAt);
        locationDao.update(location);
        return location;
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

    // 获取一个location下所有是存储位的子节点id
    public List<Long> getStoreLocationIds(Long locationId) {
        List<BaseinfoLocation> locations = this.getStoreLocations(locationId);
        return this.getLocationIds(locations);
    }

    // 获取父级节点
    public BaseinfoLocation getFatherLocation(Long locationId) {
        BaseinfoLocation curLocation = this.getLocation(locationId);
        Long fatherId = curLocation.getFatherId();
        if (fatherId == 0) {
            return null;
        }
        return this.getLocation(fatherId);
    }

    // 根据类型获取父级location节点
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

    // 获取父级区域location节点id
    public Long getFatherIdByType(Long locationId, String type) {
        BaseinfoLocation fatherLocation = this.getFatherByType(locationId, type);
        return fatherLocation.getLocationId();
    }

    // 获取父级区域节点
    public BaseinfoLocation getAreaFather(Long locationId) {
        BaseinfoLocation areaFather = this.getFatherByType(locationId, "areas");
        return areaFather;
    }

    // 获取父级区域节点id
    public Long getAreaFatherId(Long locationId) {
        BaseinfoLocation areaFatherId = this.getAreaFather(locationId);
        return areaFatherId.getLocationId();
    }

    // 按类型获取location节点
    public List<BaseinfoLocation> getLocationsByType(String type) {
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
        List<BaseinfoLocation> locations = this.getLocationsByType("collection_area");
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
    //获取货位bin节点的type

    // TODO    public BaseinfoLocation getAvailableBinLocationByType(String type)
    //获取货位节点的id
    public List<BaseinfoLocation> getBaseinfoLocationList(Map<String, Object> mapQuery) {
        return locationDao.getBaseinfoLocationList(mapQuery);
    }

    // 获取拣货位最近的存储位
    public BaseinfoLocation getNearestStorageByPicking(BaseinfoLocation pickingLocation) {
        return null;
    }


    /*
        接下来的内容是对各个子信心进行的封装
     */

    //新增,前提是有了基础location
    public void insertImpLocation(String type, Object impLocation) {
        int LOCATION_TYPE = this.LOCATION_TYPE.get(type).intValue();
        /*
        put("warehouse", new Long(1)); // 仓库
        put("area", new Long(2)); // 区域
        put("inventoryLost", new Long(3)); // 盘亏盘盈
        put("temporary", new Long(4)); // 暂存区
        put("floor", new Long(5)); // 地堆
        put("picking", new Long(6)); // 拣货位
        */

        switch (LOCATION_TYPE) {
            case 1:
                BaseinfoLocationShelf x = (BaseinfoLocationShelf) impLocation;
                shelfDao.insert(x);
                break;
        }
    }

    public void insertPureImpLocation(BaseinfoLocation baseLocation, Object impLocation) {
        DateUtils.getCurrentSeconds();
        this.insertLocation(baseLocation);
        this.insertImpLocation(baseLocation.getTypeName(), impLocation);
    }

    public void getImpLocation(long iType, long locationId) {
        Map<String, Object> params = new HashMap<String, Object>();
        BaseinfoLocation location;
        params.put("locationId", locationId);
        List<BaseinfoLocation> locations = locationDao.getBaseinfoLocationList(params);
        //return locations.size() == 1 ? locations.get(0) : null;
    }

    //获取现在inUse是否可用
    //0没占用,1占用
    public boolean isUsed(Long locationId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("locationId", locationId);
        List<BaseinfoLocation> list = this.locationDao.getBaseinfoLocationList(params);
        //查询的id肯定有
        BaseinfoLocation location = list.get(0);
        Integer in_use = location.getInUse();
        if (0 == in_use) {
            return false;
        } else {
            return true;
        }

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
     * 位置是否已占用
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
}
