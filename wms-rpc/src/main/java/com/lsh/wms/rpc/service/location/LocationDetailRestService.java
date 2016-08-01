package com.lsh.wms.rpc.service.location;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.wms.api.model.location.LocationDetailRequest;
import com.lsh.wms.api.service.location.ILocationDetailRestService;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.core.service.location.LocationConstant;
import com.lsh.wms.core.service.location.LocationDetailModelFactory;
import com.lsh.wms.core.service.location.LocationDetailService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.model.baseinfo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.lang.reflect.InvocationTargetException;
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
    private LocationDetailModelFactory locationDetailModelFactory;


    //构造之后,实例化之前,注入各种model

    /**
     * 将所有的bin的type注册到工厂中
     */
    @PostConstruct
    public void postConstruct() {
        //仓库
        locationDetailModelFactory.register(LocationConstant.Warehouse, new BaseinfoLocationWarehouse());
        //区域
        locationDetailModelFactory.register(LocationConstant.Region_area, new BaseinfoLocationRegion());
        //注入过道
        locationDetailModelFactory.register(LocationConstant.Passage, new BaseinfoLocationPassage());

        //注入阁楼区和货架区
        locationDetailModelFactory.register(LocationConstant.Shelfs, new BaseinfoLocationRegion());
        locationDetailModelFactory.register(LocationConstant.Lofts, new BaseinfoLocationRegion());
        //注入区域
        locationDetailModelFactory.register(LocationConstant.InventoryLost, new BaseinfoLocationRegion());
        locationDetailModelFactory.register(LocationConstant.Floor, new BaseinfoLocationRegion());
        locationDetailModelFactory.register(LocationConstant.Temporary, new BaseinfoLocationRegion());
        locationDetailModelFactory.register(LocationConstant.Collection_area, new BaseinfoLocationRegion());
        locationDetailModelFactory.register(LocationConstant.Back_area, new BaseinfoLocationRegion());
        locationDetailModelFactory.register(LocationConstant.Defective_area, new BaseinfoLocationRegion());
        //货架和阁楼
        locationDetailModelFactory.register(LocationConstant.Shelf, new BaseinfoLocationShelf());
        locationDetailModelFactory.register(LocationConstant.Loft, new BaseinfoLocationShelf());
        //注入码头
        locationDetailModelFactory.register(LocationConstant.Dock_area, new BaseinfoLocationDock());
        //货位
        locationDetailModelFactory.register(LocationConstant.Bin, new BaseinfoLocationBin());
        //货架和阁楼的货位
        locationDetailModelFactory.register(LocationConstant.Shelf_collection_bin, new BaseinfoLocationBin());
        locationDetailModelFactory.register(LocationConstant.Shelf_store_bin, new BaseinfoLocationBin());
        locationDetailModelFactory.register(LocationConstant.Loft_collection_bin, new BaseinfoLocationBin());
        locationDetailModelFactory.register(LocationConstant.Loft_store_bin, new BaseinfoLocationBin());
        //功能bin
        locationDetailModelFactory.register(LocationConstant.Floor_bin, new BaseinfoLocationBin());
        locationDetailModelFactory.register(LocationConstant.Temporary_bin, new BaseinfoLocationBin());
        locationDetailModelFactory.register(LocationConstant.Collection_bin, new BaseinfoLocationBin());
        locationDetailModelFactory.register(LocationConstant.Back_bin, new BaseinfoLocationBin());
        locationDetailModelFactory.register(LocationConstant.Defective_bin, new BaseinfoLocationBin());
    }


    /**
     * 根据id查找细节表
     * 先查找主表
     * 在查找细节表
     *
     * @param locationId 地址id
     * @param type       地址类型
     * @return 位置对象
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    @POST
    @Path("getLocationDetail")
    public String getLocationDetailByIdAndType(Long locationId, Integer type) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        BaseinfoLocation baseinfoLocation = locationDetailService.getIBaseinfoLocaltionModelByIdAndType(locationId, type);
        return JsonUtils.SUCCESS(baseinfoLocation);
    }

    @POST
    @Path("getlocationDetailList")
    public String getLocationDetailListByType(Map<String, Object> params) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        List<BaseinfoLocation> baseinfoLocationList = locationDetailService.getIBaseinfoLocaltionModelListByType(params);
        return JsonUtils.SUCCESS(baseinfoLocationList);
    }

    @POST
    @Path("insertLocation")
    public String insertLocationDetailByType(LocationDetailRequest request) throws BizCheckedException {
        //根据type类型,将父类转为子类
        IBaseinfoLocaltionModel iBaseinfoLocaltionModel = locationDetailModelFactory.getLocationModel(Long.valueOf(request.getType().toString()));
        ObjUtils.bean2bean(request, iBaseinfoLocaltionModel);
        locationDetailService.insert((BaseinfoLocation) iBaseinfoLocaltionModel);
        return JsonUtils.SUCCESS();
    }


    @POST
    @Path("updateLocation")
    public String updateLocationDetailByType(LocationDetailRequest request) throws BizCheckedException {
        IBaseinfoLocaltionModel iBaseinfoLocaltionModel = locationDetailModelFactory.getLocationModel(Long.valueOf(request.getType().toString()));
        ObjUtils.bean2bean(request, iBaseinfoLocaltionModel);
        locationDetailService.update((BaseinfoLocation) iBaseinfoLocaltionModel);
        return JsonUtils.SUCCESS();
    }


    @POST
    @Path("countLocation")
    public String countLocationDetailByType() {
        Map<String, Object> params = RequestUtils.getRequest();
        return JsonUtils.SUCCESS(locationDetailService.countLocationDetail(params));
    }


    @Autowired
    private LocationService locationService;

    @POST
    @Path("getList")
    public String searchList() throws BizCheckedException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Map<String, Object> params = RequestUtils.getRequest();
        List<BaseinfoLocation> list = locationService.getSubLocationList(13L,17L);
//        return JsonUtils.SUCCESS(locationDetailService.getIBaseinfoLocaltionModelListByType(params));
        return JsonUtils.SUCCESS(list);
    }
}
