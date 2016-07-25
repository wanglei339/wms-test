package com.lsh.wms.rpc.service.location;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.service.location.ILocationDetailRestService;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.core.service.location.LocationDetailService;
import com.lsh.wms.model.baseinfo.IBaseinfoLocaltionModel;
import com.lsh.wms.model.baseinfo.LocationModelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Map;

/**
 * rest服务,对外提供
 * 1.detail的list服务
 * 2.增加服务
 * 3.更新
 * 4.查找
 *
 * @Author 马启迪 maqidi@lsh123.com
 * @Date 16/7/24 下午4:18
 */
@Service(protocol = "rest")
@Path("locationDetail")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class LocationDetailRestService implements ILocationDetailRestService {
    private static Logger logger = LoggerFactory.getLogger(LocationRestService.class);

    @Autowired
    private LocationDetailService locationDetailService;

    @GET
    @Path("getLocationDetail")
    public String getLocationDetailByIdAndType(@QueryParam("locationId") Long locationId,@QueryParam("type") Integer type) {
        IBaseinfoLocaltionModel iBaseinfoLocaltionModel;
        iBaseinfoLocaltionModel = locationDetailService.getIBaseinfoLocaltionModelByIdAndType(locationId, type);
        return JsonUtils.SUCCESS(iBaseinfoLocaltionModel);

    }

    @GET
    @Path("getlocationDetailList")
    public String getLocationDetailListByType(@QueryParam("type") Integer type) {
        Map<String, Object> params = RequestUtils.getRequest();
        return JsonUtils.SUCCESS(locationDetailService.getIBaseinfoLocaltionModelListByType(params, type));

    }

    //TODO 此处要判断type的类型才能实例化不同的detail 和location一起 update
    @POST
    @Path("insertLocation")
    public String insertLocationDetailByType(IBaseinfoLocaltionModel baseinfoLocaltionModel, Integer type) throws ClassNotFoundException {
        LocationModelFactory locationModelFactory = new LocationModelFactory();
        IBaseinfoLocaltionModel iBaseinfoLocaltionModel = locationModelFactory.creatLocationModelByType(type);
        String modelName = locationModelFactory.getLocationClassByType(type);
        locationDetailService.insert(iBaseinfoLocaltionModel);
        return JsonUtils.SUCCESS();
    }

    //TODO 此处需要和location一起update
    @POST
    @Path("updateLocation")
    public String updateLocationDetailByType(IBaseinfoLocaltionModel baseinfoLocaltionModel, Integer type) {
        LocationModelFactory locationModelFactory = new LocationModelFactory();
        IBaseinfoLocaltionModel iBaseinfoLocaltionModel = locationModelFactory.creatLocationModelByType(type);
        locationDetailService.update(baseinfoLocaltionModel);
        return null;
    }


}
