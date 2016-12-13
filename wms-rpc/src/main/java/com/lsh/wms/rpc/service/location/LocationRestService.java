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
import com.lsh.wms.core.constant.BinUsageConstant;
import com.lsh.wms.core.constant.LocationConstant;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.wave.WaveService;
import com.lsh.wms.model.baseinfo.BaseinfoItemLocation;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.rpc.service.pick.PickRpcService;
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

    @POST
    @Path("getFatherRegion")
    public String getFatherRegion() throws BizCheckedException {
        Map<String, Object> request = RequestUtils.getRequest();
        Long locationId = Long.valueOf(request.get("locationId").toString());
        BaseinfoLocation location = locationRpcService.getFatherRegionBySonId(locationId);
        return JsonUtils.SUCCESS(location);
    }

    @GET
    @Path("getWarehouseLocationId")
    public String getWarehouseLocationId() {
        Long locationId = locationService.getWarehouseLocation().getLocationId();
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
        Long locationId = locationService.getInventoryLostLocation().getLocationId();
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
            if (null == locationList || locationList.size() < 1) {
                continue;
            }
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
//        List<Long> regionType = Arrays.asList(LocationConstant.SHELF_PICKING_BIN, LocationConstant.SHELF_STORE_BIN, LocationConstant.LOFT_PICKING_BIN, LocationConstant.LOFT_STORE_BIN, LocationConstant.SPLIT_SHELF_BIN);
//        for (Long oneType : regionType) {
        List<BaseinfoLocation> locationList = locationService.getChildrenLocationsByType(locationId, LocationConstant.BIN);
//            targetList.addAll(locationList);
//        }

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
     * 获取可用的所有的货架和阁楼的拣货位
     *
     * @return
     */
    @GET
    @Path("getColletionBinsCanUse")
    public String getColletionBinsCanUse() {
        //获取所有商品已使用的拣货位
        List<BaseinfoItemLocation> baseinfoItemLocations = itemLocationService.getItemLocation(null);
        List<Long> locationList = new ArrayList<Long>();
        for (BaseinfoItemLocation b : baseinfoItemLocations) {
            locationList.add(b.getPickLocationid());
        }
        //获取所有拣货位
        List<BaseinfoLocation> collectionBins = locationRpcService.getColletionBins();
        //获取存拣合一拣货位
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("binUsage", BinUsageConstant.BIN_PICK_STORE);
        mapQuery.put("isValid", LocationConstant.IS_VALID);
        mapQuery.put("isLocked", LocationConstant.UNLOCK);
        mapQuery.put("canStore", LocationConstant.CAN_STORE);
        mapQuery.put("regionType", LocationConstant.SPLIT_AREA);
        mapQuery.put("type", LocationConstant.BIN);
        List<BaseinfoLocation> pickstoreBins = locationService.getBaseinfoLocationList(mapQuery);
        collectionBins.addAll(pickstoreBins);
        //货架拣货位
        List<BaseinfoLocation> shelfsList = new ArrayList<BaseinfoLocation>();
        //阁楼拣货位
        List<BaseinfoLocation> loftsList = new ArrayList<BaseinfoLocation>();
        //拆零区拣货位
        List<BaseinfoLocation> splitList = new ArrayList<BaseinfoLocation>();

        for (BaseinfoLocation b : collectionBins) {
            if (locationList.contains(b.getLocationId())) {
                //拣货位已被使用
                continue;
            }
            if (LocationConstant.SHELFS.compareTo(b.getRegionType()) == 0) {
                //货架
                shelfsList.add(b);
            } else if (LocationConstant.LOFTS.compareTo(b.getRegionType()) == 0) {
                //阁楼
                loftsList.add(b);

            } else if (LocationConstant.SPLIT_AREA.compareTo(b.getRegionType()) == 0) {
                //拆零区
                splitList.add(b);
            }
        }
        Map<String, Object> returnMap = new HashMap<String, Object>();
        returnMap.put("shelfsList", shelfsList);
        returnMap.put("loftsList", loftsList);
        returnMap.put("splitList", splitList);
        return JsonUtils.SUCCESS(returnMap);
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
//        return JsonUtils.SUCCESS(locationRpcService.getColletionBins());
//          return JsonUtils.SUCCESS(locationRpcService.sortSowLocationByStoreNo());
//        return JsonUtils.SUCCESS(locationService.getLocation(-1L));
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
//        BaseinfoLocation location = locationService.getLocation(4432685253259L);
//          return JsonUtils.SUCCESS(locationService.getNearestStorageByPicking(location));
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
        Map<String, Object> locationMap = new HashMap<String, Object>();
        locationMap.put("locationType", locationType);
        return JsonUtils.SUCCESS(locationMap);
    }

    /**
     * 拆分货位
     *
     * @return
     * @throws BizCheckedException
     */
    @POST
    @Path("splitBin")
    public String splitBin() throws BizCheckedException {
        Map<String, Object> mapQuery = RequestUtils.getRequest();
        Long locationId = Long.valueOf(mapQuery.get("locationId").toString());
        String locationCode = mapQuery.get("locationCode").toString();
        BaseinfoLocation newLocation = locationRpcService.splitBin(locationId, locationCode);
        return JsonUtils.SUCCESS(newLocation);
    }

    /**
     * 通过制定的type获取位置
     *
     * @param type
     * @return
     * @throws BizCheckedException
     */
    @GET
    @Path("getLocationByType")
    public String getLocationByType(@QueryParam("type") Long type) throws BizCheckedException {
        return JsonUtils.SUCCESS(locationService.getLocationsByType(type));
    }

    /**
     * 根据祖先id和子孙的type,获取指定位置
     *
     * @param sonType
     * @return
     * @throws BizCheckedException
     */
    @GET
    @Path("getChildrenLocationsByFatherIdAndType")
    public String getChildrenLocationsByFatherIdAndType(@QueryParam("fatherLocationId")Long fatherLocationId, @QueryParam("sonType")Long sonType) throws BizCheckedException {
        return JsonUtils.SUCCESS(locationService.getChildrenLocationsByType(fatherLocationId, sonType));
    }

    /**
     * 初始化构建整棵location树结构
     *
     * @return
     */
    @POST
    @Path("initLocationTree")
    public String initLocationTree() {
        Map<String, Object> mapQuery = RequestUtils.getRequest(); // 参数,暂时先建立满树
        // 判断表中是否为空,必须为空表时才能构建
        Map<String, Object> params = new HashMap<String, Object>();
        Integer count = locationService.countLocation(params);
        if (count > 0) {
            return JsonUtils.FAIL("123321", "库位表不为空,不能进行初始化构建");
        }
        //Map<String, Object> config = JsonUtils.json2Obj("{\"type\":1,\"containerVol\":999999999,\"locationCode\":\"DC40\",\"regionNo\":0,\"passageNo\":0,\"shelfLevelNo\":0,\"binPositionNo\":0,\"children\":[{\"type\":3,\"containerVol\":0,\"locationCode\":\"A1\",\"isPassage\":true,\"withoutFatherCode\":true,\"canStore\":0,\"children\":[{\"type\":27,\"containerVol\":0,\"locationCode\":\"-%02d\",\"canStore\":0,\"isBin\":true,\"startCounter\":1,\"step\":2,\"counts\":2,\"children\":[{\"type\":15,\"containerVol\":999999999,\"locationCode\":\"-%d0\",\"counts\":2,\"isLevel\":true}]}]}]}", Map.class);
        Map<String, Object> config = JsonUtils.json2Obj(mapQuery.get("config").toString(), Map.class);
        locationService.initLocationTree(config, -1L);
        return JsonUtils.SUCCESS(config);
    }

    /**
     * 构建库位子树
     *
     * @return
     */
    @POST
    @Path("buildLocations")
    public String buildLocations() throws BizCheckedException {
        Map<String, Object> mapQuery = RequestUtils.getRequest();
        Map<String, Object> config = JsonUtils.json2Obj(mapQuery.get("config").toString(), Map.class);
        String fatherLocationCode = mapQuery.get("fatherLocationCode").toString();
        BaseinfoLocation fatherLocation = locationService.getLocationByCode(fatherLocationCode);
        if (fatherLocation == null) {
            throw new BizCheckedException("2180001");
        }
        locationService.initLocationTree(config, fatherLocation.getLocationId());
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("response", true);
        return JsonUtils.SUCCESS(result);
    }
}
