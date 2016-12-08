package com.lsh.wms.service.inhouse;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.base.common.utils.StrUtils;
import com.lsh.wms.api.service.inhouse.IStockTakingRestService;
import com.lsh.wms.api.service.inhouse.IStockTakingProviderRpcService;
import com.lsh.wms.api.service.location.ILocationRpcService;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.constant.BinUsageConstant;
import com.lsh.wms.core.constant.LocationConstant;
import com.lsh.wms.core.constant.RedisKeyConstant;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.dao.redis.RedisStringDao;
import com.lsh.wms.core.service.csi.CsiSkuService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.stock.StockLotService;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.core.service.taking.StockTakingService;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.core.service.task.StockTakingTaskService;
import com.lsh.wms.core.service.utils.IdGenerator;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.csi.CsiSku;
import com.lsh.wms.model.so.ObdDetail;
import com.lsh.wms.model.stock.ItemAndSupplierRelation;
import com.lsh.wms.model.stock.StockLot;
import com.lsh.wms.model.stock.StockQuant;
import com.lsh.wms.model.taking.LocationListRequest;
import com.lsh.wms.model.taking.StockTakingDetail;
import com.lsh.wms.model.taking.StockTakingHead;
import com.lsh.wms.model.taking.StockTakingRequest;
import com.lsh.wms.model.task.StockTakingTask;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.vendor.OpenJpaDialect;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by mali on 16/7/14.
 */

@Service(protocol = "rest")
@Path("inhouse/stock_taking")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class StockTakingRestService implements IStockTakingRestService {
    private static final Logger logger = LoggerFactory.getLogger(StockTakingRestService.class);

    @Autowired
    private RedisStringDao redisStringDao;
    @Autowired
    private StockTakingService stockTakingService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private StockQuantService quantService;

    @Autowired
    private StockLotService lotService;

    @Reference
    private ITaskRpcService iTaskRpcService;
    @Autowired
    private BaseTaskService baseTaskService;
    @Reference
    private ILocationRpcService locationRpcService;

    @Autowired
    private StockTakingTaskService stockTakingTaskService;

    @Autowired
    private CsiSkuService skuService;

    @Autowired
    protected IdGenerator idGenerator;
    @Reference
    private IStockTakingProviderRpcService iStockTakingProviderRpcService;

    @POST
    @Path("create")
    public String create(StockTakingRequest request) throws BizCheckedException{
        String key = StrUtils.formatString(RedisKeyConstant.TAKING_KEY,request.getTakingId());
        String takingId = redisStringDao.get(key);
        if(takingId != null) {
            return JsonUtils.TOKEN_ERROR("请勿重复提交");
        }
        redisStringDao.set(key,request.getTakingId(),24,TimeUnit.HOURS);
        StockTakingHead head = new StockTakingHead();
        ObjUtils.bean2bean(request, head);
        List<StockTakingDetail> detailList = iStockTakingProviderRpcService.prepareDetailList(head);
        iStockTakingProviderRpcService.createTask(head, detailList, 1L, head.getDueTime());
        return JsonUtils.SUCCESS();
    }
    @POST
    @Path("update")
    public String update(StockTakingRequest request) throws BizCheckedException{
        StockTakingHead head = new StockTakingHead();
        ObjUtils.bean2bean(request, head);
        this.update(head);
        return JsonUtils.SUCCESS();
    }
    @GET
    @Path("cancel")
    public String cancel(@QueryParam("takingId") Long takingId) throws BizCheckedException{
        StockTakingHead head = stockTakingService.getHeadById(takingId);
        if(head==null){
            return JsonUtils.TOKEN_ERROR("盘点任务不存在");
        }
        head.setStatus(5L);
        stockTakingService.updateHead(head);
        this.cancelTask(takingId);
        return JsonUtils.SUCCESS();
    }
    @GET
    @Path("getHead")
    public String getHead(@QueryParam("takingId") Long takingId) throws BizCheckedException{
        StockTakingHead head = stockTakingService.getHeadById(takingId);
        return JsonUtils.SUCCESS(head);
    }

    @POST
    @Path("createPlanWarehouse")
    public String createPlanWarehouse(Map<String,Object> mapQuery) throws BizCheckedException {
        List zoneIds = (List)(mapQuery.get("zoneIds"));
        for(int i = 0; i < zoneIds.size(); ++i){
            zoneIds.set(i, Long.valueOf(zoneIds.get(i).toString()));
        }
        Long planner = Long.valueOf(mapQuery.get("uid").toString());
        iStockTakingProviderRpcService.createPlanWarehouse(zoneIds, planner);
        return JsonUtils.SUCCESS();
    }

    @POST
    @Path("createPlanSales")
    public String createPlanSales(List<Long> zoneIds, Long planer) throws BizCheckedException {
        iStockTakingProviderRpcService.createPlanSales(zoneIds, planer);
        return JsonUtils.SUCCESS();
    }

    @GET
    @Path("genId")
    public String genId(@QueryParam("taskType") Long taskType){
        Long takingId = 0L;
        if(taskType.compareTo(100L)==0){
            takingId = RandomUtils.genId();
        }else {
            String idKey = "task_" + taskType.toString();
            takingId = idGenerator.genId(idKey, true, true);
        }
        return JsonUtils.SUCCESS(takingId);
    }
    @POST
    @Path("getList")
    public String getList(Map<String,Object> mapQuery) throws BizCheckedException{
        List<StockTakingHead> heads = stockTakingService.queryTakingHead(mapQuery);
        return JsonUtils.SUCCESS(heads);
    }
    @POST
    @Path("getCount")
    public String getCount(Map<String,Object> mapQuery) {
        Integer count = stockTakingService.countHead(mapQuery);
        return JsonUtils.SUCCESS(count);
    }
    @GET
    @Path("getDetail")
    public String getDetail(@QueryParam("takingId") long takingId) throws BizCheckedException{
        Long round =stockTakingService.chargeTime(takingId);
        Map<String,Object> queryMap =new HashMap<String, Object>();
        Long time =1L;
        List<List> result =new ArrayList<List>();
        Map<String,Object> resultMap =new HashMap<String, Object>();
        StockTakingHead head = stockTakingService.getHeadById(takingId);
        resultMap.put("head", head);
        while(time<=round) {
            List details =new ArrayList();
            queryMap.put("round", time);
            queryMap.put("takingId", takingId);
            queryMap.put("isValid",1);
            List<StockTakingTask> stockTakingTaskList = stockTakingTaskService.getTakingTask(queryMap);
            for(StockTakingTask takingTask:stockTakingTaskList) {
                TaskEntry entry = iTaskRpcService.getTaskEntryById(takingTask.getTaskId());
                List detailList = entry.getTaskDetailList();
                for(Object tmp:detailList) {
                    Map <String,Object> one = new HashMap<String, Object>();
                    StockTakingDetail detail = (StockTakingDetail) tmp;
                    BaseinfoLocation areaFather = locationService.getAreaFather(detail.getLocationId());
                    BaseinfoLocation location = locationService.getLocation(detail.getLocationId());
                    CsiSku csiSku = skuService.getSku(detail.getSkuId());
                    if(detail.getItemId().compareTo(0L)==0){
                        detail.setItemId(-1L);
                    }
                    StockLot lot = lotService.getStockLotByLotId(detail.getLotId());
                    one.put("operator", detail.getOperator());
                    one.put("supplierId", lot ==null ? " " : lot.getSupplierId());
                    one.put("itemId", detail.getItemId());
                    one.put("theoreticalQty", detail.getTheoreticalQty());
                    one.put("areaCode", areaFather == null ? " " : areaFather.getLocationCode());
                    one.put("locationCode", location == null ? " " : location.getLocationCode());
                    one.put("realQty", detail.getRealQty());
                    one.put("difference", detail.getRealQty().subtract(detail.getTheoreticalQty()));
                    one.put("reason", "");
                    one.put("itemName", csiSku == null ? " " : csiSku.getSkuName());
                    one.put("updatedAt", detail.getUpdatedAt());
                    details.add(one);
                }
            }
            result.add(details);
            time++;
        }
        resultMap.put("result", result);
        return JsonUtils.SUCCESS(resultMap);
    }
    @POST
    @Path("getLocationList")
    public String getLocationList(LocationListRequest request) {
        List<Long> locationList =null;
        int locationNum= Integer.MAX_VALUE;
        //Long itemId,Long AreaId,Long supplierId,Long storageId int locationNum
        if(request.getLocationNum()!=0) {
            locationNum = request.getLocationNum();
        }


        //库区，货架得到库位
        if (request.getAreaId() != 0 && request.getStorageId() == 0) {
            //根据库区得出库位
            locationList = this.getBinByWarehouseId(request.getAreaId());
            if(locationList ==null || locationList.size()==0){
                locationList = new ArrayList<Long>();
                locationList.add(request.getAreaId());
            }
        } else if (request.getStorageId() != 0) {
            //根据货架得出库位
            locationList = this.getBinByShelf(request.getStorageId());

            if(locationList == null) {
                locationList = new ArrayList<Long>();
            }

        }

        //商品,供应商得到库位
        Map<String,Object> queryMap =new HashMap<String, Object>();
        if(request.getSupplierId().compareTo(0L)!=0) {
            queryMap.put("supplierId", request.getSupplierId());
        }
        if(request.getItemId().compareTo(0L)!=0) {
            queryMap.put("itemId", request.getItemId());
        }
        List<StockQuant>quantList = quantService.getQuants(queryMap);
        Set<Long> longs = new HashSet<Long>();
        List<Long> taskLocation =new ArrayList<Long>();

        //取到盘点库位
        Map<String,Object> query = new HashMap<String, Object>();
        query.put("type",TaskConstant.TYPE_STOCK_TAKING);
        query.put("valid",1);
        List<TaskInfo> infos = baseTaskService.getTaskInfoList(query);
        if(infos!=null && infos.size()!=0){
            for(TaskInfo info:infos){
                taskLocation.add(info.getLocationId());
            }
        }


        for(StockQuant quant:quantList){
            longs.add(quant.getLocationId());
        }
        if(locationList!=null ){
            locationList.retainAll(longs);
        }else {
            locationList =new ArrayList<Long>(longs);
        }

        List<Long> locations = new ArrayList<Long>();
        int i=0;
        while (i<locationNum){
            if(locationList.size()==0){
                break;
            }

            // 取出一个随机数
            int r = (int) (Math.random() * locationList.size());
            Long locationId = locationList.get(r);

            // 排除已经取过的值
            locationList.remove(r);

            //过滤掉区的上一层
            if(locationId.compareTo(0L)==0 ||locationId.compareTo(1L)==0 || locationId.compareTo(2L)==0){
                continue;
            }
            BaseinfoLocation location = locationService.getFatherByClassification(locationId);
            if(location==null){
                continue;
            }
            //是阁楼区，货架区，存捡一体区，地堆区
            if(location.getType().compareTo(LocationConstant.SHELFS)==0 ||location.getType().compareTo(LocationConstant.LOFTS)==0  ||location.getType().compareTo(LocationConstant.SPLIT_AREA)==0 ||location.getType().compareTo(LocationConstant.FLOOR)==0 || (location.getType().equals(LocationConstant.BIN) && location.getBinUsage().equals(BinUsageConstant.BIN_FLOOR_STORE))) {
                locations.add(locationId);
                i++;
            }
        }
        locations.removeAll(taskLocation);
        return JsonUtils.SUCCESS(locations);
    }
    @GET
    @Path("getItemList")
    public String getItemList(@QueryParam("supplierId") Long supplierId) {
        List<Long> supplierList =null;
        if(supplierId!=null){
            supplierList=new ArrayList<Long>();
            supplierList.add(supplierId);
        }
        Set<Long> itemSet =new HashSet<Long>();
        Map<String,Object> queryMap = new HashMap<String, Object>();
        queryMap.put("supplierList", supplierList);
        List<ItemAndSupplierRelation> relationList = lotService.getSupplierIdOrItemId(queryMap);
        for(ItemAndSupplierRelation relation:relationList){
            itemSet.add(relation.getItemId());
        }
        return JsonUtils.SUCCESS(itemSet);
    }
    @GET
    @Path("getSupplierList")
    public String getSupplierList(@QueryParam("itemId") Long itemId) {
        List<Long> itemList=null;
        if(itemId!=null){
            itemList=new ArrayList<Long>();
            itemList.add(itemId);
        }
        Set<Long> supplierSet =new HashSet<Long>();
        Map<String,Object> queryMap = new HashMap<String, Object>();
        queryMap.put("itemList", itemList);
        List<ItemAndSupplierRelation> relationList = lotService.getSupplierIdOrItemId(queryMap);
        for(ItemAndSupplierRelation relation:relationList){
            supplierSet.add(relation.getSupplierId());
        }
        return JsonUtils.SUCCESS(supplierSet);
    }

    public void update(StockTakingHead head) throws BizCheckedException{
        StockTakingHead oldHead = stockTakingService.getHeadById(head.getTakingId());
        if(oldHead==null){
           throw  new BizCheckedException("2550007");
        }
        this.cancelTask(head.getTakingId());
        head.setId(oldHead.getId());
        stockTakingService.updateHead(head);
        List<StockTakingDetail> detailList = iStockTakingProviderRpcService.prepareDetailList(head);
        iStockTakingProviderRpcService.createTask(head, detailList, 1L, head.getDueTime());
    }
    public String cancelTask(Long takingId) throws BizCheckedException {
        Map<String,Object> queryMap = new HashMap<String, Object>();
        queryMap.put("takingId", takingId);
        List<Long> taskList = new ArrayList<Long>();
        List<StockTakingTask> takingTasks = stockTakingTaskService.getTakingTask(queryMap);
        for(StockTakingTask task :takingTasks){
           taskList.add(task.getTaskId());
        }
        iTaskRpcService.batchCancel(TaskConstant.TYPE_STOCK_TAKING, taskList);
        return JsonUtils.SUCCESS();
    }
    //根据仓库id查找所有货位
    public List<Long> getBinByWarehouseId(Long locationId) {
        List<Long> targetList = new ArrayList<Long>();
//        List<Long> regionType = Arrays.asList(LocationConstant.SHELF_PICKING_BIN, LocationConstant.SHELF_STORE_BIN, LocationConstant.LOFT_PICKING_BIN, LocationConstant.LOFT_STORE_BIN, LocationConstant.FLOOR_BIN, LocationConstant.TEMPORARY_BIN, LocationConstant.COLLECTION_BIN, LocationConstant.BACK_BIN, LocationConstant.DEFECTIVE_BIN);
//        for (Long oneType : regionType) {
//            List<BaseinfoLocation> locationList = locationService.getSubLocationList(locationId, oneType);
//            for(BaseinfoLocation location:locationList){
//                targetList.add(location.getLocationId());
//            }
//        }
//        return targetList;
        List<BaseinfoLocation> baseinfoLocations =  locationService.getChildrenLocationsByType(locationId,LocationConstant.BIN);
        for(BaseinfoLocation location :baseinfoLocations){
            targetList.add(location.getLocationId());
        }
        return targetList;
    }
    //根据货架或者阁楼找bin
    public List<Long> getBinByShelf(Long locationId) {
       List<Long> targetList = new ArrayList<Long>();
//        List<Long> regionType = Arrays.asList(LocationConstant.SHELF_PICKING_BIN, LocationConstant.SHELF_STORE_BIN, LocationConstant.LOFT_PICKING_BIN, LocationConstant.LOFT_STORE_BIN);
//        for (Long oneType : regionType) {
//            List<BaseinfoLocation> locationList = locationService.getSubLocationList(locationId, oneType);
//            for(BaseinfoLocation location:locationList){
//                targetList.add(location.getLocationId());
//            }
//        }
//        return targetList;
        List<BaseinfoLocation> baseinfoLocations = locationService.getChildrenLocationsByType(locationId, LocationConstant.BIN);
        for(BaseinfoLocation location :baseinfoLocations){
            targetList.add(location.getLocationId());
        }
        return targetList;
    }

}
