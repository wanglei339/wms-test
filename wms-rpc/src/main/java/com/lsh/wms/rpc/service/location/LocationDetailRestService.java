package com.lsh.wms.rpc.service.location;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.api.model.location.LocationDetailRequest;
import com.lsh.wms.api.model.location.LocationDetailResponse;
import com.lsh.wms.api.service.location.ILocationDetailRestService;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.core.service.location.LocationConstant;
import com.lsh.wms.core.service.location.LocationDetailModelFactory;
import com.lsh.wms.core.service.location.LocationDetailService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.model.baseinfo.*;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

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
    private LocationDetailService locationDetailService;
    @Autowired
    private LocationDetailModelFactory locationDetailModelFactory;
    //设置bin的Type集合,用于判断type是否是bin,然后设置


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
    public String getLocationDetailByIdAndType(Long locationId, Long type) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
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
        BaseinfoLocation baseinfoLocation = new BaseinfoLocation();
        //转成子类

        ObjUtils.bean2bean(request, iBaseinfoLocaltionModel);
        //转成父类
        ObjUtils.bean2bean(request, baseinfoLocation);
        //设置id
        Long locationId = RandomUtils.genId();
        iBaseinfoLocaltionModel.setLocationId(locationId);
        baseinfoLocation.setLocationId(locationId);
        //生成时间
        Long createAt = DateUtils.getCurrentSeconds();
        iBaseinfoLocaltionModel.setCreatedAt(createAt);
        iBaseinfoLocaltionModel.setUpdatedAt(createAt);
        baseinfoLocation.setCreatedAt(createAt);
        baseinfoLocation.setUpdatedAt(createAt);
        //所在哪个区?
        IBaseinfoLocaltionModel location = locationDetailModelFactory.getLocationModel(request.getType());
        locationDetailService.insert((BaseinfoLocation) iBaseinfoLocaltionModel);
        locationService.insertLocation(baseinfoLocation);
        return JsonUtils.SUCCESS();
    }


    @POST
    @Path("updateLocation")
    public String updateLocationDetailByType(LocationDetailRequest request) throws BizCheckedException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Long locationId = request.getLocationId();
        //先查找,先主表
        BaseinfoLocation location = locationService.getLocation(locationId);
        if (null == location) {
            throw new BizCheckedException("位置不存在");
        }
        IBaseinfoLocaltionModel iBaseinfoLocaltionModel = locationDetailService.getIBaseinfoLocaltionModelByIdAndType(locationId, location.getType());
        //转成父类
        ObjUtils.bean2bean(request, location);
        //转成子类
        ObjUtils.bean2bean(request, iBaseinfoLocaltionModel);
        //
        //插入
//        ObjUtils.bean2bean(request, iBaseinfoLocaltionModel);
//        locationDetailService.update((BaseinfoLocation) iBaseinfoLocaltionModel);
        //添加更新时间
        long updatedAt = DateUtils.getCurrentSeconds();
        location.setUpdatedAt(updatedAt);
        iBaseinfoLocaltionModel.setUpdatedAt(updatedAt);

        locationService.updateLocation(location);
        locationDetailService.update(iBaseinfoLocaltionModel);
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
        if (LocationConstant.Bin == Long.parseLong(params.get("type").toString())) {
            //定义bin集合
            List<Long> binTypes = Arrays.asList(LocationConstant.Shelf_store_bin, LocationConstant.Shelf_collection_bin, LocationConstant.Loft_collection_bin, LocationConstant.Loft_store_bin, LocationConstant.Floor_bin, LocationConstant.Temporary_bin, LocationConstant.Collection_bin, LocationConstant.Back_bin, LocationConstant.Defective_bin);
            List<IBaseinfoLocaltionModel> targetList = new ArrayList<IBaseinfoLocaltionModel>();
            //追加子集
            traverseList(binTypes, targetList);


            ///
            List<LocationDetailResponse> responses = new ArrayList<LocationDetailResponse>();
            //设置返回页面的字段,特殊处理,码头,通道和库位的温区
            for (IBaseinfoLocaltionModel iBaseinfoLocaltionModel : targetList) {
                LocationDetailResponse locationDetailResponse = new LocationDetailResponse();
                ObjUtils.bean2bean(iBaseinfoLocaltionModel, locationDetailResponse);
                //设置库位信息
                this.setBinParameter(locationDetailResponse);
                responses.add(locationDetailResponse);
            }
            return JsonUtils.SUCCESS(responses);

//            return JsonUtils.SUCCESS(targetList);

        } else if (LocationConstant.Region_area == Integer.parseInt(params.get("type").toString())) {
            List<Long> regionTypes = Arrays.asList(LocationConstant.Shelfs, LocationConstant.Lofts, LocationConstant.Floor, LocationConstant.Temporary, LocationConstant.Collection_area, LocationConstant.Back_area, LocationConstant.Defective_area, LocationConstant.Dock_area);
            List<IBaseinfoLocaltionModel> targetList = new ArrayList<IBaseinfoLocaltionModel>();
            //追加子集
            traverseList(regionTypes, targetList);


            //将父亲的结果拷贝给子类
            //设置是否能用


//            ////////////////////////////////
            //前端响应
            List<LocationDetailResponse> responses = new ArrayList<LocationDetailResponse>();
            //设置返回页面的字段,特殊处理,码头,通道和库位的温区
            for (IBaseinfoLocaltionModel iBaseinfoLocaltionModel : targetList) {
                LocationDetailResponse locationDetailResponse = new LocationDetailResponse();
                ObjUtils.bean2bean(iBaseinfoLocaltionModel, locationDetailResponse);
                //码头设置
                Long type = locationDetailResponse.getType();
                Integer direction = locationDetailResponse.getDirection();
                //码头
                this.setDockParameter(locationDetailResponse);

                responses.add(locationDetailResponse);
            }
            //
            return JsonUtils.SUCCESS(responses);

            //////////////////
//            return JsonUtils.SUCCESS(targetList);
        } else {
            //码头,通道,库位
            List<BaseinfoLocation> localtions = locationDetailService.getIBaseinfoLocaltionModelListByType(params);
            List<LocationDetailResponse> responses = new ArrayList<LocationDetailResponse>();
            for (BaseinfoLocation baseinfoLocation : localtions) {
                LocationDetailResponse locationDetailResponse = new LocationDetailResponse();
                ObjUtils.bean2bean(baseinfoLocation, locationDetailResponse);
                //设置库位信息
                this.setBinParameter(locationDetailResponse);
                //码头
                this.setDockParameter(locationDetailResponse);
                //设置通道
                this.setPassageParameter(locationDetailResponse);
                responses.add(locationDetailResponse);
            }
            return JsonUtils.SUCCESS(responses);

//            return JsonUtils.SUCCESS();
        }

    }

    /**
     * 遍历LocationList的集合,根据将type的固定代号 bin type=15 和 region_area= 2 拆分type集合
     * 然后根据指定的type,返回各自type查找集合
     *
     * @param binTypes
     * @param targetList 需要追加的目标集合
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    private List<IBaseinfoLocaltionModel> traverseList(List<Long> binTypes, List<IBaseinfoLocaltionModel> targetList) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {

        for (Long type : binTypes) {
            Map<String, Object> mapQuery = new HashMap<String, Object>();
            mapQuery.put("type", type);
            List<BaseinfoLocation> subLocationList = locationDetailService.getIBaseinfoLocaltionModelListByType(mapQuery);
            targetList.addAll(subLocationList);
        }
        return targetList;
    }

    /**
     * 设置通道页面显示参数
     *
     * @param locationDetailResponse
     * @return
     */
    private LocationDetailResponse setPassageParameter(LocationDetailResponse locationDetailResponse) {
        if (LocationConstant.Passage == locationDetailResponse.getType()) {
            if (LocationConstant.PassageEastWest == locationDetailResponse.getDirection()) {
                locationDetailResponse.setPassageDirection("东西");
            }
            if (LocationConstant.PassageNorthSouth == locationDetailResponse.getDirection()) {
                locationDetailResponse.setPassageDirection("南北");
            }
        }
        return locationDetailResponse;
    }

    /**
     * 设置码头页面显示参数
     *
     * @param locationDetailResponse
     * @return
     */
    private LocationDetailResponse setDockParameter(LocationDetailResponse locationDetailResponse) {
        if (LocationConstant.Dock_area == locationDetailResponse.getType()) {
            //设置方位
            if (LocationConstant.DockEast == locationDetailResponse.getDirection()) {
                locationDetailResponse.setDockDirection("东");
            }
            if (LocationConstant.DockSouth == locationDetailResponse.getDirection()) {
                locationDetailResponse.setDockDirection("南");
            }
            if (LocationConstant.DockWest == locationDetailResponse.getDirection()) {
                locationDetailResponse.setDockDirection("西");
            }
            if (LocationConstant.DockNorth == locationDetailResponse.getDirection()) {
                locationDetailResponse.setDockDirection("北");
            }
            //设置地秤的有无
            if (locationDetailResponse.getHaveScales() > 0) {
                locationDetailResponse.setSureHaveScale("有");
            } else {
                locationDetailResponse.setSureHaveScale("无");
            }
            //设置出库入库
            if (LocationConstant.DockIn == locationDetailResponse.getDockApplication()) {
                locationDetailResponse.setApplicationName("入库");
            } else {
                locationDetailResponse.setApplicationName("出库");
            }
        }
        return locationDetailResponse;
    }

    /**
     * 库位的页面显示设置
     *
     * @param locationDetailResponse
     * @return
     */
    private LocationDetailResponse setBinParameter(LocationDetailResponse locationDetailResponse) {
        //设置库位的温区
        if (locationDetailResponse.getZoneType() != null) {
            if (LocationConstant.RoomTemperature == locationDetailResponse.getZoneType()) {
                locationDetailResponse.setZoneName("常温库");
            }
            if (LocationConstant.LowTemperature == locationDetailResponse.getZoneType()) {
                locationDetailResponse.setZoneName("低温库");
            }
        }
        return locationDetailResponse;
    }


}
