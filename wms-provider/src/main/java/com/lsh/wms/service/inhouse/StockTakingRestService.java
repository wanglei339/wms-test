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
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.stock.StockMoveService;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.core.service.taking.StockTakingService;
import com.lsh.wms.core.service.task.StockTakingTaskService;
import com.lsh.wms.model.StockTakingInfo;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.stock.StockMove;
import com.lsh.wms.model.stock.StockQuant;
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
import java.math.BigDecimal;
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
    private StockMoveService moveService;

    @Reference
    private ITaskRpcService iTaskRpcService;

    @Autowired
    private StockTakingTaskService stockTakingTaskService;

    @POST
    @Path("create")
    public String create(StockTakingRequest request) throws BizCheckedException{
        StockTakingHead head = new StockTakingHead();
        ObjUtils.bean2bean(request, head);
        List<StockTakingDetail> detailList = prepareDetailList(head);
        stockTakingService.create(head, detailList);
        this.createTask(head, detailList, 1L, head.getDueTime());
        return JsonUtils.SUCCESS();
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
        List<StockTakingInfo> infos =new ArrayList<StockTakingInfo>();
        for (StockTakingHead head:heads) {
            StockTakingInfo info =new StockTakingInfo();
            info.setHead(head);
            Set<Long> operatorSet =new HashSet<Long>();
            Map<String,Object> taskMap =new HashMap<String, Object>();
            taskMap.put("takingId",head.getTakingId());
            List<StockTakingTask> takingTasks =stockTakingTaskService.getTakingTask(taskMap);
            for(StockTakingTask task:takingTasks){
                TaskEntry entry =iTaskRpcService.getTaskEntryById(task.getTaskId());
                if(entry.getTaskInfo().getOperator()!=0) {
                    operatorSet.add(entry.getTaskInfo().getOperator());
                }
            }
            info.setOperatorSet(operatorSet);
            infos.add(info);
        }
        return JsonUtils.SUCCESS(infos);
    }
    @POST
    @Path("getCount")
    public String getCount(Map<String,Object> mapQuery) {
        Integer count = stockTakingService.countHead(mapQuery);
        Map<String,Object> result =new HashMap<String, Object>();
        result.put("count",count);
        return JsonUtils.SUCCESS(result);
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
            List<StockTakingTask> stockTakingTaskList = stockTakingTaskService.getTakingTask(queryMap);
            for(StockTakingTask takingTask:stockTakingTaskList) {
                Map <String,Object> one = new HashMap<String, Object>();
                TaskEntry entry = iTaskRpcService.getTaskEntryById(takingTask.getTaskId());
                StockTakingDetail detail =(StockTakingDetail)(entry.getTaskDetailList().get(0));
                Long supplierId=quantService.getSupplierByLocationAndItemId(detail.getLocationId(),detail.getItemId());
                one.put("operator",entry.getTaskInfo().getOperator());
                one.put("supplierId",supplierId);
                one.put("itemId",detail.getItemId());
                one.put("theoreticalQty",detail.getTheoreticalQty());
                one.put("areaId",locationService.getAreaFather(detail.getLocationId()));
                one.put("locationId",detail.getLocationId());
                one.put("realQty",detail.getRealQty());
                one.put("difference",detail.getRealQty().subtract(detail.getTheoreticalQty()));
                one.put("reason","哈哈哈");
                one.put("updatedAt",detail.getUpdatedAt());
                details.add(one);
            }
            result.add(details);
            time++;
        }
        resultMap.put("result",result);
        return JsonUtils.SUCCESS(resultMap);
    }
    @POST
    @Path("getLocationList")
    public String getLocationList(Map<String,Object> mapQuery) {
        int locationNum = Integer.parseInt(mapQuery.get("locationNum").toString());
        //Long itemId,Long AreaId,Long supplierId,int locationNum
        BaseinfoLocation location = locationService.getWarehouseLocation();
        List<Long> locationList = locationService.getStoreLocationIds(location.getLocationId());

        if(locationList.size()<=locationNum){
            locationNum = locationList.size();
        }
        long[] locations=new long[locationNum];
        for (int i = 0; i < locations.length; i++) {

            // 取出一个随机数
            int r = (int) (Math.random() * locationList.size());

            locations[i] = locationList.get(r);

            // 排除已经取过的值
            locationList.remove(r);
        }
        Map<String,Object> result =new HashMap<String, Object>();
        result.put("locationList",locations);
        return JsonUtils.SUCCESS(result);
    }

    public void createNextDetail(Long stockTakingId,Long roundTime) throws BizCheckedException{
        Map queryMap = new HashMap();
        StockTakingHead head = stockTakingService.getHeadById(stockTakingId);
        List<StockTakingDetail> detailList = new ArrayList<StockTakingDetail>();
        queryMap.put("stockTakingId",stockTakingId);
        queryMap.put("round",roundTime);
        List<StockTakingDetail> details=stockTakingService.getDetailListByRound(stockTakingId,roundTime);
        for (StockTakingDetail stockTakingDetail:details){
            stockTakingDetail.setId(0L);
            BigDecimal qty=quantService.getQuantQtyByLocationIdAndItemId(stockTakingDetail.getLocationId(), stockTakingDetail.getItemId());
            stockTakingDetail.setTheoreticalQty(qty);
            stockTakingDetail.setRound(roundTime+1);
            detailList.add(stockTakingDetail);
        }
        stockTakingService.insertDetailList(detailList);
        this.createTask(head, detailList, roundTime + 1, head.getDueTime());
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
            detail.setItemId(quant.getItemId());
            detail.setRealItemId(quant.getItemId());
            detail.setRealSkuId(detail.getSkuId());
            detail.setTheoreticalQty(quant.getQty());
            detailList.add(detail);
            idx++;
        }
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
            mapQuery.put("locationList", locations);
            mapQuery.put("itemId", head.getItemId());
            mapQuery.put("lotId", head.getLotId());
            mapQuery.put("ownerId", head.getOwnerId());
            mapQuery.put("supplierId", head.getSupplierId());
            List<StockQuant> quantList = quantService.getQuants(mapQuery);

        if (head.getTakingType().equals(0L)) {
            return this.prepareDetailListByItem(quantList);
        }
        else {
            return this.prepareDetailListByLocation(locationList, quantList);
        }
    }

    public void doOne(String detailInfo) {
        StockTakingDetail detail = JSON.parseObject(detailInfo, StockTakingDetail.class);
        stockTakingService.updateDetail(detail);
    }

    private boolean chargeDifference(Long stockTakingId, Long round) {
        List<StockTakingDetail> details = stockTakingService.getDetailListByRound(stockTakingId, round);
        for (StockTakingDetail detail : details) {
            if (detail.getRealQty().compareTo(detail.getTheoreticalQty()) != 0) {
                return false;
            }
        }
       return true;
    }



    public void createTask(StockTakingHead head, List<StockTakingDetail> detailList,Long round,Long dueTime) throws BizCheckedException{
        List<TaskEntry> taskEntryList=new ArrayList<TaskEntry>();
        for(StockTakingDetail detail:detailList) {
            TaskInfo taskInfo = new TaskInfo();
            taskInfo.setPlanId(head.getTakingId());
            taskInfo.setDueTime(dueTime);
            taskInfo.setPlanner(head.getPlanner());
            taskInfo.setStatus(1L);
            taskInfo.setLocationId(detail.getLocationId());
            taskInfo.setSkuId(detail.getSkuId());
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


    public void confirm(Long stockTakingId) throws BizCheckedException{
        // 获取stockingHead
        // 如果是临时盘点, 直接调用confirmDifference
        // 计划盘点,
        //      如果ruund == 1, 发起新一轮盘点
        //      如果round == 2, 获取第一轮,第二轮的差异明细列表, 如果非空, 发起第三轮盘点
        //      如果round == 3, 直接调用confirmDiffence

        StockTakingHead head=stockTakingService.getHeadById(stockTakingId);
        if (head.getPlanType()==1) {
            this.confirmDifference(stockTakingId,1L);
        }else {
            Long times = stockTakingService.chargeTime(stockTakingId);
            if (times == 1) {
                this.createNextDetail(stockTakingId,times);
            } else {
                if (times == 2) {
                    boolean isSame=this.chargeDifference(stockTakingId,times);
                    if (isSame) {
                        this.confirmDifference(stockTakingId, times);
                    } else {
                        this.createNextDetail(stockTakingId, times);
                    }
                } else {
                    this.confirmDifference(stockTakingId, times);
                }
            }
        }
    }


    public void confirmDifference(Long stockTakingId ,long roundTime) {
        List<StockTakingDetail> detailList = stockTakingService.getDetailListByRound(stockTakingId, roundTime);
        List<StockMove> moveList = new ArrayList<StockMove>();
        for (StockTakingDetail detail : detailList) {
            if (detail.getSkuId().equals(detail.getRealSkuId())) {
                StockMove move = new StockMove();
                move.setTaskId(detail.getTakingId());
                move.setSkuId(detail.getSkuId());
                move.setStatus(TaskConstant.Done);
                if (detail.getTheoreticalQty().compareTo(detail.getRealQty()) < 0) {
                    move.setQty(detail.getRealQty().subtract(detail.getTheoreticalQty()));
                    move.setQtyDone(move.getQty());
                    move.setFromLocationId(detail.getLocationId());
                    move.setToLocationId(locationService.getInventoryLostLocationId());
                }
                else {
                    move.setQty(detail.getTheoreticalQty().subtract(detail.getRealQty()));
                    move.setQtyDone(move.getQty());
                    move.setFromLocationId(locationService.getInventoryLostLocationId());
                    move.setToLocationId(detail.getLocationId());
                }
                moveList.add(move);
            }
            else {
                StockMove moveWin= new StockMove();
                moveWin.setTaskId(detail.getTakingId());
                moveWin.setSkuId(detail.getSkuId());
                moveWin.setToLocationId(locationService.getInventoryLostLocationId());
                moveWin.setFromLocationId(detail.getLocationId());
                moveWin.setQty(detail.getRealQty());
                moveWin.setQtyDone(detail.getRealQty());
                moveList.add(moveWin);

                StockMove moveLoss= new StockMove();
                moveLoss.setTaskId(detail.getTakingId());
                moveLoss.setSkuId(detail.getSkuId());
                moveLoss.setFromLocationId(locationService.getInventoryLostLocationId());
                moveLoss.setToLocationId(detail.getLocationId());
                moveLoss.setQty(detail.getRealQty());
                moveLoss.setQtyDone(detail.getRealQty());
                moveList.add(moveLoss);
            }
        }
        moveService.create(moveList);

    }
    public String create(StockTakingHead head) throws BizCheckedException{
        List<StockTakingDetail> detailList = prepareDetailList(head);
        logger.info("detail:"+JSON.toJSONString(detailList));
        stockTakingService.create(head, detailList);
        logger.info("end create taking");
        logger.info("head:"+JSON.toJSONString(head));
        this.createTask(head, detailList, 1L, head.getDueTime());
        return JsonUtils.SUCCESS();
    }

}
