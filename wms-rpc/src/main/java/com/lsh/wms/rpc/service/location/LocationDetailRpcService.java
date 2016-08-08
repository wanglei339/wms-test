package com.lsh.wms.rpc.service.location;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.api.service.location.ILocationDetailRpc;
import com.lsh.wms.core.service.location.LocationDetailService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.baseinfo.IBaseinfoLocaltionModel;
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
    @Autowired
    private LocationDetailService locationDetailService;
    @Autowired
    private LocationService locationService;

    public IBaseinfoLocaltionModel getLocationDetailById(Long locationId) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        BaseinfoLocation subLocation = (BaseinfoLocation) locationDetailService.getIBaseinfoLocaltionModelById(locationId);
        return subLocation;
    }

    public List<BaseinfoLocation> getLocationDetailList(Map<String, Object> params) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        //如果存在码头出入的参数dockApplication,选用对付码头的方法
        if (params.get("dockApplication") != null) {
            List<BaseinfoLocation> baseinfoLocationList = locationDetailService.getDockListByType(params);
            return baseinfoLocationList;
        }
        List<BaseinfoLocation> baseinfoLocationList = locationDetailService.getIBaseinfoLocaltionModelListByType(params);
        return baseinfoLocationList;
    }

    public IBaseinfoLocaltionModel insertLocationDetailByType(IBaseinfoLocaltionModel localtionModel) throws BizCheckedException {
        locationDetailService.insert(localtionModel);
        return localtionModel;
    }

    public IBaseinfoLocaltionModel updateLocationDetailByType(IBaseinfoLocaltionModel localtionModel) throws BizCheckedException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        locationDetailService.update(localtionModel);
        return localtionModel;
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
