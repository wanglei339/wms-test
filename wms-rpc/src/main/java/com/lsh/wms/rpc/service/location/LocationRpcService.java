package com.lsh.wms.rpc.service.location;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.service.location.ILocationRpcService;
import com.lsh.wms.core.constant.LocationConstant;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.baseinfo.IBaseinfoLocaltionModel;
import com.lsh.wms.model.stock.StockQuant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * Created by fengkun on 16/7/11.
 */

@Service(protocol = "dubbo")
public class LocationRpcService implements ILocationRpcService {
    private static Logger logger = LoggerFactory.getLogger(LocationRpcService.class);

    @Autowired
    private LocationService locationService;
    @Autowired
    private StockQuantService stockQuantService;

    public BaseinfoLocation getLocation(Long locationId) throws BizCheckedException {
        BaseinfoLocation baseinfoLocation = locationService.getLocation(locationId);
        if (null == baseinfoLocation) {
            throw new BizCheckedException("2180001");
        }
        return baseinfoLocation;
    }

    // 获取一个location下所有是存储位的子节点
    public List<BaseinfoLocation> getStoreLocations(Long locationId) throws BizCheckedException {
        return locationService.getStoreLocations(locationId);
    }

    // 获取一个location下一层的子节点
    public List<BaseinfoLocation> getNextLevelLocations(Long locationId) throws BizCheckedException {
        return locationService.getNextLevelLocations(locationId);
    }

    // 获取父级节点
    public BaseinfoLocation getFatherLocation(Long locationId) throws BizCheckedException {
        return locationService.getFatherLocation(locationId);
    }

    // 获取父级区域location节点
    public BaseinfoLocation getFatherByType(Long locationId, Long type) throws BizCheckedException {
        return locationService.getFatherByType(locationId, type);
    }

    //提供位置能否存储存
    public boolean canStore(Long locationId) throws BizCheckedException {
        BaseinfoLocation baseinfoLocation = locationService.getLocation(locationId);
        if (baseinfoLocation.getCanStore() != 0) {
            return true;
        }
        return false;
    }

    // todo 插入集货道和集货道组,需要用此insert方法,因为只是插入主表
    public BaseinfoLocation insertLocation(BaseinfoLocation location) throws BizCheckedException {
        return locationService.insertLocation(location);
    }

    public BaseinfoLocation updateLocation(BaseinfoLocation location) throws BizCheckedException {
        return locationService.updateLocation(location);
    }

    // 分配暂存区location
    public BaseinfoLocation assignTemporary() {
        return locationService.getAvailableLocationByType(LocationConstant.TEMPORARY);
    }

    // 分配地堆区location
    public BaseinfoLocation assignFloor(StockQuant quant) throws BizCheckedException {
        return locationService.getAvailableFloorLocation(quant.getLotId());
    }


    //分配退货区
    public BaseinfoLocation getBackLocation() {
        return locationService.getBackLocation();
    }

    //分配残次区
    public BaseinfoLocation getDefectiveLocation() {
        return locationService.getDefectiveLocation();
    }

    /**
     * 获取全货区
     *
     * @return
     */
    public List<BaseinfoLocation> getAllRegion() {
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("classification", 1);
        return locationService.getBaseinfoLocationList(mapQuery);
    }

    /**
     * 获取全货架
     *
     * @return
     */
    public List<BaseinfoLocation> getAllShelfs() {
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        //将不同的货架type塞入
        List<BaseinfoLocation> targetList = new ArrayList<BaseinfoLocation>();
        List<Long> regionType = Arrays.asList(LocationConstant.SHELF, LocationConstant.LOFT);
        for (Long oneType : regionType) {
            mapQuery.put("type", oneType);
            List<BaseinfoLocation> locationList = locationService.getBaseinfoLocationList(mapQuery);
            if (locationList.size() > 0) {
                targetList.addAll(locationList);
            }
        }
        return targetList;
    }

    /**
     * 获取全货位
     *
     * @return
     */
    public List<BaseinfoLocation> getAllBin() {
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("classification", 2);
        mapQuery.put("isValid", LocationConstant.IS_VALID);
        return locationService.getBaseinfoLocationList(mapQuery);
    }

    /**
     * 获取所有的拣货位
     *
     * @return
     */
    public List<BaseinfoLocation> getColletionBins() {
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        List<BaseinfoLocation> targetList = new ArrayList<BaseinfoLocation>();
        //放入阁楼拣货位
        mapQuery.put("type", LocationConstant.LOFT_PICKING_BIN);
        mapQuery.put("isValid", LocationConstant.IS_VALID);
        List<BaseinfoLocation> loftColletionBins = locationService.getLocationListByType(mapQuery);
        targetList.addAll(loftColletionBins);
        //货架拣货位
        mapQuery.put("type", LocationConstant.SHELF_PICKING_BIN);
        mapQuery.put("isValid", LocationConstant.IS_VALID);
        List<BaseinfoLocation> shelfColletionBins = locationService.getLocationListByType(mapQuery);
        targetList.addAll(shelfColletionBins);
        return targetList;
    }

    /**
     * 位置上锁
     *
     * @param locationId
     * @return
     */
    public BaseinfoLocation lockLocation(Long locationId) throws BizCheckedException {
        return locationService.lockLocation(locationId);
    }

    /**
     * 位置解锁
     *
     * @param locationId
     * @return
     */
    public BaseinfoLocation unlockLocation(Long locationId) throws BizCheckedException {
        return locationService.unlockLocation(locationId);
    }

    /**
     * 同通道的位置排序
     *
     * @param locations 同通道的位置list
     * @return 按列递增的位置
     * @throws BizCheckedException
     */
    public List<BaseinfoLocation> sortLocationInOnePassage(List<BaseinfoLocation> locations) throws BizCheckedException {
        //sort位置排序
        if (locations == null || locations.isEmpty()) {
            throw new BizCheckedException("2180007");
        }
        Collections.sort(locations, new Comparator<BaseinfoLocation>() {
            public int compare(BaseinfoLocation o1, BaseinfoLocation o2) {
                return (o1.getBinPositionNo() > o2.getBinPositionNo()) ? 1 : ((o1.getBinPositionNo() == o2.getBinPositionNo()) ? 0 : -1);
            }
        });
        return locations;
    }

    /**
     * 将同一区下的所有通道按与传入通道的距离值进行排序
     * 效果 5 3 1 | 2 4 6 (与中间的通道成镜面对称)
     *
     * @param location 传入通道
     * @return
     */
    public List<BaseinfoLocation> getNearestPassage(BaseinfoLocation location) {
        //保证同一区域下的所有通道,最近的两个,每次作用自增1,拿出所有的region下的通道
        List<BaseinfoLocation> passageList = locationService.getChildrenLocationsByType(location.getFatherId(), LocationConstant.PASSAGE);
        List<Map<String, Object>> passageDistanceList = new ArrayList<Map<String, Object>>();
        for (BaseinfoLocation temp : passageList) {
            Map<String, Object> tempMap = new HashMap<String, Object>();
            //距离目标通道的距离
            tempMap.put("passageDisance", (temp.getPassageNo() - location.getPassageNo()) * (temp.getPassageNo() - location.getPassageNo()));
            tempMap.put("location", temp);
        }
        //按距离排序,升序
        Collections.sort(passageDistanceList, new Comparator<Map<String, Object>>() {
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                return (Long) o1.get("passageDisance") > (Long) o2.get("passageDisance") ? 1 : ((Long) o1.get("passageDisance") == (Long) o2.get("passageDisance") ? 0 : -1);
            }
        });
        //结果放入List
        List<BaseinfoLocation> sortList = new ArrayList<BaseinfoLocation>();
        for (Map<String, Object> mapTemp : passageDistanceList) {
                sortList.add((BaseinfoLocation) mapTemp.get("location"));
        }
        return sortList;
    }

}
