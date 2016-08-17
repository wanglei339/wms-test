package com.lsh.wms.rpc.service.location;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.wms.api.model.location.LocationDetailRequest;
import com.lsh.wms.api.model.location.LocationDetailResponse;
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
    //设置bin的Type集合,用于判断type是否是bin,然后设置


    //构造之后,实例化之前,注入各种model

    /**
     * 将所有的bin的type注册到工厂中
     */
    @PostConstruct
    public void postConstruct() {
        //仓库
        locationDetailModelFactory.register(LocationConstant.WAREHOUSE, new BaseinfoLocationWarehouse());
        //区域
        locationDetailModelFactory.register(LocationConstant.REGION_AREA, new BaseinfoLocationRegion());
        //注入过道
        locationDetailModelFactory.register(LocationConstant.PASSAGE, new BaseinfoLocationPassage());

        //注入阁楼区和货架区
        locationDetailModelFactory.register(LocationConstant.SHELFS, new BaseinfoLocationRegion());
        locationDetailModelFactory.register(LocationConstant.LOFTS, new BaseinfoLocationRegion());
        //注入区域
        locationDetailModelFactory.register(LocationConstant.INVENTORYLOST, new BaseinfoLocationRegion());
        locationDetailModelFactory.register(LocationConstant.FLOOR, new BaseinfoLocationRegion());
        locationDetailModelFactory.register(LocationConstant.TEMPORARY, new BaseinfoLocationRegion());
        locationDetailModelFactory.register(LocationConstant.COLLECTION_AREA, new BaseinfoLocationRegion());
        locationDetailModelFactory.register(LocationConstant.BACK_AREA, new BaseinfoLocationRegion());
        locationDetailModelFactory.register(LocationConstant.DEFECTIVE_AREA, new BaseinfoLocationRegion());
        //货架和阁楼
        locationDetailModelFactory.register(LocationConstant.SHELF, new BaseinfoLocationShelf());
        locationDetailModelFactory.register(LocationConstant.LOFT, new BaseinfoLocationShelf());
        //注入码头
        locationDetailModelFactory.register(LocationConstant.DOCK_AREA, new BaseinfoLocationDock());
        //货位
        locationDetailModelFactory.register(LocationConstant.BIN, new BaseinfoLocationBin());
        //货架和阁楼的货位
        locationDetailModelFactory.register(LocationConstant.SHELF_PICKING_BIN, new BaseinfoLocationBin());
        locationDetailModelFactory.register(LocationConstant.SHELF_STORE_BIN, new BaseinfoLocationBin());
        locationDetailModelFactory.register(LocationConstant.LOFT_PICKING_BIN, new BaseinfoLocationBin());
        locationDetailModelFactory.register(LocationConstant.LOFT_STORE_BIN, new BaseinfoLocationBin());
        //功能bin
        locationDetailModelFactory.register(LocationConstant.FLOOR_BIN, new BaseinfoLocationBin());
        locationDetailModelFactory.register(LocationConstant.TEMPORARY_BIN, new BaseinfoLocationBin());
        locationDetailModelFactory.register(LocationConstant.COLLECTION_BIN, new BaseinfoLocationBin());
        locationDetailModelFactory.register(LocationConstant.BACK_BIN, new BaseinfoLocationBin());
        locationDetailModelFactory.register(LocationConstant.DEFECTIVE_BIN, new BaseinfoLocationBin());
    }


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
    public String getLocationDetailById(@QueryParam("locationId") Long locationId)  {
        Long id = Long.parseLong(locationId.toString());
        //前端回显示用的fatherLocation的显示
        IBaseinfoLocaltionModel localtionModel = locationDetailRpcService.getLocationDetailById(id);
        BaseinfoLocation fatherLocation = locationRpcService.getFatherLocation(id);
        LocationDetailResponse detailResponse = new LocationDetailResponse();
        ObjUtils.bean2bean(localtionModel,detailResponse);
        //设置fathe的回显示
        detailResponse.setFatherLocation(fatherLocation);
        return JsonUtils.SUCCESS(detailResponse);
    }

    @POST
    @Path("insertLocation")
    public String insertLocationDetailByType(LocationDetailRequest request) throws BizCheckedException {
        //根据type类型,将父类转为子类
        IBaseinfoLocaltionModel iBaseinfoLocaltionModel = locationDetailModelFactory.getLocationModel(Long.valueOf(request.getType().toString()));
        //转成子类
        ObjUtils.bean2bean(request, iBaseinfoLocaltionModel);
        //插入是否成功
        boolean isTrue = locationDetailRpcService.insertLocationDetailByType((BaseinfoLocation) iBaseinfoLocaltionModel);
        if (isTrue){
            return JsonUtils.SUCCESS("插入成功");
        }else {
            //原位置已经存在
            return JsonUtils.EXCEPTION_ERROR("insertError");
        }
    }


    @POST
    @Path("updateLocation")
    public String updateLocationDetailByType(LocationDetailRequest request) throws BizCheckedException {
        Long locationId = Long.parseLong(request.getLocationId().toString());
        IBaseinfoLocaltionModel iBaseinfoLocaltionModel = locationDetailRpcService.getLocationDetailById(locationId);
        ObjUtils.bean2bean(request, iBaseinfoLocaltionModel);

        //添加更新时间
        long updatedAt = DateUtils.getCurrentSeconds();
        iBaseinfoLocaltionModel.setUpdatedAt(updatedAt);
        boolean isTrue = locationDetailRpcService.updateLocationDetailByType((BaseinfoLocation) iBaseinfoLocaltionModel);
        if (isTrue){
            return JsonUtils.SUCCESS("更新成功");
        } else {
            return JsonUtils.EXCEPTION_ERROR("updateError");
        }
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
        if (locations==null){
            throw new BizCheckedException("2180001");   // 位置不存在
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
     * 按照指定的获取list的方法
     * @return 全大区、全功能区、全货架阁楼、全货架阁楼区、全通道
     * @throws BizCheckedException
     */
    @GET
    @Path("getTargetListByListType")
    public String getTargetListByListType(@QueryParam("listType") Integer listType) throws BizCheckedException {
        return JsonUtils.SUCCESS(locationDetailRpcService.getTargetListByListType(listType));
    }

    /**
     * 获取下一层的所有节点
     * @param locationId
     * @return
     */
    @GET
    @Path("getNextLevelLocations")
    public String getNextLevelLocations(@QueryParam("locationId") Long locationId) {
        return JsonUtils.SUCCESS(locationDetailRpcService.getNextLevelLocations(locationId));
    }
}
