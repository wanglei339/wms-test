package com.lsh.wms.rpc.service.location;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.api.service.location.ILocationDetailRpc;
import com.lsh.wms.core.service.location.LocationDetailService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.baseinfo.IBaseinfoLocaltionModel;
import com.lsh.wms.rpc.service.stock.StockLotRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 * @Author 马启迪 maqidi@lsh123.com
 * @Date 16/8/6 下午4:08
 */
@Service(protocol = "dubbo")
public class LocationDetailRpcService implements ILocationDetailRpc {
    private static Logger logger = LoggerFactory.getLogger(LocationDetailRpcService.class);
    @Autowired
    private LocationDetailService locationDetailService;
    @Autowired
    private LocationService locationService;

    public IBaseinfoLocaltionModel getLocationDetailById(Long locationId) {
        BaseinfoLocation subLocation = (BaseinfoLocation) locationDetailService.getIBaseinfoLocaltionModelById(locationId);
        return subLocation;
    }

    /**
     * 查找location明细
     *
     * @param params
     * @return
     */
    public List<BaseinfoLocation> getLocationDetailList(Map<String, Object> params) {
        //如果存在码头出入的参数dockApplication,选用对付码头的方法
        if (params.get("dockApplication") != null) {
            List<BaseinfoLocation> baseinfoLocationList = locationDetailService.getDockListByType(params);
            return baseinfoLocationList;
        }
        List<BaseinfoLocation> baseinfoLocationList = locationDetailService.getIBaseinfoLocaltionModelListByType(params);
        return baseinfoLocationList;
    }

    public boolean insertLocationDetailByType(BaseinfoLocation baseinfoLocation) throws BizCheckedException {
        if (locationDetailService.getIBaseinfoLocaltionModelById(baseinfoLocation.getLocationId()) == null) {
            return false;
        }
        try {
            locationDetailService.insert(baseinfoLocation);
        } catch (Exception e) {
            logger.error(e.getCause().getMessage());
            return false;
        }
        return true;
    }

    public boolean updateLocationDetailByType(BaseinfoLocation baseinfoLocation) throws BizCheckedException {
        if (locationDetailService.getIBaseinfoLocaltionModelById(baseinfoLocation.getLocationId()) == null) {
            return false;
        }
        try{
            locationDetailService.update(baseinfoLocation);
        } catch (Exception e){
            logger.error(e.getCause().getMessage());
            return false;
        }
        return true;
    }

    public Integer countLocationDetailByType(Map<String, Object> mapQuery) {
        return locationDetailService.countLocationDetail(mapQuery);

    }

    public boolean removeLocation(Long locationId) throws BizCheckedException {
        BaseinfoLocation location = locationService.getLocation(locationId);
        if (location != null) {
            location.setIsValid(0);
            locationService.updateLocation(location);
            return true;
        }
        return false;
    }
}
