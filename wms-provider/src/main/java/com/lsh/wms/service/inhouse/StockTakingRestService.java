package com.lsh.wms.service.inhouse;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.alibaba.fastjson.JSON;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.api.service.inhouse.IStockTakingRestService;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.service.csi.CsiSkuService;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.constant.LocationConstant;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.stock.StockLotService;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.core.service.taking.StockTakingService;
import com.lsh.wms.core.service.task.StockTakingTaskService;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.csi.CsiSku;
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

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.*;

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
    private StockTakingTaskService stockTakingTaskService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private CsiSkuService skuService;

    @POST
    @Path("create")
    public String create(StockTakingRequest request) throws BizCheckedException{

        StockTakingHead head = stockTakingService.getHeadById(request.getTakingId());
        if(head!=null){
            return JsonUtils.TOKEN_ERROR("请勿重复提交");
        }
        head = new StockTakingHead();
        ObjUtils.bean2bean(request, head);
        List<StockTakingDetail> detailList = prepareDetailList(head);
        stockTakingService.insertHead(head);
        this.createTask(head, detailList, 1L, head.getDueTime());
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
    @GET
    @Path("genId")
    public String genId(){
        Long takingId=RandomUtils.genId();
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
        } else if (request.getStorageId() != 0) {
            //根据货架得出库位
            locationList = this.getBinByShelf(request.getStorageId());
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
        Set<Long> locationSet =new HashSet<Long>();
        for(StockQuant quant:quantList){
            if(quant.getLocationId()!=null) {
                locationSet.add(quant.getLocationId());
            }
        }
        if(locationList!=null && locationList.size()!=0){
            locationList.retainAll(new ArrayList<Long>(locationSet));
        }else {
            locationList =new ArrayList<Long>(locationSet);
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
            if(location.getType().compareTo(LocationConstant.SHELFS)==0 ||location.getType().compareTo(LocationConstant.LOFTS)==0  ||location.getType().compareTo(LocationConstant.SPLIT_AREA)==0 ||location.getType().compareTo(LocationConstant.FLOOR)==0) {
                locations.add(locationId);
                i++;
            }
        }

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
        queryMap.put("itemList",itemList);
        List<ItemAndSupplierRelation> relationList = lotService.getSupplierIdOrItemId(queryMap);
        for(ItemAndSupplierRelation relation:relationList){
            supplierSet.add(relation.getSupplierId());
        }
        return JsonUtils.SUCCESS(supplierSet);
    }

    private List<StockTakingDetail> prepareDetailListByLocation(List<Long> locationList, List<StockQuant> quantList){
        Map<Long, StockQuant> mapLoc2Quant = new HashMap<Long, StockQuant>();
        for (StockQuant quant : quantList) {
            mapLoc2Quant.put(quant.getLocationId(), quant);
        }

        Long idx = 0L;
        List<StockTakingDetail> detailList = new ArrayList<StockTakingDetail>();
        for (Long locationId : locationList) {
            StockTakingDetail detail = new StockTakingDetail();
            detail.setLocationId(locationId);
            detail.setDetailId(idx);

            StockQuant quant = mapLoc2Quant.get(locationId);
            if (quant != null ) {
                detail.setTheoreticalQty(quant.getQty());
                detail.setSkuId(quant.getSkuId());
                detail.setRealSkuId(detail.getSkuId());
            }
            idx++;
            detailList.add(detail);
        }
        return detailList;
    }

    private List<StockTakingDetail> prepareDetailListByItem(List<StockQuant> quantList) {
        Long idx = 0L;
        List<StockTakingDetail> detailList = new ArrayList<StockTakingDetail>();
        Map<String,StockQuant> mergeQuantMap =new HashMap<String, StockQuant>();
        for(StockQuant quant:quantList){
            String key= "i:"+quant.getItemId()+"l:"+quant.getLocationId();
            if (mergeQuantMap.containsKey(key)){
                StockQuant stockQuant =mergeQuantMap.get(key);
                stockQuant.setQty(quant.getQty().add(stockQuant.getQty()));
            }else {
                mergeQuantMap.put(key,quant);
            }
        }
        for (String key : mergeQuantMap.keySet()) {
            StockQuant quant=mergeQuantMap.get(key);
            StockTakingDetail detail = new StockTakingDetail();
            detail.setDetailId(idx);
            detail.setLocationId(quant.getLocationId());
            detail.setSkuId(quant.getSkuId());
            detail.setContainerId(quant.getContainerId());
            detail.setItemId(quant.getItemId());
            detail.setRealItemId(quant.getItemId());
            detail.setRealSkuId(detail.getSkuId());
            detail.setTheoreticalQty(quant.getQty());
            detail.setPackName(quant.getPackName());
            detail.setPackUnit(quant.getPackUnit());
            detail.setLotId(quant.getLotId());
            detailList.add(detail);
            idx++;
        }
        logger.info(JsonUtils.SUCCESS(quantList));
        return detailList;
    }

    private List<StockTakingDetail> prepareDetailList(StockTakingHead head) {

        List<Long> locationList= JSON.parseArray(head.getLocationList(), Long.class);
        List<Long> locations=new ArrayList<Long>();
        if (locationList != null && locationList.size()!=0) {
            for(Long locationId:locationList) {
                locations.add(locationId);
            }
        }else {
            Long locationId = locationService.getWarehouseLocationId();
            locations.addAll(locationService.getStoreLocationIds(locationId));
        }
            Map<String, Object> mapQuery = new HashMap<String, Object>();
            mapQuery.put("locationIdList", locations);
            mapQuery.put("itemId", head.getItemId());
            mapQuery.put("lotId", head.getLotId());
            mapQuery.put("ownerId", head.getOwnerId());
            mapQuery.put("supplierId", head.getSupplierId());
            List<StockQuant> quantList = quantService.getQuants(mapQuery);

        if (head.getTakingType().equals(1L)) {
            return this.prepareDetailListByItem(quantList);
        }
        else {
            return this.prepareDetailListByLocation(locationList, quantList);
        }
    }

    public void createTask(StockTakingHead head, List<StockTakingDetail> detailList,Long round,Long dueTime) throws BizCheckedException{
        List<TaskEntry> taskEntryList=new ArrayList<TaskEntry>();
        for(StockTakingDetail detail:detailList) {
            TaskInfo taskInfo = new TaskInfo();
            taskInfo.setTaskName("盘点任务[ " + detail.getLocationId() + "]");
            taskInfo.setPlanId(head.getTakingId());
            taskInfo.setDueTime(dueTime);
            taskInfo.setPlanner(head.getPlanner());
            taskInfo.setStatus(1L);
            taskInfo.setLocationId(detail.getLocationId());
            taskInfo.setSkuId(detail.getSkuId());
            taskInfo.setItemId(detail.getItemId());
            taskInfo.setContainerId(detail.getContainerId());
            taskInfo.setType(TaskConstant.TYPE_STOCK_TAKING);
            taskInfo.setDraftTime(DateUtils.getCurrentSeconds());

            StockTakingTask task = new StockTakingTask();
            task.setRound(round);
            task.setTakingId(head.getTakingId());

            TaskEntry taskEntry = new TaskEntry();
            taskEntry.setTaskInfo(taskInfo);
            List details = new ArrayList();
            details.add(detail);
            taskEntry.setTaskDetailList(details);

            StockTakingTask taskHead = new StockTakingTask();
            taskHead.setRound(round);
            taskHead.setTakingId(taskInfo.getPlanId());
            taskEntry.setTaskHead(taskHead);
            taskEntryList.add(taskEntry);
            iTaskRpcService.create(TaskConstant.TYPE_STOCK_TAKING, taskEntry);
        }
    }

    public void update(StockTakingHead head) throws BizCheckedException{
        StockTakingHead oldHead = stockTakingService.getHeadById(head.getTakingId());
        if(oldHead==null){
           throw  new BizCheckedException("2550007");
        }
        this.cancelTask(head.getTakingId());
        head.setId(oldHead.getId());
        stockTakingService.updateHead(head);
        List<StockTakingDetail> detailList = prepareDetailList(head);
        this.createTask(head, detailList, 1L, head.getDueTime());
    }
    public String cancelTask(Long takingId) throws BizCheckedException {
        Map<String,Object> queryMap =new HashMap();
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
        List<Long> regionType = Arrays.asList(LocationConstant.SHELF_PICKING_BIN, LocationConstant.SHELF_STORE_BIN, LocationConstant.LOFT_PICKING_BIN, LocationConstant.LOFT_STORE_BIN, LocationConstant.FLOOR_BIN, LocationConstant.TEMPORARY_BIN, LocationConstant.COLLECTION_BIN, LocationConstant.BACK_BIN, LocationConstant.DEFECTIVE_BIN);
        for (Long oneType : regionType) {
            List<BaseinfoLocation> locationList = locationService.getSubLocationList(locationId, oneType);
            for(BaseinfoLocation location:locationList){
                targetList.add(location.getLocationId());
            }
        }
        return targetList;
    }
    //根据货架或者阁楼找bin
    public List<Long> getBinByShelf(Long locationId) {
        List<Long> targetList = new ArrayList<Long>();
        List<Long> regionType = Arrays.asList(LocationConstant.SHELF_PICKING_BIN, LocationConstant.SHELF_STORE_BIN, LocationConstant.LOFT_PICKING_BIN, LocationConstant.LOFT_STORE_BIN);
        for (Long oneType : regionType) {
            List<BaseinfoLocation> locationList = locationService.getSubLocationList(locationId, oneType);
            for(BaseinfoLocation location:locationList){
                targetList.add(location.getLocationId());
            }
        }
        return targetList;
    }

}
