package com.lsh.wms.rpc.service.location;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.wms.api.model.location.LocationDetailRequest;
import com.lsh.wms.api.service.item.IItemRpcService;
import com.lsh.wms.api.service.location.ILocationRestService;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.core.constant.LocationConstant;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.wave.WaveService;
import com.lsh.wms.model.baseinfo.BaseinfoItemLocation;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.rpc.service.pick.PickRpcService;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.*;

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
    @Autowired
    private LocationDetailRpcService locationDetailRpcService;
    @Autowired
    private PickRpcService pickRpcService;
    @Autowired
    private WaveService waveService;
    @Reference
    private IItemRpcService itemLocationService;

    @GET
    @Path("getLocation")
    public String getLocation(@QueryParam("locationId") Long locationId) throws BizCheckedException {
        return JsonUtils.SUCCESS(locationRpcService.getLocation(locationId));
    }

    @GET
    @Path("getStoreLocationIds")
    public String getStoreLocationIds(@QueryParam("locationId") Long locationId) throws BizCheckedException {
        List<BaseinfoLocation> locations = locationRpcService.getStoreLocations(locationId);
        List<Long> locationIds = locationService.getLocationIds(locations);
        return JsonUtils.SUCCESS(locationIds);
    }

    @GET
    @Path("getFatherByType")
    public String getFatherByType(@QueryParam("locationId") Long locationId, @QueryParam("type") Long type) throws BizCheckedException {
        BaseinfoLocation location = locationRpcService.getFatherByType(locationId, type);
        return JsonUtils.SUCCESS(location);
    }

    @GET
    @Path("getFatherArea")
    public String getFatherArea(@QueryParam("locationId") Long locationId) throws BizCheckedException {
        BaseinfoLocation location = locationRpcService.getFatherByType(locationId, LocationConstant.REGION_AREA);
        return JsonUtils.SUCCESS(location);
    }

    @GET
    @Path("getWarehouseLocationId")
    public String getWarehouseLocationId() {
        Long locationId = locationService.getWarehouseLocationId();
        return JsonUtils.SUCCESS(locationId);
    }

    @POST
    @Path("getItemLocation")
    public String getItemLocation() throws BizCheckedException {
        Map<String, Object> request = RequestUtils.getRequest();
        List<BaseinfoItemLocation> baseinfoItemLocations = itemLocationService.getItemLocation(request);
        return JsonUtils.SUCCESS(baseinfoItemLocations);
    }

    @GET
    @Path("getinventoryLostLocationId")
    public String getInventoryLostLocationId() {
        Long locationId = locationService.getInventoryLostLocationId();
        return JsonUtils.SUCCESS(locationId);
    }


    //    //insert与detail相关,需要同时插入detail的信息
    @POST
    @Path("insertLocation")
    public String insertLocation(LocationDetailRequest request) throws BizCheckedException {
        BaseinfoLocation location = new BaseinfoLocation();
        ObjUtils.bean2bean(request, location);
        return JsonUtils.SUCCESS(locationRpcService.insertLocation(location));
    }

    @POST
    @Path("updateLocation")
    public String updateLocation(LocationDetailRequest request) throws BizCheckedException {
        BaseinfoLocation location = new BaseinfoLocation();
        ObjUtils.bean2bean(request, location);
        return JsonUtils.SUCCESS(locationRpcService.updateLocation(location));
    }

    @POST
    @Path("getLocationList")
    public String searchList(Map<String, Object> params) throws BizCheckedException {
        List<BaseinfoLocation> baseinfoLocationList = locationService.getBaseinfoLocationList(params);
        logger.info(JsonUtils.SUCCESS(baseinfoLocationList));
        return JsonUtils.SUCCESS(baseinfoLocationList);
    }

    @POST
    @Path("countLocation")
    public String countBaseinfoLocation(Map<String, Object> params) throws BizCheckedException {
        return JsonUtils.SUCCESS(locationService.countLocation(params));
    }

    @GET
    @Path("getTemp")
    public String getTemp(@QueryParam("type") Long type) throws BizCheckedException {
        return JsonUtils.SUCCESS(locationService.getAvailableLocationByType(type));
    }

    /**
     * 根据仓库id获取下面的区域
     *
     * @return
     */
    @GET
    @Path("getRegionByWareHouseId")
    public String getRegionByWareHouseId() {
        return JsonUtils.SUCCESS(locationRpcService.getAllRegion());
    }

    /**
     * 根据区域id选择货架
     *
     * @param locationId
     * @return
     */
    @GET
    @Path("getShelfByRegionId")
    public String getShelfByRegionId(@QueryParam("locationId") Long locationId) throws BizCheckedException {
        List<BaseinfoLocation> targetList = new ArrayList<BaseinfoLocation>();
        List<Long> regionType = Arrays.asList(LocationConstant.SHELF, LocationConstant.LOFT, LocationConstant.SPLIT_SHELF);
        for (Long oneType : regionType) {
            List<BaseinfoLocation> locationList = locationService.getChildrenLocationsByType(locationId, oneType);
            targetList.addAll(locationList);
        }

        return JsonUtils.SUCCESS(targetList);
    }
    /**
     * 根仓库
     *
     * @return
     */
    @GET
    @Path("getWarehouseLocation")
    public BaseinfoLocation getWarehouseLocation() {
        return locationRpcService.getWarehouseLocation();
    }
    /**
     * 根据货架或者阁楼找bin
     *
     * @param locationId
     * @return
     */
    @GET
    @Path("getBinByShelf")
    public String getBinByShelf(@QueryParam("locationId") Long locationId) throws BizCheckedException {
        List<BaseinfoLocation> targetList = new ArrayList<BaseinfoLocation>();
        List<Long> regionType = Arrays.asList(LocationConstant.SHELF_PICKING_BIN, LocationConstant.SHELF_STORE_BIN, LocationConstant.LOFT_PICKING_BIN, LocationConstant.LOFT_STORE_BIN, LocationConstant.SPLIT_SHELF_BIN);
        for (Long oneType : regionType) {
            List<BaseinfoLocation> locationList = locationService.getChildrenLocationsByType(locationId, oneType);
            targetList.addAll(locationList);
        }

        return JsonUtils.SUCCESS(targetList);
    }

    /**
     * 根据仓库id查找所有货位
     *
     * @return
     */
    @GET
    @Path("getBinByWarehouseId")
    public String getBinByWarehouseId() {
        return JsonUtils.SUCCESS(locationRpcService.getAllBin());
    }

    /**
     * 获取所有的货架和阁楼的拣货位
     *
     * @return
     */
    @GET
    @Path("getAllColletionBins")
    public String getAllColletionBins() {

        return JsonUtils.SUCCESS(locationRpcService.getColletionBins());
    }

    /**
     * 获取全货架(阁楼)
     *
     * @return
     */
    @GET
    @Path("getAllShelfs")
    public String getAllShelfs() {
        return JsonUtils.SUCCESS(locationRpcService.getAllShelfs());
//          return JsonUtils.SUCCESS(locationRpcService.sortSowLocationByStoreNo());
//        return JsonUtils.SUCCESS(locationRpcService.removeStoreNoOnRoad(26736205236244L));
//        return JsonUtils.SUCCESS(locationRpcService.getLocationIdByCode("DC10-N-002-XXX"));
//        Long locationId = Long.parseLong("9073135487256");
//        BaseinfoLocation baseinfoLocation = locationService.getLocation(locationId);
//        return JsonUtils.SUCCESS(locationService.getNearestStorageByPicking(baseinfoLocation));
//        //测试方法(PickRpcService()的方法)
        //获取list
//        List<WaveDetail> pickDetails = new ArrayList<WaveDetail>();
////        pickDetails.add(waveService.getWaveDetailById(23));
////        pickDetails.add(waveService.getWaveDetailById(24));
////        pickDetails.add(waveService.getWaveDetailById(26));
////        pickDetails.add(waveService.getWaveDetailById(27));
////        pickDetails.add(waveService.getWaveDetailById(28));
//
//        pickDetails.add(waveService.getWaveDetailById(30));
//        pickDetails.add(waveService.getWaveDetailById(31));
//        pickDetails.add(waveService.getWaveDetailById(32));
//        pickDetails.add(waveService.getWaveDetailById(151));
//        pickDetails.add(waveService.getWaveDetailById(152));
//        pickRpcService.calcPickOrder(pickDetails);
//
//        return "yes";
    }

    /**
     * 将mysql一次性导入redis
     *
     * @return
     */
    @GET
    @Path("syncRedisAll")
    public String syncRedisAll() throws BizCheckedException {
        try {
            locationRpcService.syncRedisAll();
            return JsonUtils.SUCCESS("同步成功");
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new BizCheckedException("2210001");
        }
    }

    /**
     * 锁库位
     *
     * @param locationId 库位id
     * @return 结果
     * @throws BizCheckedException
     */
    @GET
    @Path("lockLocation")
    public String lockLocation(@QueryParam("locationId") Long locationId) throws BizCheckedException {
        return JsonUtils.SUCCESS(locationRpcService.lockLocation(locationId));
    }

    /**
     * 解锁库位
     *
     * @param locationId 库位id
     * @return
     * @throws BizCheckedException
     */
    @GET
    @Path("unlockLocation")
    public String unlockLocation(@QueryParam("locationId") Long locationId) throws BizCheckedException {
        return JsonUtils.SUCCESS(locationRpcService.unlockLocation(locationId));
    }

    @GET
    @Path("getLocationIdByCode")
    public String getLocationIdByCode(@QueryParam("locationCode") String locationCode) throws BizCheckedException {
        return JsonUtils.SUCCESS(locationRpcService.getLocationIdByCode(locationCode));
    }

    /**
     * 根据库位的左右范围获取指定库位
     *
     * @return
     * @throws BizCheckedException
     */
    @POST
    @Path("getRangeLocationList")
    public String getRangeLocationList() throws BizCheckedException {
        Map<String, Object> mapQuery = RequestUtils.getRequest();
        List<BaseinfoLocation> locations = locationRpcService.getRangeLocationList(mapQuery);
        if (null == locations || locations.size() < 1) {
            throw new BizCheckedException("2180030");
        }
        return JsonUtils.SUCCESS(locations);
    }

    /**
     * 获取一个location下一层的子节点
     *
     * @return
     * @throws BizCheckedException
     */
    @POST
    @Path("getNextLevelLocations")
    public String getNextLevelLocations() throws BizCheckedException {
        Map<String, Object> mapQuery = RequestUtils.getRequest();
        Long locationId = Long.valueOf(mapQuery.get("locationId").toString());
        if (null == locationId || locationId.equals("")) {
            throw new BizCheckedException("2180004");
        }
        List<BaseinfoLocation> locations = locationRpcService.getNextLevelLocations(locationId);
        if (null == locations || locations.size() < 1) {
            throw new BizCheckedException("2180002");
        }
        return JsonUtils.SUCCESS(locations);
    }

    @GET
    @Path("getLocationType")
    //0:其他 1:货架  2:阁楼
    public String getLocationType(@QueryParam("locationId") String locationId) throws BizCheckedException {
        Long locaId = Long.parseLong(locationId);
        BaseinfoLocation baseinfoLocation1 = locationService.getFatherByType(locaId, LocationConstant.SHELF);
        int locationType = 0;
        if (baseinfoLocation1 != null) {
            locationType = 1;//货架
        } else {
            BaseinfoLocation baseinfoLocation2 = locationService.getFatherByType(locaId, LocationConstant.LOFT);
            if (baseinfoLocation2 != null) {
                locationType = 2;//阁楼
            }
        }
        Map<String,Object> locationMap = new HashMap<String, Object>();
        locationMap.put("locationType",locationType);
        return JsonUtils.SUCCESS(locationMap);
    }

    /**
     * 初始化构建整棵location树结构
     * @return
     * @throws BizCheckedException
     */
    @POST
    @Path("initLocationTree")
    public String initLocationTree() {
        //Map<String, Object> mapQuery = RequestUtils.getRequest(); // 参数,暂时先建立满树
        // 判断表中是否为空,必须为空表时才能构建
        Map<String, Object> params = new HashMap<String, Object>();
        Integer count = locationService.countLocation(params);
        if (count > 0) {
            return JsonUtils.FAIL("123321", "库位表不为空,不能进行初始化构建");
        }
        Map<String, Object> config = JsonUtils.json2Obj("{\"type\":1,\"containerVol\":999999999,\"locationCode\":\"DC40\",\"regionNo\":0,\"passageNo\":0,\"shelfLevelNo\":0,\"binPositionNo\":0,\"children\":[{\"type\":2,\"containerVol\":999999999,\"locationCode\":\"DC40\",\"regionNo\":2,\"children\":[{\"type\":7,\"containerVol\":999999999,\"locationCode\":\"A1\"},{\"type\":8,\"containerVol\":999999999,\"locationCode\":\"A2\"},{\"type\":9,\"containerVol\":999999999,\"locationCode\":\"A3\"},{\"type\":5,\"containerVol\":999999999,\"locationCode\":\"A4\",\"isPassage\":true,\"children\":[{\"levels\":[{\"type\":3,\"containerVol\":0,\"locationCode\":\"-P%d\",\"canStore\":0,\"counts\":2,\"children\":[{\"type\":13,\"containerVol\":0,\"locationCode\":\"-%03d\",\"canStore\":0,\"counts\":3}]}]}]},{\"type\":5,\"containerVol\":999999999,\"locationCode\":\"A5\",\"isPassage\":true,\"children\":[{\"levels\":[{\"type\":3,\"containerVol\":0,\"locationCode\":\"-P%d\",\"canStore\":0,\"counts\":2,\"startCounter\":2,\"step\":2,\"children\":[{\"type\":13,\"containerVol\":0,\"locationCode\":\"abc-%03d\",\"canStore\":0,\"counts\":3,\"withoutFatherCode\":true}]}]}]},{\"type\":12,\"containerVol\":999999999,\"locationCode\":\"A6\"},{\"type\":12,\"containerVol\":999999999,\"locationCode\":\"A7\"},{\"type\":6,\"containerVol\":999999999,\"locationCode\":\"A8\"},{\"type\":32,\"containerVol\":999999999,\"locationCode\":\"A9\"}]}]}", Map.class);
        //Map<String, Object> config = JsonUtils.json2Obj(mapQuery.get("config").toString(), Map.class);
        locationService.initLocationTree(config, -1L);
        return JsonUtils.SUCCESS(config);
    }
}
