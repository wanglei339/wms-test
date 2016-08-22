package com.lsh.wms.rpc.service.location;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.service.location.ILocationRpcService;
import com.lsh.wms.core.constant.LocationConstant;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.baseinfo.IBaseinfoLocaltionModel;
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

    public BaseinfoLocation getLocation(Long locationId) {
        return locationService.getLocation(locationId);
    }

    // 获取一个location下所有是存储位的子节点
    public List<BaseinfoLocation> getStoreLocations(Long locationId) {
        return locationService.getStoreLocations(locationId);
    }

    // 获取一个location下一层的子节点
    public List<BaseinfoLocation> getNextLevelLocations(Long locationId) {
        return locationService.getNextLevelLocations(locationId);
    }

    // 获取父级节点
    public BaseinfoLocation getFatherLocation(Long locationId) {
        return locationService.getFatherLocation(locationId);
    }

    // 获取父级区域location节点
    public BaseinfoLocation getFatherByType(Long locationId, Long type) {
        return locationService.getFatherByType(locationId, type);
    }

    //提供位置能否存储存
    public boolean canStore(Long locationId) {
        BaseinfoLocation baseinfoLocation = locationService.getLocation(locationId);
        if (baseinfoLocation.getCanStore() != 0) {
            return true;
        }
        return false;
    }
    // todo 插入集货道和集货道组,需要用此insert方法,因为只是插入主表
    public BaseinfoLocation insertLocation(BaseinfoLocation location) {
        return locationService.insertLocation(location);
    }

    public BaseinfoLocation updateLocation(BaseinfoLocation location) {
        return locationService.updateLocation(location);
    }

    // 分配暂存区location
    public BaseinfoLocation assignTemporary() {
        return locationService.getAvailableLocationByType(LocationConstant.TEMPORARY);
    }

    // 分配地堆区location
    public BaseinfoLocation assignFloor() {
        return locationService.getAvailableLocationByType(LocationConstant.FLOOR);
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
    public BaseinfoLocation lockLocation(Long locationId) {
        return locationService.lockLocation(locationId);
    }

    /**
     * 位置解锁
     *
     * @param locationId
     * @return
     */
    public BaseinfoLocation unlockLocation(Long locationId) {
        return locationService.unlockLocation(locationId);
    }

}
