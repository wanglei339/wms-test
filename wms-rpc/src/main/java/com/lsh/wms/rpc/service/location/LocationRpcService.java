package com.lsh.wms.rpc.service.location;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.wms.api.service.location.ILocationRpcService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.stock.StockQuant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.parsing.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        if (baseinfoLocation.getCanStore()!=0){
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
}
