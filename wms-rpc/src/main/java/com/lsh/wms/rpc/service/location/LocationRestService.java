package com.lsh.wms.rpc.service.location;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.BeanMapTransUtils;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.wms.api.service.location.ILocationRestService;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

/**
 * Created by fengkun on 16/7/11.
 */

@Service(protocol = "rest")
@Path("location")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class LocationRestService implements ILocationRestService {
    private static Logger logger = LoggerFactory.getLogger(LocationRestService.class);

    @Autowired
    private LocationRpcService locationRpcService;
    @Autowired
    private LocationService locationService;

    @GET
    @Path("getLocation")
    public String getLocation(@QueryParam("locationId") Long locationId) {
        BaseinfoLocation locationInfo = locationRpcService.getLocation(locationId);
        return JsonUtils.SUCCESS(locationInfo);
    }

    @GET
    @Path("getStoreLocationIds")
    public String getStoreLocationIds(@QueryParam("locationId") Long locationId) {
        List<BaseinfoLocation> locations = locationRpcService.getStoreLocations(locationId);
        List<Long> locationIds = locationService.getLocationIds(locations);
        return JsonUtils.SUCCESS(locationIds);
    }

    @GET
    @Path("getFatherByType")
    public String getFatherByType(@QueryParam("locationId") Long locationId, @QueryParam("type") String type) {
        BaseinfoLocation location = locationRpcService.getFatherByType(locationId, type);
        return JsonUtils.SUCCESS(location);
    }

    @GET
    @Path("getFatherArea")
    public String getFatherArea(@QueryParam("locationId") Long locationId) {
        BaseinfoLocation location = locationRpcService.getFatherByType(locationId, "area");
        return JsonUtils.SUCCESS(location);
    }

    @GET
    @Path("getWarehouseLocationId")
    public String getWarehouseLocationId() {
        Long locationId = locationService.getWarehouseLocationId();
        return JsonUtils.SUCCESS(locationId);
    }

    @GET
    @Path("getinventoryLostLocationId")
    public String getInventoryLostLocationId() {
        Long locationId = locationService.getInventoryLostLocationId();
        return JsonUtils.SUCCESS(locationId);
    }


    //insert与detail相关,需要同时插入detail的信息
    @POST
    @Path("insertLocation")
    public String insertLocation() {
        Map<String,Object> param = RequestUtils.getRequest();
        BaseinfoLocation location = BeanMapTransUtils.map2Bean(param,BaseinfoLocation.class);
        location.setLocationId((long) 20);

        location.setCanStore(1);
        location.setDescription("1");
        location.setBinPositionNo((long) 1);
        location.setFatherId((long) 1);
        location.setIsLeaf(1);
        location.setContainerVol((long) 5);
        location.setInUse(1);
        location.setLocationCode("2313");
        location.setDescription("13233");
//        location.setUpdatedAt(new Data);
        locationService.insertLocation(location);

        return JsonUtils.SUCCESS(locationRpcService.insertLocation(location));
    }

    //update与detail相关,需要跟新detail的信息
    @POST
    @Path("updateLocation")
    public String updateLocation(BaseinfoLocation location) {
        return JsonUtils.SUCCESS(locationRpcService.updateLocation(location));
    }

    @Path("getLocationList")
    public String searchList(Map<String, Object> params) {
        List<BaseinfoLocation> baseinfoLocationList = locationService.getBaseinfoLocationList(params);
        return JsonUtils.SUCCESS(baseinfoLocationList);
    }

    @POST
    @Path("countLocation")
    public String countBaseinfoLocation(Map<String, Object> params) {
        return JsonUtils.SUCCESS(locationService.countLocation(params));
    }


}
