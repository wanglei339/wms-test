package com.lsh.wms.core.service.location;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.base.q.Module.Base;
import com.lsh.wms.api.model.location.LocationDetailRequest;
import com.lsh.wms.core.constant.CustomerConstant;
import com.lsh.wms.core.constant.LocationConstant;
import com.lsh.wms.core.constant.StoreConstant;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.dao.baseinfo.BaseinfoLocationDao;
import com.lsh.wms.core.service.csi.CsiCustomerService;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.baseinfo.BaseinfoStore;
import com.lsh.wms.model.baseinfo.IBaseinfoLocaltionModel;
import com.lsh.wms.model.csi.CsiCustomer;
import com.lsh.wms.model.stock.StockQuant;
import org.apache.commons.collections.MapUtils;
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
    private LocationDetailService locationDetailService;
    @Autowired
    private LocationRedisService locationRedisService;
    @Autowired
    private CsiCustomerService csiCustomerService;


    /**
     * 计数
     * valid一定是1 未删除的
     *
     * @param params
     * @return
     */
    public int countLocation(Map<String, Object> params) {
        params.put("isValid", LocationConstant.IS_VALID);
        return locationDao.countBaseinfoLocation(params);
    }

    /**
     * 根据相同的属性的分类获取指定位置群,入全货架组、全货位组
     *
     * @param classification
     * @return
     * @throws BizCheckedException
     */
    public List<BaseinfoLocation> getLocationsByClassfication(Integer classification) throws BizCheckedException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("classification", classification);
        params.put("isValid", LocationConstant.IS_VALID);
        List<BaseinfoLocation> locations = locationDao.getBaseinfoLocationList(params);
        return locations;
    }

    public BaseinfoLocation getLocation2(Long locationId) throws BizCheckedException {
        logger.error("getLocation2 started!");
        if (null == locationId) {
            throw new BizCheckedException("2180001");
        }
        logger.error("begin fuck you 2 ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("locationId", locationId);
        params.put("isValid", LocationConstant.IS_VALID);
        //params.put("type", 17L);
        logger.error("you say what2");
        List<BaseinfoLocation> locations = locationDao.getBaseinfoLocationList(params);
        logger.error("say again fuck2");
        //redis中没有,放入redis
        if (locations != null && locations.size() > 0) {
//            //将没读入redis的写入redis(直接调用接口写入redis)
//            locationRedisService.insertLocationRedis(locations.get(0));
            logger.error("getLocaton eneded2");
            return locations.get(0);
        } else {
            return null;
        }
    }

    /**
     * 根据locationId获取location
     *
     * @param locationId 位置序列号
     * @return BaseinfoLocation
     */
    public BaseinfoLocation getLocation(Long locationId) throws BizCheckedException {
        logger.error("getLocation started!");
        if (null == locationId) {
            throw new BizCheckedException("2180001");
        }
        logger.error("begin fuck you ");
        //先从redis中取数据,没有去数据库中取
//        Map<String, String> locationMap = locationRedisService.getRedisLocation(locationId);
//        if (locationMap != null && !locationMap.isEmpty()) {
//            BaseinfoLocation location = new BaseinfoLocation();
//            location.setLocationId(Long.valueOf(locationMap.get("locationId")));
//            location.setLocationCode(locationMap.get("locationCode"));
//            location.setFatherId(Long.valueOf(locationMap.get("fatherId")));
//            location.setLeftRange(Long.valueOf(locationMap.get("leftRange")));
//            location.setRightRange(Long.valueOf(locationMap.get("rightRange")));
//            location.setLevel(Long.valueOf(locationMap.get("level")));
//            location.setType(Long.valueOf(locationMap.get("type")));
//            location.setTypeName(locationMap.get("typeName"));
//            location.setIsLeaf(Integer.valueOf(locationMap.get("isLeaf")));
//            location.setIsValid(Integer.valueOf(locationMap.get("isValid")));
//            location.setCanStore(Integer.valueOf(locationMap.get("canStore")));
//            location.setContainerVol(Long.valueOf(locationMap.get("containerVol")));
//            location.setRegionNo(Long.valueOf(locationMap.get("regionNo")));
//            location.setPassageNo(Long.valueOf(locationMap.get("passageNo")));
//            location.setShelfLevelNo(Long.valueOf(locationMap.get("shelfLevelNo")));
//            location.setBinPositionNo(Long.valueOf(locationMap.get("binPositionNo")));
//            location.setCreatedAt(Long.valueOf(locationMap.get("createdAt")));
//            location.setUpdatedAt(Long.valueOf(locationMap.get("updatedAt")));
//            location.setClassification(Integer.valueOf(locationMap.get("classification")));
//            location.setCanUse(Integer.valueOf(locationMap.get("canUse")));
//            location.setIsLocked(Integer.valueOf(locationMap.get("isLocked")));
//            location.setCurContainerVol(Long.valueOf(locationMap.get("curContainerVol")));
//            location.setDescription(locationMap.get("description"));
//            location.setStoreNo(locationMap.get("storeNo").toString());
//            location.setSupplierNo(locationMap.get("storeNo").toString());
//            return location;
//        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("locationId", locationId);
        params.put("isValid", LocationConstant.IS_VALID);
        logger.error("you say what");
        List<BaseinfoLocation> locations = locationDao.getBaseinfoLocationList(params);
        logger.error("say again fuck");
        //redis中没有,放入redis
        if (locations != null && locations.size() > 0) {
//            //将没读入redis的写入redis(直接调用接口写入redis)
//            locationRedisService.insertLocationRedis(locations.get(0));
            logger.error("getLocaton eneded");
            return locations.get(0);
        } else {
            return null;
        }
    }

    /**
     * 插入location方法
     *
     * @param location
     * @return 位置BaseinfoLocation
     */
    @Transactional(readOnly = false)
    public BaseinfoLocation insertLocation(BaseinfoLocation location) {
        location = this.setLocationIdAndRange(location);
        //添加新增时间
        long createdAt = DateUtils.getCurrentSeconds();
        location.setCreatedAt(createdAt);
        location.setUpdatedAt(createdAt);
        locationDao.insert(location);
        //写入缓存
        locationRedisService.insertLocationRedis(location);
        return location;
    }

    /**
     * 更新location
     *
     * @param iBaseinfoLocaltionModel location
     * @return location
     */
    @Transactional(readOnly = false)
    public BaseinfoLocation updateLocation(IBaseinfoLocaltionModel iBaseinfoLocaltionModel) {
        BaseinfoLocation baseinfoLocation = (BaseinfoLocation) iBaseinfoLocaltionModel;
//        if (this.getLocation(baseinfoLocation.getLocationId()) == null) {
//            return null;
//        }
        long updatedAt = DateUtils.getCurrentSeconds();
        baseinfoLocation.setUpdatedAt(updatedAt);
        locationDao.update(baseinfoLocation);
        //写入缓存
        locationRedisService.insertLocationRedis(baseinfoLocation);
        return baseinfoLocation;
    }

    /**
     * 批量更新location
     *
     * @param locations 位置集合list
     * @return
     */
    @Transactional(readOnly = false)
    public void update(List<IBaseinfoLocaltionModel> locations) {
        for (IBaseinfoLocaltionModel location : locations) {
            this.updateLocation(location);
        }
    }

    /**
     * 删除节点和下面的所有子树
     * 并且删除细节表的所有location
     *
     * @param locationId
     * @return location
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
            //删除redis的数据
            locationRedisService.delLocationCodeRedis(child.getLocationCode()); //删除code-locationId
            locationRedisService.delLocationRedis(child.getLocationId());   //删除locationId的hash

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
        params.put("canStore", LocationConstant.CAN_STORE);
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
     *
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
     * 查找位置
     *
     * @param mapQuery
     * @return
     */
    public List<BaseinfoLocation> getBaseinfoLocationList(Map<String, Object> mapQuery) {
        mapQuery.put("isValid", LocationConstant.IS_VALID);
        //locationCode
        String locationCode = (String) mapQuery.get("locationCode");
        if (locationCode != null) {
            locationCode = locationCode + "%";
            mapQuery.put("locationCode", locationCode);
        }

        return locationDao.getBaseinfoLocationList(mapQuery);
    }

    /**
     * 根据货架的拣货位获取货架的最近存货位
     *
     * @param pickingLocation
     * @return
     */
    public BaseinfoLocation getNearestStorageByPicking(BaseinfoLocation pickingLocation) {
        //获取相邻货架的所有拣货位,先获取当前货架,获取通道,货物相邻货架,然后获取
        BaseinfoLocation shelfLocationSelf = this.getShelfByClassification(pickingLocation.getLocationId());    //获取货架
        //通道
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("locationId", shelfLocationSelf.getFatherId());
        List<BaseinfoLocation> passageList = this.getBaseinfoLocationList(params);
        BaseinfoLocation passage = null;
        //将本货架的所有位置放在一个集合中
        List<BaseinfoLocation> tempLocations = new ArrayList<BaseinfoLocation>();
        List<BaseinfoLocation> allNearShelfSubs = null;
        //无论是否存在相邻货架,将一个通道下的所有位置拿出来(必须保证货架个体的father必须是通道)
        passage = passageList.get(0);   //获取通道
        allNearShelfSubs = this.getStoreLocations(passage.getLocationId());
        tempLocations.addAll(allNearShelfSubs);
        //筛选相邻两货架间距离当前拣货位最近的存货位
        BaseinfoLocation nearestLocation = this.filterNearestBinAlgorithm(tempLocations, pickingLocation, shelfLocationSelf, LocationConstant.SHELF_STORE_BIN);
        return nearestLocation;
    }


    /**
     * 获取货架存货位最小位置的方法,选取的集合在相邻的两货架之间(现阶段)
     *
     * @param locations         筛选的存储位置集合
     * @param pickingLocation   拣货位的位置
     * @param shelfLocationSelf 拣货位所在货架(用于判断是否是同一货架)
     * @param type              查找的指定location的集合
     * @return
     */
    public BaseinfoLocation filterNearestBinAlgorithm(List<BaseinfoLocation> locations, BaseinfoLocation pickingLocation, BaseinfoLocation shelfLocationSelf, Long type) {
        List<Map<String, Object>> storeBinDistanceList = new ArrayList<Map<String, Object>>();
        //放入location和当前location到目标位置的距离
        for (BaseinfoLocation temp : locations) {
            //存货位,为空没上锁
            if (temp.getType().equals(type) && this.shelfBinLocationIsEmptyAndUnlock(temp)) {
                // 考虑库存,无库存的货架位才能放入商品
                List<StockQuant> quants = stockQuantService.getQuantsByLocationId(temp.getLocationId());
                if (quants.size() < 1) {
                    //放入location和距离
                    Long distance = (temp.getBinPositionNo() - pickingLocation.getBinPositionNo()) * (temp.getBinPositionNo() - pickingLocation.getBinPositionNo()) + (temp.getShelfLevelNo() - pickingLocation.getShelfLevelNo()) * (temp.getShelfLevelNo() - pickingLocation.getShelfLevelNo());
                    Map<String, Object> distanceMap = new HashMap<String, Object>();
                    distanceMap.put("location", temp);
                    distanceMap.put("distance", distance);
                    storeBinDistanceList.add(distanceMap);
                }
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
                if ((Long.valueOf(((Long) distanceMap.get("distance")).toString()) == Long.valueOf(((Long) minDistanceMap.get("distance")).toString())) && (this.getShelfByClassification(((BaseinfoLocation) distanceMap.get("location")).getLocationId())).getLocationId().equals(shelfLocationSelf.getLocationId())) {
                    //位置相同,同货架优先,同货架位置相同,给一个就行
                    minDistanceMap = distanceMap;
                } else if (Long.valueOf(((Long) distanceMap.get("distance")).toString()) < Long.valueOf(((Long) minDistanceMap.get("distance")).toString())) {
                    minDistanceMap = distanceMap;
                }
            }
            return (BaseinfoLocation) minDistanceMap.get("location");
        } else {
            return null;
        }
    }


    /**
     * todo 因为阁楼和拆零区的货位不是整托,会有同一批次的商品继续分配相同的货位,现阶段用库存为零判断,以后加入其它判断(待产品经理给规则)
     *
     * @param pickingLocation
     * @return
     */
    public BaseinfoLocation getNearestBinInSpiltByPicking(BaseinfoLocation pickingLocation) {
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
        BaseinfoLocation neareatLocation = this.filterNearestBinAlgorithm(tempLocations, pickingLocation, shelfLocationSelf, LocationConstant.SPLIT_SHELF_BIN);
        return neareatLocation;
    }

    /**
     * 获取阁楼拣货位的最近的存货位,不关心当前的托盘数,没达到上线,可以一直放,放什么商品不关心
     *
     * @param pickingLocation 阁楼货架的拣货位
     * @return
     */
    public BaseinfoLocation getNearestBinInLoftByPicking(BaseinfoLocation pickingLocation) {
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
        BaseinfoLocation neareatLocation = this.filterNearestBinAlgorithm(tempLocations, pickingLocation, shelfLocationSelf, LocationConstant.LOFT_STORE_BIN);
        return neareatLocation;
    }


    //获取code
    public String getCodeById(Long locationId) {
        String code = null;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("locationId", locationId);
        params.put("isValid", LocationConstant.IS_VALID);
        List<BaseinfoLocation> baseinfoLocationList = locationDao.getBaseinfoLocationList(params);
        if (baseinfoLocationList.size() > 0) {
            code = baseinfoLocationList.get(0).getLocationCode();
        }
        return code;
    }

    /**
     * 通过位置编码,返回为位置的id
     *
     * @param code
     * @return
     */
    public Long getLocationIdByCode(String code) {
        Long locationId = 0L;
        //先从redis中取code-locaitonId
        locationId = locationRedisService.getRedisLocationIdByCode(code);
        if (null != locationId) {
            return locationId;
        }
        //redis没有去mysql中查
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("locationCode", code);
        params.put("isValid", LocationConstant.IS_VALID);
        List<BaseinfoLocation> baseinfoLocations = locationDao.getLocationbyCode(params);
        if (baseinfoLocations.size() > 0) {
            locationId = baseinfoLocations.get(0).getLocationId();
        }
        return locationId;
    }

    /**
     * 通过位置编码,返回为位置的location
     *
     * @param code
     * @return
     */
    public BaseinfoLocation getLocationByCode(String code) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("locationCode", code);
        params.put("isValid", LocationConstant.IS_VALID);
        List<BaseinfoLocation> baseinfoLocations = locationDao.getLocationbyCode(params);
        if (baseinfoLocations != null && baseinfoLocations.size() > 0) {
            return baseinfoLocations.get(0);
        }
        return null;
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
     * 设置位置没有被占用
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
     * 一库位一托盘码
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
     * 货架位置为空并且没上锁(没占用+没上锁)
     * 一库位一托盘码
     *
     * @param locationId
     * @return
     */
    public boolean shelfBinLocationIsEmptyAndUnlock(Long locationId) {
        BaseinfoLocation location = this.getLocation(locationId);
        if ((location.getCanUse().equals(1)) && location.getIsLocked().equals(0)) {
            return true;
        }
        return false;
    }

    /**
     * 判断位置上当前没托盘切没有被任务锁定
     *
     * @param locationId
     * @return
     */
    public boolean locationIsEmptyAndUnlock(Long locationId) {
        if (this.getLocation(locationId).getCurContainerVol().equals(0L) && !this.checkLocationLockStatus(locationId)) {
            return true;
        }
        return false;
    }

    /**
     * 提供空的可用位置,位置上当前没托盘切没有被任务锁定
     *
     * @param location
     * @return
     */
    public boolean locationIsEmptyAndUnlock(BaseinfoLocation location) {
        if (location.getCurContainerVol().equals(0L) && !this.checkLocationLockStatus(location)) {
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
                if (this.locationIsEmptyAndUnlock(location)) {
                    return location;
                }
            }
        }
        return null;
    }

    /**
     * 根据当前的locationId获取,指定type的子集
     * 如果fatherId不是locationId,那就是祖先的id
     *
     * @param locationId
     * @param type       指定的位置type
     * @return
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
     * 获取在location列表中的码头,按条件筛选(前端使用,用于关联查询join)
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
        logger.error("begin fuck you unlockLocatoin");
        BaseinfoLocation location = this.getLocation2(locationId);
        logger.error("this fucked really hard");
        if (location == null) {
            throw new BizCheckedException("2180001");
        }
        location.setIsLocked(0);    //解锁
        logger.error("begin fuck you update location");
        this.updateLocation(location);
        logger.error("unlock location finished");
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
     * 检查位置的锁状态
     *
     * @param location
     * @return
     */
    public Boolean checkLocationLockStatus(BaseinfoLocation location) {
        if (location.getIsLocked().equals(1)) {
            return true;
        }
        return false;
    }

    /**
     * 根据库区库位类型classification来查到区的级别
     * 区的级别classification=1
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

    public BaseinfoLocation getFatherByClassification(BaseinfoLocation location) {
        Long fatherId = location.getFatherId();
        if (location.getClassification().equals(LocationConstant.REGION_TYPE)) {
            return location;
        }
        if (fatherId == 0) {
            return null;
        }
        return this.getFatherByClassification(fatherId);
    }


    /**
     * 根据货架类型classification来查到区的级别
     * 货架的级别classification=4
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
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("type", type);
        return this.getBaseinfoLocationList(params);
    }

    @Transactional(readOnly = false)
    public void lockLocationById(Long locationId) {
        BaseinfoLocation location = this.getLocation(locationId);
        if (null == location) {
            throw new BizCheckedException("2180002");
        }
        locationDao.lock(location.getId());
    }

    @Transactional(readOnly = false)
    public void lockLocationByContainer(Long containerId) {
        List<Long> locationIdList = stockQuantService.getLocationIdByContainerId(containerId);
        if (1 != locationIdList.size()) {
            throw new BizCheckedException("3550002");
        }
        this.lockLocationById(locationIdList.get(0));
    }

    /**
     * 更新当前容器的容量的方法,并更新canUse
     *
     * @param locationId
     * @param containerVol
     * @return
     */
    @Transactional(readOnly = false)
    public BaseinfoLocation refreshContainerVol(Long locationId, Long containerVol) {
        BaseinfoLocation location = this.getLocation(locationId);
        if (location == null) {
            throw new BizCheckedException("2180001");
        }
        location.setCurContainerVol(containerVol);    //被占用
        //设置状态
        if (this.isOnThreshold(location, containerVol)) {
            location.setCanUse(2);
        } else {
            location.setCanUse(1);
        }
        this.updateLocation(location);
        logger.warn("fuck commit");
        return location;
    }

    /**
     * 是否达到容量上限
     *
     * @param location
     * @param containerVol
     * @return true 达到上限 false没有达到上限
     */
    //判断当前容器的是否达到上限
    public boolean isOnThreshold(BaseinfoLocation location, Long containerVol) {
        if (location.getContainerVol() - containerVol > 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 根据货位查找所在通道
     *
     * @param locationId 货位的id
     * @return
     */
    public BaseinfoLocation getPassageByBin(Long locationId) {
        return this.getFatherByType(locationId, LocationConstant.PASSAGE);
    }

    /**
     * 导入mysql数据库到redis的方法
     */
    public void syncRedisAll() {
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("isValid", LocationConstant.IS_VALID);
        List<BaseinfoLocation> locations = this.getBaseinfoLocationList(mapQuery);
        for (BaseinfoLocation location : locations) {
            locationRedisService.insertLocationRedis(location);
        }
    }

    /**
     * 设置门店号
     *
     * @param location 位置
     * @param storeNo  设置的门店号
     * @return 设置门店号的位置
     * @throws BizCheckedException
     */
    @Transactional(readOnly = false)
    public BaseinfoLocation setStoreNoOnRoad(BaseinfoLocation location, String storeNo) throws BizCheckedException {
        location.setStoreNo(storeNo);
        this.updateLocation(location);
        return location;
    }

    /**
     * 设置门店号
     *
     * @param locationId 位置编号
     * @param storeNo    设置的门店号
     * @return 设置门店号的位置
     * @throws BizCheckedException
     */
    @Transactional(readOnly = false)
    public BaseinfoLocation setStoreNoOnRoad(Long locationId, String storeNo) throws BizCheckedException {
        BaseinfoLocation location = this.getLocation(locationId);
        location.setStoreNo(storeNo);
        this.updateLocation(location);
        return location;
    }

    /**
     * 移除集货道|集货位的门店号,将门店号置为0
     *
     * @param location 位置
     * @return
     * @throws BizCheckedException
     */
    @Transactional(readOnly = false)
    public BaseinfoLocation removeStoreNoOnRoad(BaseinfoLocation location) throws BizCheckedException {
        location.setStoreNo(LocationConstant.REMOVE_STORE_NO);
        this.updateLocation(location);
        return location;
    }

    /**
     * 移除集货道|集货位的门店号,将门店号置为0
     *
     * @param locationId 库位id
     * @return
     * @throws BizCheckedException
     */
    @Transactional(readOnly = false)
    public BaseinfoLocation removeStoreNoOnRoad(Long locationId) throws BizCheckedException {
        BaseinfoLocation location = this.getLocation(locationId);
        location.setStoreNo(LocationConstant.REMOVE_STORE_NO);
        this.updateLocation(location);
        return location;
    }

    /**
     * 查找指定门店号的集货位置,集货道|集货位
     *  todo 放入集货道
     * @param storeNo
     * @return
     */
    public List<BaseinfoLocation> getCollectionByStoreNo(String storeNo) throws BizCheckedException {
        //CsiCustomer csiCustomer = csiCustomerService.getCustomerByCustomerCode(ownerId,storeNo);
        //if (null == csiCustomer) {
        //    throw new BizCheckedException("2180012");
        //}
        Map<String,Object> mapQuery = new HashMap<String, Object>();
        //if (CustomerConstant.STORE.equals(csiCustomer.getCustomerType())) { //小店
        //    mapQuery.put("type", LocationConstant.COLLECTION_BIN);
//
  //      } else {
    //        mapQuery.put("type", LocationConstant.COLLECTION_ROAD);
      //  }
        mapQuery.put("storeNo", storeNo);
        List<BaseinfoLocation> list = this.getBaseinfoLocationList(mapQuery);
        if (null == list || list.size() < 1) {
            //throw new BizCheckedException("2180012");
            return new LinkedList<BaseinfoLocation>();
        }
        return list;
    }

    /**
     * 查找指定门店号的播种位置
     *
     * @param storeNo
     * @return
     */
    public List<BaseinfoLocation> getSowByStoreNo(String storeNo) throws BizCheckedException {
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("storeNo", storeNo);
        mapQuery.put("type", LocationConstant.SOW_BIN);
        List<BaseinfoLocation> list = this.getBaseinfoLocationList(mapQuery);
        if (null == list || list.size() < 1) {
            //throw new BizCheckedException("2180012");
            return new LinkedList<BaseinfoLocation>();
        }

        return list;
    }

    /**
     * 获取按门店号升序排序好的位置
     *
     * @param type 门店位置(播种货位|集货货位)
     * @return
     */
    public List<BaseinfoLocation> sortLocationByStoreNo(Long type) {
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("type", type);
        mapQuery.put("storeNo", LocationConstant.REMOVE_STORE_NO);   //这里使用的mapper是 storeNO>0, 0是在库的代号
        List<BaseinfoLocation> locations = locationDao.sortLocationByStoreNoAndType(mapQuery); //0不是门店的位置,查找的是大于0的结果
        return locations;
    }

    /**
     * 根据供商号，获取位置
     *
     * @param type       (退货存储货位|入库货位)
     * @param supplierNo (供商号)
     * @return
     */
    public List<BaseinfoLocation> getLocationBySupplierNo(Long type, String supplierNo) {
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("type", type);
        mapQuery.put("isValid", LocationConstant.IS_VALID);
        mapQuery.put("supplierNo", supplierNo);
        List<BaseinfoLocation> locations = locationDao.getBaseinfoLocationList(mapQuery);
        return locations;
    }

    /**
     * 传入left和right查询 固定列区间的库位
     *
     * @param mapQuery
     * @return
     */
    public List<BaseinfoLocation> getRangeLocationList(Map<String, Object> mapQuery) {
        mapQuery.put("isValid", LocationConstant.IS_VALID);
        List<BaseinfoLocation> locations = locationDao.getRangeLocationList(mapQuery);
        return locations;
    }

    /**
     * 初始化构建整棵location树结构
     */
    @Transactional(readOnly = false)
    public void initLocationTree(Map<String, Object> config, Long fatherId) {
        LocationDetailRequest detailRequest = new LocationDetailRequest();
        BaseinfoLocation father = this.getLocation(fatherId);
        List<Map<String, Object>> levels = new ArrayList<Map<String, Object>>();
        Long regionNo = 0L;
        Long passageNo = 0L;
        Long shelfLevelNo = 0L;
        Long binPositionNo = 0L;
        if (config.get("levels") != null) {
            levels = (List<Map<String,Object>>)config.get("levels");
        } else {
            levels.add(config);
        }
        for (Map<String, Object> conf: levels) {
            Integer counts = 1;
            BaseinfoLocation location = new BaseinfoLocation();
            if (conf.get("counts") != null) {
                counts = Integer.valueOf(conf.get("counts").toString());
            }
            for (Integer i = 1; i <= counts; i++) {
                Long type = Long.valueOf(conf.get("type").toString());
                String typeName = "";
                detailRequest.setType(type);
                detailRequest.setLocationCode(conf.get("locationCode").toString());
                if (father != null) {
                    String code = "";
                    if (father.getType().equals(LocationConstant.WAREHOUSE) || father.getType().equals(LocationConstant.REGION_AREA)) {
                        code = String.format(conf.get("locationCode").toString(), i);
                    } else {
                        code = father.getLocationCode() + String.format(conf.get("locationCode").toString(), i);
                    }
                    detailRequest.setLocationCode(code);
                }
                detailRequest.setFatherId(fatherId);
                Integer classification = 3;
                if (type.equals(LocationConstant.REGION_AREA)) {
                    classification = LocationConstant.CLASSIFICATION_AREAS;
                }
                if (LocationConstant.LOCATION_TYPE_NAME.get(type) != null) {
                    typeName = LocationConstant.LOCATION_TYPE_NAME.get(type);
                }
                Integer canStore = 1;
                if (conf.get("canStore") != null) {
                    canStore = Integer.valueOf(conf.get("canStore").toString());
                }
                detailRequest.setTypeName(typeName);
                detailRequest.setIsLeaf(0);
                detailRequest.setIsValid(1);
                detailRequest.setCanStore(canStore);
                if (conf.get("regionNo") != null) {
                    regionNo = Long.valueOf(conf.get("regionNo").toString());
                }
                conf.put("regionNo", regionNo);
                if (conf.get("passageNo") != null) {
                    passageNo = Long.valueOf(conf.get("passageNo").toString());
                    if (conf.get("isPassage") != null && Boolean.parseBoolean(conf.get("isPassage").toString())) {
                        passageNo++;
                    }
                }
                conf.put("passageNo", passageNo);
                if (conf.get("shelfLevelNo") != null) {
                    shelfLevelNo = Long.valueOf(conf.get("shelfLevelNo").toString());
                    if (conf.get("isLevel") != conf.get("isLevel") && Boolean.parseBoolean(conf.get("isLevel").toString())) {
                        shelfLevelNo++;
                    }
                }
                conf.put("shelfLevelNo", shelfLevelNo);
                if (conf.get("binPositionNo") != null) {
                    binPositionNo = Long.valueOf(conf.get("binPositionNo").toString());
                    if (conf.get("isBin") != null && Boolean.parseBoolean(conf.get("isBin").toString())) {
                        binPositionNo++;
                    }
                }
                conf.put("binPositionNo", binPositionNo);
                detailRequest.setContainerVol(Long.valueOf(conf.get("containerVol").toString()));
                detailRequest.setRegionNo(Long.valueOf(conf.get("regionNo").toString()));
                detailRequest.setPassageNo(Long.valueOf(conf.get("passageNo").toString()));
                detailRequest.setShelfLevelNo(Long.valueOf(conf.get("shelfLevelNo").toString()));
                detailRequest.setBinPositionNo(Long.valueOf(conf.get("binPositionNo").toString()));
                detailRequest.setDescription("");
                detailRequest.setClassification(LocationConstant.CLASSIFICATION_OTHERS);
                detailRequest.setCanUse(1);
                detailRequest.setIsLocked(0);
                detailRequest.setCurContainerVol(0L);
                detailRequest.setStoreNo("");
                detailRequest.setSupplierNo("");
                location = locationDetailService.insert(detailRequest);
                if (conf.get("children") != null) {
                    List<Map<String, Object>> children = (List<Map<String, Object>>) conf.get("children");
                    for (Map<String, Object> child : children) {
                        if (child.get("regionNo") == null) {
                            child.put("regionNo", location.getRegionNo());
                        }
                        if (child.get("passageNo") == null) {
                            child.put("passageNo", location.getPassageNo());
                        }
                        if (child.get("shelfLevelNo") == null) {
                            child.put("shelfLevelNo", location.getShelfLevelNo());
                        }
                        if (child.get("binPositionNo") == null) {
                            child.put("binPositionNo", location.getBinPositionNo());
                        }
                        this.initLocationTree(child, location.getLocationId());
                    }
                }
            }
            father = location;
        }
    }
}
