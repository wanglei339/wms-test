package com.lsh.wms.rpc.service.location;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.service.location.ILocationDetailRestService;
import com.lsh.wms.core.service.location.LocationDetailService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.model.baseinfo.IBaseinfoLocaltionModel;
import com.lsh.wms.model.baseinfo.LocationModelFactory;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
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
    @Autowired
    private LocationService locationService;

    @GET
    @Path("getLocationDetail")
    public String getLocationDetailByIdAndType(@QueryParam("locationId") Long locationId, @QueryParam("type") Integer type) {
        IBaseinfoLocaltionModel iBaseinfoLocaltionModel;
        iBaseinfoLocaltionModel = locationDetailService.getIBaseinfoLocaltionModelByIdAndType(locationId, type);
        //设置可用
        if (!locationService.isUsed(locationId)) {
            iBaseinfoLocaltionModel.setIsUsed("可用");
        } else {
            iBaseinfoLocaltionModel.setIsUsed("未占用");
        }
        return JsonUtils.SUCCESS(iBaseinfoLocaltionModel);
    }

    @GET
    @Path("getlocationDetailList")
    public String getLocationDetailListByType(@QueryParam("type") Integer type) {
        Map<String,Object> params = new HashedMap();
        params.put("type", type);
        List<IBaseinfoLocaltionModel> list = locationDetailService.getIBaseinfoLocaltionModelListByType(params);
        for (IBaseinfoLocaltionModel iBaseinfoLocaltionModel : list) {
            Long locationId = iBaseinfoLocaltionModel.getLocationId();
            //设置编码
            String code = locationService.getCodeById(locationId);
            iBaseinfoLocaltionModel.setCode(code);
            //设置可用
            if (!locationService.isUsed(locationId)) {
                iBaseinfoLocaltionModel.setIsUsed("可用");
            } else {
                iBaseinfoLocaltionModel.setIsUsed("未占用");
            }
        }
        return JsonUtils.SUCCESS(list);
    }


    @POST
    @Path("countLocation")
    public String countLocationDetailByType(Map<String, Object> params) {

        Integer type = (Integer) params.get("type");
        int count = locationDetailService.countLocationDetail(params);
        return JsonUtils.SUCCESS(count);
    }

    @POST
    @Path("getList")
    public String searchList(Map<String, Object> params) {
        List<IBaseinfoLocaltionModel> iBaseinfoLocaltionModelsList = locationDetailService.getIBaseinfoLocaltionModelListByType(params);
        //设置可用和数量
        return JsonUtils.SUCCESS(iBaseinfoLocaltionModelsList);
    }

    //TODO 此处要判断type的类型才能实例化不同的detail 和location一起 update
    @POST
    @Path("insertLocation")
    public String insertLocationDetailByType(IBaseinfoLocaltionModel baseinfoLocaltionModel, @QueryParam("type") Integer type) throws ClassNotFoundException {
        LocationModelFactory locationModelFactory = new LocationModelFactory();
        IBaseinfoLocaltionModel iBaseinfoLocaltionModel = locationModelFactory.creatLocationModelByType(type);
        String modelName = locationModelFactory.getLocationClassByType(type);
        locationDetailService.insert(iBaseinfoLocaltionModel);
        return JsonUtils.SUCCESS();
    }

    //TODO 此处需要和location一起update
    @POST
    @Path("updateLocation")
    public String updateLocationDetailByType(IBaseinfoLocaltionModel baseinfoLocaltionModel, @QueryParam("type") Integer type) {
        LocationModelFactory locationModelFactory = new LocationModelFactory();
        IBaseinfoLocaltionModel iBaseinfoLocaltionModel = locationModelFactory.creatLocationModelByType(type);
        locationDetailService.update(baseinfoLocaltionModel);
        return null;
    }


}
