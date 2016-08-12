package com.lsh.wms.core.service.location;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.core.constant.LocationConstant;
import com.lsh.wms.core.dao.baseinfo.BaseinfoLocationDao;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
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


    //计数
    //valid一定是1 未删除的
    public int countLocation(Map<String, Object> params) {
        params.put("isValid", 1);
        return locationDao.countBaseinfoLocation(params);
    }

    // 获取location
    public BaseinfoLocation getLocation(Long locationId) {
        Map<String, Object> params = new HashMap<String, Object>();
        BaseinfoLocation location = new BaseinfoLocation();
        params.put("locationId", locationId);
        params.put("isValid", 1);
        List<BaseinfoLocation> locations = locationDao.getBaseinfoLocationList(params);
        return locations.size() > 0 ? locations.get(0) : null;
    }

    /**
     * 插入location方法
     *
     * @param location
     * @return
     */
    @Transactional(readOnly = false)
    public BaseinfoLocation insertLocation(BaseinfoLocation location) {
        location = this.setLocationIdAndRange(location);
        //添加新增时间
        long createdAt = DateUtils.getCurrentSeconds();
        location.setCreatedAt(createdAt);
        location.setUpdatedAt(createdAt);
        locationDao.insert(location);
        return location;
    }

    /**
     * 更新location
     *
     * @param iBaseinfoLocaltionModel
     * @return
     */
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

    /**
     * 删除节点和下面的所有子树
     * @param locationId
     * @return
     */
    @Transactional(readOnly = false)
    public BaseinfoLocation removeLocationAndChildren(Long locationId) throws BizCheckedException{
        BaseinfoLocation location = this.getLocation(locationId);
        //找不到
        if(null == location){
            throw new BizCheckedException("2180003");
        }
        //将location的子树查出来isvalid置为0
        List<BaseinfoLocation> childrenList = this.getChildrenLocations(locationId);
        for (BaseinfoLocation child:childrenList){
            child.setIsValid(0);
            updateLocation(child);
        }
        //删除该节点
        location.setIsValid(0);
        updateLocation(location);
        return location;
    }

    /**
     * 重置location_id和range
     *
     * @param location
     * @return
     */
    @Transactional(readOnly = false)
    public BaseinfoLocation resetLocation(BaseinfoLocation location) {
        location = this.setLocationIdAndRange(location);
        this.updateLocation(location);
        return location;
    }

    /**
     * 设置location节点子节点的范围
     * 重要方法,设置location_id必须使用此方法!!!
     *
     * @param location
     * @return
     */
    public BaseinfoLocation setLocationIdAndRange(BaseinfoLocation location) {
        Long fatherLocationId = location.getFatherId();
        // 根节点处理
        if (fatherLocationId == null || fatherLocationId.equals(-1L)) {
            location.setLocationId(0L);
            location.setLeftRange(1L);
            location.setLevel(0L); // 设置层数,根节点层数为0,下一层为起始层,层数为1
            Long rightRange = 0L;
            for (Integer i = 1; i <= LocationConstant.LOCATION_LEVEL; i++) {
                rightRange += Math.round(Math.pow(LocationConstant.CHILDREN_RANGE, i));
            }
            location.setRightRange(rightRange);
        } else {
            // 非根节点
            BaseinfoLocation fatherLocation = this.getLocation(fatherLocationId);
            Long level = fatherLocation.getLevel() + 1;
            Long fatherLeftRange = fatherLocation.getLeftRange();
            Long fatherRightRange = fatherLocation.getRightRange();
            List<Long> levelLocationIds = new ArrayList<Long>();
            Long tmpLocationId = fatherLeftRange;
            levelLocationIds.add(tmpLocationId);
            // 超出最大层数
            if (level > LocationConstant.LOCATION_LEVEL) {
                throw new BizCheckedException("2600002");
            }
            // 找出当前层所有可能的location_id
            for (Long i = fatherLeftRange; i < (fatherLeftRange + LocationConstant.CHILDREN_RANGE); i++) {
                tmpLocationId += (fatherRightRange - fatherLeftRange + 1) / LocationConstant.CHILDREN_RANGE;
                levelLocationIds.add(tmpLocationId);
            }
            for (Long levelLocationId : levelLocationIds) {
                BaseinfoLocation tmpLocation = this.getLocation(levelLocationId);
                // 如果已分配过但是逻辑删除了,则复用该id
                if (tmpLocation == null || tmpLocation.getIsValid() == 0) { //一旦集合比较大,选择没有分配过的location即null,进入方法设置locationId和左右范围
                    location.setLocationId(levelLocationId);
                    location.setLeftRange(levelLocationId + 1);
                    location.setLevel(level);
                    Long rightRange = levelLocationId;
                    for (Integer i = 1; i <= LocationConstant.LOCATION_LEVEL - level; i++) {
                        rightRange += Math.round(Math.pow(LocationConstant.CHILDREN_RANGE, i));
                    }
                    location.setRightRange(rightRange);
                    break;
                }
            }
            // id已分配至上限,不可继续分配
            if (location.getLocationId().equals(0) || location.getLocationId() == null) {
                throw new BizCheckedException("2600001");
            }
        }
        return location;
    }

    /**
     * 获取节点location_id
     *
     * @param locations
     * @return
     */
    public List<Long> getLocationIds(List<BaseinfoLocation> locations) {
        List<Long> locationIds = new ArrayList<Long>();
        for (BaseinfoLocation location : locations) {
            locationIds.add(location.getLocationId());
        }
        return locationIds;
    }

    /**
     * 获取一个location所有子节点
     *
     * @param locationId
     * @return
     */
    public List<BaseinfoLocation> getChildrenLocations(Long locationId) {
        Map<String, Object> params = new HashMap<String, Object>();
        BaseinfoLocation location = this.getLocation(locationId);
        params.put("leftRange", location.getLeftRange());
        params.put("rightRange", location.getRightRange());
        params.put("isValid", 1);
        return locationDao.getChildrenLocationList(params);
    }

    /**
     * 根据type获取子节点
     *
     * @param locationId
     * @param type
     * @return
     */
    public List<BaseinfoLocation> getChildrenLocationsByType(Long locationId, String type) {
        Map<String, Object> params = new HashMap<String, Object>();
        BaseinfoLocation location = this.getLocation(locationId);
        params.put("leftRange", location.getLeftRange());
        params.put("rightRange", location.getRightRange());
        params.put("type", type);
        params.put("isValid", 1);
        return locationDao.getChildrenLocationList(params);
    }

    /**
     * 获取一个location下一层的子节点
     *
     * @param locationId
     * @return
     */
    public List<BaseinfoLocation> getNextLevelLocations(Long locationId) {
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

    /**
     * 获取一个location下一层的子节点id
     *
     * @param locationId
     * @return
     */
    public List<Long> getNextLevelLocationIds(Long locationId) {
        List<BaseinfoLocation> locations = this.getNextLevelLocations(locationId);
        return this.getLocationIds(locations);
    }

    /**
     * 获取一个location下所有是存储位的子节点
     *
     * @param locationId
     * @return
     */
    public List<BaseinfoLocation> getStoreLocations(Long locationId) {
        Map<String, Object> params = new HashMap<String, Object>();
        BaseinfoLocation location = this.getLocation(locationId);
        params.put("leftRange", location.getLeftRange());
        params.put("rightRange", location.getRightRange());
        params.put("can_store", 1);
        params.put("isValid", 1);
        return locationDao.getChildrenLocationList(params);
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
        if (curLocation.getType().equals(LocationConstant.LOCATION_TYPE.get(type))) {
            return curLocation;
        }
        if (fatherId == 0) {
            return null;
        }
        return this.getFatherByType(fatherId, type);
    }

    /**
     * 找全路径
     *
     * @param locationId
     * @return
     */
    public List<BaseinfoLocation> getFatherList(Long locationId) {
        List<BaseinfoLocation> baseinfoLocationList = new ArrayList<BaseinfoLocation>();
        BaseinfoLocation curLocation = this.getLocation(locationId);
        Long fatherId = curLocation.getFatherId();
        if (curLocation.getType().equals(LocationConstant.LOCATION_TYPE.get(LocationConstant.WAREHOUSE))) {
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

    /**
     * 获取父级区域节点id
     *
     * @param locationId
     * @return
     */
    public Long getAreaFatherId(Long locationId) {
        BaseinfoLocation areaFatherId = this.getAreaFather(locationId);
        return areaFatherId.getLocationId();
    }

    /**
     * 按类型获取location节点
     *
     * @param type
     * @return
     */
    public List<BaseinfoLocation> getLocationsByType(String type) {
        if (type == null || type.equals("")) {
            return null;
        }
        Map<String, Object> params = new HashMap<String, Object>();
        Long LOCATION_TYPE = LocationConstant.LOCATION_TYPE.get(type);
        params.put("type", LOCATION_TYPE);
        params.put("isValid", 1);
        List<BaseinfoLocation> locations = locationDao.getBaseinfoLocationList(params);
        return locations;
    }

    /**
     * 获取可用仓库根节点
     *
     * @return
     */
    public BaseinfoLocation getWarehouseLocation() {
        List<BaseinfoLocation> locations = this.getLocationsByType("warehouse");
        if (locations.size() > 0) {
            return locations.get(0);
        } else {
            return null;
        }
    }

    /**
     * 获取可用仓库根节点id
     *
     * @return
     */
    public Long getWarehouseLocationId() {
        BaseinfoLocation location = this.getWarehouseLocation();
        return location.getLocationId();
    }

    /**
     * 获取可用盘亏盘盈节点
     *
     * @return
     */
    public BaseinfoLocation getInventoryLostLocation() {
        List<BaseinfoLocation> locations = this.getLocationsByType("inventoryLost");
        if (locations.size() > 0) {
            return locations.get(0);
        } else {
            return null;
        }
    }

    /**
     * 获取可用盘亏盘盈节点id
     *
     * @return
     */
    public Long getInventoryLostLocationId() {
        BaseinfoLocation location = this.getInventoryLostLocation();
        return location.getLocationId();
    }

    /**
     * 获取可用残次区的节点
     *
     * @return
     */
    public BaseinfoLocation getDefectiveLocation() {
        List<BaseinfoLocation> locations = this.getLocationsByType("defective_area");
        if (locations.size() > 0) {
            return locations.get(0);
        } else {
            return null;
        }
    }

    /**
     * 获取可用残次区节点id
     *
     * @return
     */
    public Long getDefectiveLocationId() {
        BaseinfoLocation location = this.getDefectiveLocation();
        return location.getLocationId();
    }

    /**
     * 获取可用退货区节点
     *
     * @return
     */
    public BaseinfoLocation getBackLocation() {
        List<BaseinfoLocation> locations = this.getLocationsByType("back_area");
        if (locations.size() > 0) {
            return locations.get(0);
        } else {
            return null;
        }
    }

    /**
     * 获取可用退货区节点id
     *
     * @return
     */
    public Long getBackLocationId() {
        BaseinfoLocation location = this.getBackLocation();
        return location.getLocationId();
    }

    /**
     * 分配可用暂存区location
     *
     * @param type
     * @return
     */
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

    /**
     * 获取可用暂存区节点id
     *
     * @param type
     * @return
     */
    public Long getAvailableLocationId(String type) {
        BaseinfoLocation location = this.getAvailableLocationByType(type);
        return location.getLocationId();
    }


    /**
     * 分配可用集货区节点
     *
     * @return
     */
    public BaseinfoLocation getCollectionLocation() {
        return this.getAvailableLocationByType("collection_area");
    }

    /**
     * 获取可用的集货节点id
     *
     * @return
     */
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

    /**
     * 获取码头节点id
     *
     * @return
     */
    public Long getDockLocationId() {
        BaseinfoLocation location = this.getDockLocation();
        return location.getLocationId();
    }

    /**
     * 获取货位节点的id
     *
     * @param mapQuery
     * @return
     */
    public List<BaseinfoLocation> getBaseinfoLocationList(Map<String, Object> mapQuery) {
        mapQuery.put("isValid", 1);
        return locationDao.getBaseinfoLocationList(mapQuery);
    }

    // TODO 获取拣货位最近的存储位
    public BaseinfoLocation getNearestStorageByPicking(BaseinfoLocation pickingLocation) {
        return null;
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
    public List<BaseinfoLocation> getLocationListByType(Map<String, Object> mapQuery) {
        mapQuery.put("isValid", 1);
        List<BaseinfoLocation> list = locationDao.getBaseinfoLocationList(mapQuery);
        return list;
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
     * 获取location子代集合的开关
     */
    public static boolean LocationFlag = true;

    public static void setLocationFlag(boolean locationFlag) {
        LocationFlag = locationFlag;
    }

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

    /**
     * 获取在location列表中的码头,按条件筛选
     *
     * @param params
     * @return
     */
    public List<BaseinfoLocation> getDockList(Map<String, Object> params) {
        params.put("isValid",1);
        return locationDao.getDockList(params);
    }

    /**
     * 计数按码头出入库条件筛选的码头条数
     *
     * @param params
     * @return
     */
    public Integer countDockList(Map<String, Object> params) {
        params.put("isValid",1);
        return locationDao.countDockList(params);
    }

}
