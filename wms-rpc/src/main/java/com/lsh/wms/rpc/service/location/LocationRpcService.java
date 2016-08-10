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
    public List<BaseinfoLocation> getChildrenLocations(Long locationId) {
        return locationService.getChildrenLocations(locationId);
    }

    // 获取父级节点
    public BaseinfoLocation getFatherLocation(Long locationId) {
        return locationService.getFatherLocation(locationId);
    }

    // 获取父级区域location节点
    public BaseinfoLocation getFatherByType(Long locationId, String type) {
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

    public BaseinfoLocation insertLocation(BaseinfoLocation location) {
        return locationService.insertLocation(location);
    }

    public BaseinfoLocation updateLocation(BaseinfoLocation location) {
        return locationService.updateLocation(location);
    }

    // 分配暂存区location
    public BaseinfoLocation assignTemporary() {
        return locationService.getAvailableLocationByType("temporary");
    }

    // 分配地堆区location
    public BaseinfoLocation assignFloor() {
        return locationService.getAvailableLocationByType("floor");
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
     * @return
     */
    public List<BaseinfoLocation> getAllShelfs() {
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        //将不同的货架type塞入
        List<BaseinfoLocation> targetList = new ArrayList<BaseinfoLocation>();
        List<Long> regionType = Arrays.asList(LocationConstant.SHELF, LocationConstant.LOFT);
        for (Long oneType:regionType){
            mapQuery.put("type",oneType);
            List<BaseinfoLocation> locationList = locationService.getBaseinfoLocationList(mapQuery);
            if (locationList.size()>0){
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
        mapQuery.put("isValid",LocationConstant.IS_VALID);
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
        mapQuery.put("isValid",LocationConstant.IS_VALID);
        List<BaseinfoLocation> loftColletionBins = locationService.getLocationListByType(mapQuery);
        targetList.addAll(loftColletionBins);
        //货架拣货位
        mapQuery.put("type",LocationConstant.SHELF_PICKING_BIN);
        mapQuery.put("isValid",LocationConstant.IS_VALID);
        List<BaseinfoLocation> shelfColletionBins = locationService.getLocationListByType(mapQuery);
        targetList.addAll(shelfColletionBins);
        return targetList;
    }

    /**
     * 按照dock的选择条件,给出符合DOCK条件的locatinList
     * @param params
     * @return
     */
    public List<BaseinfoLocation> getDockList(Map<String, Object> params) {
        params.put("isValid",LocationConstant.IS_VALID);
        return locationService.getDockList(params);
    }


}
