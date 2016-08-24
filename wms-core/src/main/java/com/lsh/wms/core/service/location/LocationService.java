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
    @Autowired
    private BaseinfoLocationShelfService baseinfoLocationShelfService;
    @Autowired
    private LocationDetailService locationDetailService;


    //计数
    //valid一定是1 未删除的
    public int countLocation(Map<String, Object> params) {
        params.put("isValid", LocationConstant.IS_VALID);
        return locationDao.countBaseinfoLocation(params);
    }

    // 获取location
    public BaseinfoLocation getLocation(Long locationId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("locationId", locationId);
        params.put("isValid", LocationConstant.IS_VALID);
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
     * 并且删除细节表的所有location
     * @param locationId
     * @return
     */
    @Transactional(readOnly = false)
    public BaseinfoLocation removeLocationAndChildren(Long locationId) throws BizCheckedException {
        BaseinfoLocation location = this.getLocation(locationId);
        //找不到
        if (null == location) {
            throw new BizCheckedException("2180003");
        }
        //将location的子树查出来isvalid置为0
        List<BaseinfoLocation> childrenList = this.getChildrenLocations(locationId);
        for (BaseinfoLocation child : childrenList) {
            child.setIsValid(LocationConstant.NOT_VALID);
            updateLocation(child);
            locationDetailService.removeLocationDetail(child);
        }
        //删除该节点
        location.setIsValid(LocationConstant.NOT_VALID);
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
        params.put("isValid", LocationConstant.IS_VALID);
        return locationDao.getChildrenLocationList(params);
    }

    /**
     * 根据type获取子节点
     *
     * @param locationId
     * @param type
     * @return
     */
    public List<BaseinfoLocation> getChildrenLocationsByType(Long locationId, Long type) {
        Map<String, Object> params = new HashMap<String, Object>();
        BaseinfoLocation location = this.getLocation(locationId);
        params.put("leftRange", location.getLeftRange());
        params.put("rightRange", location.getRightRange());
        params.put("type", type);
        params.put("isValid", LocationConstant.IS_VALID);
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
        params.put("isValid", LocationConstant.IS_VALID);
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
        params.put("isValid", LocationConstant.IS_VALID);
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
    public BaseinfoLocation getFatherByType(Long locationId, Long type) {
        BaseinfoLocation curLocation = this.getLocation(locationId);
        Long fatherId = curLocation.getFatherId();
        if (curLocation.getType().equals(type)) {
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
        if (curLocation.getType().equals(LocationConstant.WAREHOUSE)) {
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
    public Long getFatherIdByType(Long locationId, Long type) {
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
        BaseinfoLocation areaFather = this.getFatherByType(locationId, LocationConstant.REGION_AREA);
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
    public List<BaseinfoLocation> getLocationsByType(Long type) {
        if (type == null) {
            return null;
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("type", type);
        params.put("isValid", LocationConstant.IS_VALID);
        List<BaseinfoLocation> locations = locationDao.getBaseinfoLocationList(params);
        return locations;
    }

    /**
     * 获取可用仓库根节点
     *
     * @return
     */
    public BaseinfoLocation getWarehouseLocation() {
        List<BaseinfoLocation> locations = this.getLocationsByType(LocationConstant.WAREHOUSE);
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
        List<BaseinfoLocation> locations = this.getLocationsByType(LocationConstant.INVENTORYLOST);
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
        List<BaseinfoLocation> locations = this.getLocationsByType(LocationConstant.DEFECTIVE_AREA);
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
        List<BaseinfoLocation> locations = this.getLocationsByType(LocationConstant.BACK_AREA);
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
     * 分配可用可用location
     *
     * @param type
     * @return
     */
    public BaseinfoLocation getAvailableLocationByType(Long type) {
        List<BaseinfoLocation> locations = this.getLocationsByType(type);
        if (locations.size() > 0) {
            for (BaseinfoLocation location : locations) {
                if (location.getCanUse().equals(1) && !this.checkLocationLockStatus(location.getLocationId())) {
                    return location;
                }
            }
        }
        return null;
    }

    /**
     * 获取可用的地堆区,需保证是同一批次
     * @return
     */
    public BaseinfoLocation getAvailableFloorLocation(Long lotId) {
        List<BaseinfoLocation> locations = this.getLocationsByType(LocationConstant.FLOOR);
        if (locations.size() > 0) {
            for (BaseinfoLocation location : locations) {
                Long locationId = location.getLocationId();
                if (location.getCanUse().equals(1) && !this.checkLocationLockStatus(locationId)) {
                    List<StockQuant> quants = stockQuantService.getQuantsByLocationId(locationId);
                    if (quants.isEmpty()) {
                        return location;
                    } else {
                        StockQuant quant = quants.get(0);
                        if (quant.getLotId().equals(lotId)) {
                            return location;
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * 获取可用Location节点id
     *
     * @param type
     * @return
     */
    public Long getAvailableLocationId(Long type) {
        BaseinfoLocation location = this.getAvailableLocationByType(type);
        return location.getLocationId();
    }


    /**
     * 分配可用集货区节点
     *
     * @return
     */
    public BaseinfoLocation getCollectionLocation() {
        return this.getAvailableLocationByType(LocationConstant.COLLECTION_AREA);
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
        List<BaseinfoLocation> locations = this.getLocationsByType(LocationConstant.DOCK_AREA);
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
     * 一次去数据库中取两个相邻货架的拣货位,然后按照筛选规则去筛选存货位
     *
     * @param mapQuery
     * @return
     */
    public List<BaseinfoLocation> getBaseinfoLocationList(Map<String, Object> mapQuery) {
        mapQuery.put("isValid", LocationConstant.IS_VALID);
        return locationDao.getBaseinfoLocationList(mapQuery);
    }

    /**
     * 根据货架的拣货位获取货架的最近存货位
     * TODO 获取拣货位最近的存储位 (待检测)
     *
     * @param pickingLocation
     * @return
     */
    public BaseinfoLocation getNearestStorageByPicking(BaseinfoLocation pickingLocation) {
        //获取相邻货架的所有拣货位,先获取当前货架,获取通道,货物相邻货架,然后获取
        BaseinfoLocation shelfLocationSelf = this.getShelfByClassification(pickingLocation.getLocationId());
        //通道
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("locationId", shelfLocationSelf.getFatherId());
        List<BaseinfoLocation> passageList = this.getBaseinfoLocationList(params);
        BaseinfoLocation passage = null;
        //将本货架的所有位置放在一个集合中
        List<BaseinfoLocation> tempLocations = new ArrayList<BaseinfoLocation>();
        List<BaseinfoLocation> allNearShelfSubs = null;
        //无论是否存在相邻货架,将一个通道下的所有位置拿出来(必须保证货架个体的father必须是通道)
        passage = passageList.get(0);
        allNearShelfSubs = this.getStoreLocations(passage.getLocationId());
        tempLocations.addAll(allNearShelfSubs);
        List<Map<String, Object>> storeBinDistanceList = new ArrayList<Map<String, Object>>();
        //放入location和当前location到目标位置的距离
        for (BaseinfoLocation temp : tempLocations) {
            //存货位,为空没上锁
            if (temp.getType().equals(LocationConstant.SHELF_STORE_BIN) && this.shelfBinLocationIsEmptyAndUnlock(temp)) {
                //放入location和距离
                Long distance = (temp.getBinPositionNo() - pickingLocation.getBinPositionNo()) * (temp.getBinPositionNo() - pickingLocation.getBinPositionNo()) + (temp.getShelfLevelNo() - pickingLocation.getShelfLevelNo()) * (temp.getShelfLevelNo() - pickingLocation.getShelfLevelNo());
                Map<String, Object> distanceMap = new HashMap<String, Object>();
                distanceMap.put("location", temp);
                distanceMap.put("distance", distance);
                storeBinDistanceList.add(distanceMap);
            }
        }
        //遍历距离的list,根据map的ditance的取出最小的
        if (storeBinDistanceList.size() > 0) {
            Map<String, Object> minDistanceMap = new HashMap<String, Object>();
            BaseinfoLocation location = (BaseinfoLocation) storeBinDistanceList.get(0).get("location");
            Long minDistance = (Long) storeBinDistanceList.get(0).get("distance");
            minDistanceMap.put("location", location);
            minDistanceMap.put("distance", minDistance);
            for (Map<String, Object> distanceMap : storeBinDistanceList) {
                if ((Long.parseLong(((Long) distanceMap.get("distance")).toString()) == Long.parseLong(((Long) minDistanceMap.get("distance")).toString())) && (this.getShelfByClassification(((BaseinfoLocation) distanceMap.get("location")).getLocationId())).getLocationId().equals(shelfLocationSelf.getLocationId())) {
                    //位置相同,同货架优先,同货架位置相同,给一个就行
                    minDistanceMap = distanceMap;
                } else if (Long.parseLong(((Long) distanceMap.get("distance")).toString()) < Long.parseLong(((Long) minDistanceMap.get("distance")).toString())) {
                    minDistanceMap = distanceMap;
                }
            }
            return (BaseinfoLocation) minDistanceMap.get("location");
        } else {
            return null;
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
     * 根据type,isvalid和或者code获取location的集合,主要和查询有关
     *
     * @param mapQuery 前端传过来的map参数
     * @return
     */
    public List<BaseinfoLocation> getLocationListByType(Map<String, Object> mapQuery) {
        mapQuery.put("isValid", LocationConstant.IS_VALID);
        List<BaseinfoLocation> list = locationDao.getBaseinfoLocationList(mapQuery);
        return list;
    }

    /**
     * 判断位置上是否有库存, 判断占用情况应该使用location.getCanUse(),即位置上是否是空的
     *
     * @param locationId
     * @return
     */
    public Boolean isQuantInLocation(Long locationId) {
        List<StockQuant> quants = stockQuantService.getQuantsByLocationId(locationId);
        if (quants.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 设置location被占用
     *
     * @param locationId
     * @return
     */
    @Transactional(readOnly = false)
    public BaseinfoLocation setLocationIsOccupied(Long locationId) {
        BaseinfoLocation location = this.getLocation(locationId);
        if (location == null) {
            throw new BizCheckedException("2180001");
        }
        location.setCanUse(2);    //被占用
        this.updateLocation(location);
        return location;
    }

    /**
     * 设置为止没有被占用
     *
     * @param locationId
     * @return
     */
    @Transactional(readOnly = false)
    public BaseinfoLocation setLocationUnOccupied(Long locationId) {
        BaseinfoLocation location = this.getLocation(locationId);
        if (location == null) {
            throw new BizCheckedException("2180001");
        }
        location.setCanUse(1);    //被未被使用
        this.updateLocation(location);
        return location;
    }

    /**
     * 查看位置现在是否能继续使用,(没上满|没库存的)都是能继续使用的,对于库位
     *
     * @param locationId
     * @return
     */
    public Boolean checkLocationUseStatus(Long locationId) {
        BaseinfoLocation location = this.getLocation(locationId);
        if (location.getCanUse().equals(1)) {
            return true;
        }
        return false;
    }

    /**
     * 货架位置为空并且没上锁(没占用+没上锁)
     * 一库位一托盘
     *
     * @param location
     * @return
     */
    public boolean shelfBinLocationIsEmptyAndUnlock(BaseinfoLocation location) {
        if ((location.getCanUse().equals(1)) && location.getIsLocked().equals(0)) {
            return true;
        }
        return false;
    }

    /**
     * 位置为空,切无库存
     *
     * @param locationId
     * @return
     */
    public boolean locationIsEmptyAndUnlock(Long locationId) {
        if ((!this.isQuantInLocation(locationId)) && !this.checkLocationLockStatus(locationId)) {
            return true;
        }
        return false;
    }

    /**
     * 分配可用可用location
     *
     * @param type
     * @return
     */
    public BaseinfoLocation getlocationIsEmptyAndUnlockByType(Long type) {
        List<BaseinfoLocation> locations = this.getLocationsByType(type);
        if (locations.size() > 0) {
            for (BaseinfoLocation location : locations) {
                Long locationId = location.getLocationId();
                if ((!this.isQuantInLocation(locationId)) && (!this.checkLocationLockStatus(locationId))) {
                    return location;
                }
            }
        }
        return null;
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
            if (baseinfoLocation.getType().equals(type)) {
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
        params.put("isValid", LocationConstant.IS_VALID);
        return locationDao.getDockList(params);
    }

    /**
     * 计数按码头出入库条件筛选的码头条数
     *
     * @param params
     * @return
     */
    public Integer countDockList(Map<String, Object> params) {
        params.put("isValid", LocationConstant.IS_VALID);
        return locationDao.countDockList(params);
    }

    /**
     * location上锁
     *
     * @return
     */
    @Transactional(readOnly = false)
    public BaseinfoLocation lockLocation(Long locationId) {
        BaseinfoLocation location = this.getLocation(locationId);
        if (location == null) {
            throw new BizCheckedException("2180001");
        }
        location.setIsLocked(1);    //上锁
        this.updateLocation(location);
        return location;
    }

    /**
     * 解锁
     *
     * @param locationId
     * @return
     */
    @Transactional(readOnly = false)
    public BaseinfoLocation unlockLocation(Long locationId) {
        BaseinfoLocation location = this.getLocation(locationId);
        if (location == null) {
            throw new BizCheckedException("2180001");
        }
        location.setIsLocked(0);    //解锁
        this.updateLocation(location);
        return location;
    }

    /**
     * 检查位置的锁状态
     *
     * @param locationId
     * @return
     */
    public Boolean checkLocationLockStatus(Long locationId) {
        BaseinfoLocation location = this.getLocation(locationId);
        if (location.getIsLocked().equals(1)) {
            return true;
        }
        return false;
    }

    /**
     * 根据库区库位类型classification来查到区的级别
     *
     * @param locationId
     * @return
     */
    public BaseinfoLocation getFatherByClassification(Long locationId) {
        BaseinfoLocation curLocation = this.getLocation(locationId);
        Long fatherId = curLocation.getFatherId();
        if (curLocation.getClassification().equals(LocationConstant.REGION_TYPE)) {
            return curLocation;
        }
        if (fatherId == 0) {
            return null;
        }
        return this.getFatherByClassification(fatherId);
    }

    /**
     * 根据货架类型classification来查到区的级别
     *
     * @param locationId
     * @return
     */
    public BaseinfoLocation getShelfByClassification(Long locationId) {
        BaseinfoLocation curLocation = this.getLocation(locationId);
        Long fatherId = curLocation.getFatherId();
        if (curLocation.getClassification().equals(LocationConstant.LOFT_SHELF)) {
            return curLocation;
        }
        if (fatherId == 0) {
            return null;
        }
        return this.getShelfByClassification(fatherId);
    }

    /**
     * 获取指定位置类型的方法,建议使用这个方法获取指定type的方法
     *
     * @param type 位置类型
     * @return
     */
    public List<BaseinfoLocation> getTargetLocationListByType(Long type) {
        Long targetType = Long.parseLong(type.toString());
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("type", targetType);
        return this.getBaseinfoLocationList(params);
    }

    @Transactional(readOnly = false)
    public void lockLocationById(Long locationId) {
        if (null == this.getLocation(locationId)) {
            throw new BizCheckedException("2180002");
        }
        locationDao.lock(locationId);
    }

    @Transactional(readOnly = false)
    public void lockLocationByContainer(Long containerId) {
        List<Long> locationIdList = stockQuantService.getLocationIdByContainerId(containerId);
        if (1 != locationIdList.size()) {
            throw new BizCheckedException("3050002");
        }
        this.lockLocationById(locationIdList.get(0));
    }
}
