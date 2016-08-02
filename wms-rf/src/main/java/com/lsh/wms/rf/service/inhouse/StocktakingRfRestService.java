package com.lsh.wms.rf.service.inhouse;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.api.service.inhouse.IStockTakingRfRestService;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.constant.CsiConstan;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.service.csi.CsiSkuService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.stock.StockMoveService;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.core.service.taking.StockTakingService;
import com.lsh.wms.model.csi.CsiSku;
import com.lsh.wms.model.stock.StockMove;
import com.lsh.wms.model.taking.StockTakingDetail;
import com.lsh.wms.model.taking.StockTakingHead;
import com.lsh.wms.model.task.StockTakingTask;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskInfo;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wuhao on 16/7/30.
 */
@Service(protocol = "rest")
@Path("inhouse/stock_taking")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class StocktakingRfRestService implements IStockTakingRfRestService {
    @Reference
    private ITaskRpcService iTaskRpcService;
    @Autowired
    private StockQuantService quantService;
    @Autowired
    private StockTakingService stockTakingService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private StockMoveService moveService;
    @Autowired
    private CsiSkuService skuService;


    @GET
    @Path("doOne")
    public String doOne() throws BizCheckedException {
        //Long taskId,int qty,String barcode
        Map request = RequestUtils.getRequest();
        List<Map> resultList = (List)(request.get("resultList"));
        if (resultList == null || resultList.size() == 0) {
            return JsonUtils.EXCEPTION_ERROR("参数错误");
        }
        Long taskId = Long.parseLong(resultList.get(0).get("taskId").toString());
        TaskEntry entry = iTaskRpcService.getTaskEntryById(taskId);
        StockTakingHead head = (StockTakingHead)(entry.getTaskHead());
        StockTakingDetail detail = (StockTakingDetail) (entry.getTaskDetailList().get(0));
        for(Map<String,Object> beanMap:resultList){
            Object barcode = beanMap.get("barcode");
            BigDecimal realQty = new BigDecimal(beanMap.get("qty").toString());
            if(barcode==null ||barcode =="") {
                BigDecimal qty = quantService.getQuantQtyByLocationIdAndItemId(detail.getLocationId(), detail.getItemId());
                detail.setTheoreticalQty(qty);
                detail.setRealQty(realQty);
                stockTakingService.updateDetail(detail);
                iTaskRpcService.done(taskId);
            }else {
                StockTakingDetail newDetail = new StockTakingDetail();
                CsiSku csiSku = skuService.getSkuByCode(CsiConstan.CSI_CODE_TYPE_BARCODE,barcode.toString());
                newDetail.setRealQty(realQty);
                newDetail.setTakingId(head.getTakingId());
                newDetail.setTaskId(taskId);
                newDetail.setRealSkuId(csiSku.getSkuId());
                newDetail.setLocationId(detail.getLocationId());
                newDetail.setContainerId(detail.getContainerId());
                newDetail.setRound(detail.getRound());
                stockTakingService.insertDetail(newDetail);
            }
            Map<String,Object> queryMap = new HashMap<String, Object>();
            queryMap.put("planId",head.getTakingId());
            queryMap.put("status",1L);
            List<TaskEntry> entries = iTaskRpcService.getTaskHeadList(TaskConstant.TYPE_STOCK_TAKING,queryMap);
            if(entries==null || entries.size()==0){
                this.confirm(head.getTakingId());
            }
        }

        return JsonUtils.SUCCESS();
    }

    @GET
    @Path("getTask")
    public String getTaskInfo(@QueryParam("taskId") Long taskId) throws BizCheckedException{
        TaskEntry entry = iTaskRpcService.getTaskEntryById(taskId);
        StockTakingDetail detail = (StockTakingDetail)(entry.getTaskDetailList().get(0));
        StockTakingTask task = (StockTakingTask)(entry.getTaskHead());
        StockTakingHead head = stockTakingService.getHeadById(task.getTakingId());
        BigDecimal qty = quantService.getQuantQtyByLocationIdAndItemId(detail.getLocationId(),detail.getItemId());
        detail.setTheoreticalQty(qty);
        stockTakingService.updateDetail(detail);
        Map<String,Object> result = new HashMap<String, Object>();
        //TODO 返回rf枪所需的字段值。
        result.put("viewType",head.getViewType());
        result.put("taskId",taskId);
        result.put("locationId",detail.getLocationId());
        result.put("itemId",detail.getItemId());
        result.put("qty",qty);
        return JsonUtils.SUCCESS(result);

    }

    public void confirm(Long stockTakingId) throws BizCheckedException {
        // 获取stockingHead
        // 如果是临时盘点, 直接调用confirmDifference
        // 计划盘点,
        //      如果ruund == 1, 发起新一轮盘点
        //      如果round == 2, 获取第一轮,第二轮的差异明细列表, 如果非空, 发起第三轮盘点
        //      如果round == 3, 直接调用confirmDiffence

        StockTakingHead head = stockTakingService.getHeadById(stockTakingId);
        if (head.getPlanType() == 1) {
            this.confirmDifference(stockTakingId, 1L);
        } else {
            Long times = stockTakingService.chargeTime(stockTakingId);
            if (times == 1) {
                this.createNextDetail(stockTakingId, times);
            } else {
                if (times == 2) {
                    boolean isSame = this.chargeDifference(stockTakingId, times);
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


    public void confirmDifference(Long stockTakingId, long roundTime) {
        List<StockTakingDetail> detailList = stockTakingService.getDetailListByRound(stockTakingId, roundTime);
        StockTakingHead head = stockTakingService.getHeadById(stockTakingId);
        head.setStatus(3L);
        stockTakingService.updateHead(head);
        List<StockMove> moveList = new ArrayList<StockMove>();
        for (StockTakingDetail detail : detailList) {
            if (detail.getSkuId().equals(detail.getRealSkuId())) {
                StockMove move = new StockMove();
                move.setTaskId(detail.getTakingId());
                move.setSkuId(detail.getSkuId());
                move.setItemId(detail.getItemId());
                move.setStatus(TaskConstant.Done);
                if (detail.getTheoreticalQty().compareTo(detail.getRealQty()) < 0) {
                    move.setQty(detail.getRealQty().subtract(detail.getTheoreticalQty()));
                    move.setFromLocationId(detail.getLocationId());
                    move.setToLocationId(locationService.getInventoryLostLocationId());
                } else {
                    move.setQty(detail.getTheoreticalQty().subtract(detail.getRealQty()));
                    move.setFromLocationId(locationService.getInventoryLostLocationId());
                    move.setToLocationId(detail.getLocationId());
                }
                moveList.add(move);
            } else {
                StockMove moveWin = new StockMove();
                moveWin.setTaskId(detail.getTakingId());
                moveWin.setSkuId(detail.getSkuId());
                moveWin.setToLocationId(locationService.getInventoryLostLocationId());
                moveWin.setFromLocationId(detail.getLocationId());
                moveWin.setQty(detail.getRealQty());
                moveList.add(moveWin);

                StockMove moveLoss = new StockMove();
                moveLoss.setTaskId(detail.getTakingId());
                moveLoss.setSkuId(detail.getSkuId());
                moveLoss.setFromLocationId(locationService.getInventoryLostLocationId());
                moveLoss.setToLocationId(detail.getLocationId());
                moveLoss.setQty(detail.getRealQty());
                moveList.add(moveLoss);
            }
        }
        moveService.create(moveList);

    }

    private boolean chargeDifference(Long stockTakingId, Long round) {
        List<StockTakingDetail> oldDetails =stockTakingService.getDetailListByRound(stockTakingId, round - 1);
        List<StockTakingDetail> details = stockTakingService.getDetailListByRound(stockTakingId, round);
        Map<String,BigDecimal> compareMap = new HashMap<String, BigDecimal>();
        for(StockTakingDetail detail:oldDetails){
            String key = "l:"+detail.getLocationId()+"i:"+detail.getItemId();
            compareMap.put(key,detail.getRealQty().subtract(detail.getTheoreticalQty()));
        }
        for (StockTakingDetail detail : details) {
            String key = "l:"+detail.getLocationId()+"i:"+detail.getItemId();
            BigDecimal differQty = detail.getRealQty().subtract(detail.getTheoreticalQty());
            if(!compareMap.containsKey(key) || compareMap.get(key).compareTo(differQty)!=0) {
                return false;
            }
        }
        return true;
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
            stockTakingDetail.setRound(roundTime + 1);
            detailList.add(stockTakingDetail);
        }
        stockTakingService.insertDetailList(detailList);
        this.createTask(head, detailList, roundTime + 1, head.getDueTime());
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

}
