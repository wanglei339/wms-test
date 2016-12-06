package com.lsh.wms.rpc.service.location;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.wms.api.model.location.LocationDetailRequest;
import com.lsh.wms.api.model.location.LocationDetailResponse;
import com.lsh.wms.api.model.location.LocationDetailUpdateRequest;
import com.lsh.wms.api.service.location.ILocationDetailRestService;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.core.constant.LocationConstant;
import com.lsh.wms.core.service.location.LocationDetailModelFactory;
import com.lsh.wms.model.baseinfo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

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
    private LocationDetailModelFactory locationDetailModelFactory;
    @Autowired
    private LocationDetailRpcService locationDetailRpcService;
    @Autowired
    private LocationRpcService locationRpcService;



    /**
     * 根据id查找细节表
     * 先查找主表
     * 在查找细节表
     *
     * @param locationId 地址id
     * @return 位置对象
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    @GET
    @Path("getLocationDetail")
    public String getLocationDetailById(@QueryParam("locationId") Long locationId) throws BizCheckedException {
        //前端回显示用的fatherLocation的显示
        IBaseinfoLocaltionModel localtionModel = locationDetailRpcService.getLocationDetailById(locationId);
        BaseinfoLocation fatherLocation = locationRpcService.getFatherLocation(locationId);
        LocationDetailResponse detailResponse = new LocationDetailResponse();
        ObjUtils.bean2bean(localtionModel, detailResponse);
        //设置fathe的回显示
        detailResponse.setFatherLocation(fatherLocation);
        return JsonUtils.SUCCESS(detailResponse);
    }

    @POST
    @Path("insertLocation")
    public String insertLocationDetailByType(LocationDetailRequest request) throws BizCheckedException {
        //根据type类型,将父类转为子类
//        IBaseinfoLocaltionModel iBaseinfoLocaltionModel = locationDetailModelFactory.getLocationModel(Long.valueOf(request.getType().toString()));
        //转成子类
//        ObjUtils.bean2bean(request, iBaseinfoLocaltionModel);
        //插入是否成功
        if (null == request) {
            throw new BizCheckedException("2180021");
        }
        locationDetailRpcService.insertLocationDetailByType(request);
        return JsonUtils.SUCCESS();

    }


    @POST
    @Path("updateLocation")
    public String updateLocationDetailByType(LocationDetailUpdateRequest locationDetailUpdateRequest) throws BizCheckedException {
//        Map<String, Object> beanMap = RequestUtils.getRequest();
//        Long locationId = Long.parseLong(request.getLocationId().toString());
        IBaseinfoLocaltionModel iBaseinfoLocaltionModel = locationDetailRpcService.getLocationDetailById(locationDetailUpdateRequest.getLocationId());
        if (null==iBaseinfoLocaltionModel){
            throw new BizCheckedException("2180001");
        }
        Long type =iBaseinfoLocaltionModel.getType();
        ObjUtils.bean2bean(locationDetailUpdateRequest, iBaseinfoLocaltionModel);
        iBaseinfoLocaltionModel.setType(type);
        //添加更新时间
        long updatedAt = DateUtils.getCurrentSeconds();
        iBaseinfoLocaltionModel.setUpdatedAt(updatedAt);
        return JsonUtils.SUCCESS(locationDetailRpcService.updateLocationDetailByType(iBaseinfoLocaltionModel));

    }


    @POST
    @Path("countLocation")
    public String countLocationDetailByType() {
        Map<String, Object> mapQuery = RequestUtils.getRequest();
        return JsonUtils.SUCCESS(locationDetailRpcService.countLocationDetailByType(mapQuery));
    }

    @POST
    @Path("getList")
    public String searchList() throws BizCheckedException {
        Map<String, Object> params = RequestUtils.getRequest();
        List<BaseinfoLocation> locations = locationDetailRpcService.getLocationDetailList(params);
        if (locations == null) {
            locations = new ArrayList<BaseinfoLocation>();
        }
        return JsonUtils.SUCCESS(locations);
    }

    /**
     * location的删除操作
     *
     * @return
     * @throws BizCheckedException
     */
    @POST
    @Path("removeLocation")
    public String removeLocation() throws BizCheckedException {
        //先找到location,然后将location的is——valid置为1
        Map<String, Object> params = RequestUtils.getRequest();
        Long locationId = Long.parseLong(params.get("locationId").toString());
        if (locationDetailRpcService.removeLocation(locationId)) {
            return JsonUtils.SUCCESS("删除成功");
        } else {
            return JsonUtils.EXCEPTION_ERROR("位置不存在");  //  位置不存在
        }
    }

    /**
     * 获取指定type的location列表
     * @return 同一类型的位置集合
     * @throws BizCheckedException
     */
    @GET
    @Path("getTargetListByListType")
    public String getTargetListByListType(@QueryParam("listType") Integer listType) throws BizCheckedException {
        return JsonUtils.SUCCESS(locationDetailRpcService.getTargetListByListType(listType));
    }

    /**
     * 获取下一层的所有节点
     *
     * @param locationId
     * @return
     */
    @GET
    @Path("getNextLevelLocations")
    public String getNextLevelLocations(@QueryParam("locationId") Long locationId) throws BizCheckedException {
        return JsonUtils.SUCCESS(locationDetailRpcService.getNextLevelLocations(locationId));
    }

    @POST
    @Path("insertRegion")
    public String insertRegion(LocationDetailRequest request) throws BizCheckedException {
        locationDetailRpcService.insertRegion(request);
        return JsonUtils.SUCCESS();
    }

    @POST
    @Path("insertPassage")
    public String insertPassage(LocationDetailRequest request) throws BizCheckedException{
        locationDetailRpcService.insertPassage(request);
        return JsonUtils.SUCCESS();
    }

}
