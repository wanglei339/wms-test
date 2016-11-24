package com.lsh.wms.task.service.task.taking;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.core.service.taking.StockTakingService;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.core.service.task.StockTakingTaskService;
import com.lsh.wms.model.taking.StockTakingDetail;
import com.lsh.wms.model.taking.StockTakingHead;
import com.lsh.wms.model.task.StockTakingTask;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.task.service.handler.AbsTaskHandler;
import com.lsh.wms.task.service.handler.TaskHandlerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mali on 16/7/20.
 */
@Component
public class StockTakingTaskHandler extends AbsTaskHandler {
    @Autowired
    private StockTakingTaskService stockTakingTaskService;
    @Autowired
    private StockTakingService stockTakingService;
    @Autowired
    private BaseTaskService baseTaskService;
    @Autowired
    private StockQuantService quantService;

    @Autowired
    private TaskHandlerFactory handlerFactory;

    @Reference
    private ITaskRpcService iTaskRpcService;

    @PostConstruct
    public void postConstruct() {
        handlerFactory.register(TaskConstant.TYPE_STOCK_TAKING, this);
    }

    public void createConcrete(TaskEntry taskEntry) {
        StockTakingTask task = (StockTakingTask) taskEntry.getTaskHead();
        Long taskId=taskEntry.getTaskInfo().getTaskId();
        task.setTaskId(taskId);
        List<Object> stockTakingDetails = taskEntry.getTaskDetailList();
        for(Object detail:stockTakingDetails){
            StockTakingDetail stockTakingDetail =(StockTakingDetail)detail;
            stockTakingDetail.setTaskId(taskId);
            stockTakingDetail.setTakingId(task.getTakingId());
            stockTakingService.insertDetail(stockTakingDetail);
        }
        stockTakingTaskService.create(task);
    }
    public void getConcrete(TaskEntry taskEntry) {
        taskEntry.setTaskHead(stockTakingTaskService.getTakingTaskByTaskId(taskEntry.getTaskInfo().getTaskId()));
        taskEntry.setTaskDetailList((List<Object>) (List<?>) stockTakingService.getDetailByTaskId(taskEntry.getTaskInfo().getTaskId()));
    }
    public void getHeadConcrete(TaskEntry taskEntry) {
        taskEntry.setTaskHead(stockTakingTaskService.getTakingTaskByTaskId(taskEntry.getTaskInfo().getTaskId()));
    }
    public void assignConcrete(Long taskId, Long staffId) throws BizCheckedException {
        StockTakingTask stockTakingTask = stockTakingTaskService.getTakingTaskByTaskId(taskId);
        List<StockTakingDetail> details = stockTakingService.getDetailByTaskId(taskId);
        if(details!=null && details.size()!=0){
            StockTakingDetail detail = details.get(0);
            detail.setOperator(staffId);
            stockTakingService.updateDetail(detail);
        }
        if(stockTakingTask!=null) {
            StockTakingHead head = stockTakingService.getHeadById(stockTakingTask.getTakingId());
            head.setStatus(2L);
            head.setUpdatedAt(DateUtils.getCurrentSeconds());
            stockTakingService.updateHead(head);
        }
    }
    public void doneConcrete(Long taskId) {
        TaskEntry entry = iTaskRpcService.getTaskEntryById(taskId);
        StockTakingTask task = (StockTakingTask) (entry.getTaskHead());
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("planId", task.getTakingId());
        queryMap.put("status", 2L);
        List<TaskEntry> entries = iTaskRpcService.getTaskHeadList(TaskConstant.TYPE_STOCK_TAKING, queryMap);
        if (entries == null || entries.size() == 0) {
            this.confirm(task.getTakingId());
        }
    }
    public void cancelConcrete(Long taskId) {
        StockTakingTask task = stockTakingTaskService.getTakingTaskByTaskId(taskId);
        task.setIsValid(0);
        stockTakingTaskService.updateTakingTask(task);
        Map<String,Object> queryMap = new HashMap<String, Object>();
        queryMap.put("takingId", task.getTakingId());
        queryMap.put("taskId" , task.getTaskId());
        List<StockTakingDetail> details= stockTakingService.queryTakingDetail(queryMap);
        for(StockTakingDetail detail:details){
            detail.setIsValid(0);
            stockTakingService.updateDetail(detail);
        }
    }
    public void createNextDetail(Long stockTakingId,Long roundTime) throws BizCheckedException {
        Map<String,Object> queryMap = new HashMap();
        StockTakingHead head = stockTakingService.getHeadById(stockTakingId);
        List<StockTakingDetail> detailList = new ArrayList<StockTakingDetail>();
        queryMap.put("stockTakingId",stockTakingId);
        queryMap.put("round", roundTime);
        queryMap.put("isValid", 1);
        List<StockTakingDetail> details=stockTakingService.getDetailListByRound(stockTakingId, roundTime);
        for (StockTakingDetail stockTakingDetail:details){
            //判断是否是盘点时多出来的商品。。。如果是，则不生成下次盘点的detail
            if(stockTakingDetail.getItemId()==0){
                continue;
            }

            stockTakingDetail.setId(0L);
            BigDecimal qty=quantService.getQuantQtyByLocationIdAndItemId(stockTakingDetail.getLocationId(), stockTakingDetail.getItemId());
            stockTakingDetail.setTheoreticalQty(qty);
            stockTakingDetail.setOperator(0L);
            stockTakingDetail.setRealQty(BigDecimal.ZERO);
            stockTakingDetail.setRound(roundTime + 1);
            detailList.add(stockTakingDetail);
        }
        this.createTask(head, detailList, roundTime + 1, head.getDueTime());
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
            stockTakingService.confirmDifference(stockTakingId, 1L);
        } else {
            Long times = stockTakingService.chargeTime(stockTakingId);
            if (times == 1) {
                this.createNextDetail(stockTakingId, times);
            } else {
                if (times == 2) {
                    boolean isSame = this.chargeDifference(stockTakingId, times);
                    if (isSame) {
                        stockTakingService.confirmDifference(stockTakingId, times);
                    } else {
                        this.createNextDetail(stockTakingId, times);
                    }
                } else {
                    stockTakingService.confirmDifference(stockTakingId, times);
                }
            }
        }
    }
    private boolean chargeDifference(Long stockTakingId, Long round) {
        List<StockTakingDetail> oldDetails =stockTakingService.getDetailListByRound(stockTakingId, round - 1);
        List<StockTakingDetail> details = stockTakingService.getDetailListByRound(stockTakingId, round);
        Map<String,BigDecimal> compareMap = new HashMap<String, BigDecimal>();
        if(details.size()!=oldDetails.size()){
            return false;
        }
        for(StockTakingDetail detail:oldDetails){
            String key = "l:"+detail.getLocationId()+"i:"+detail.getItemId();
            compareMap.put(key,detail.getRealQty().subtract(detail.getTheoreticalQty()));
        }
        for (StockTakingDetail detail : details) {
            String key = "l:"+detail.getLocationId()+"i:"+detail.getItemId();
            BigDecimal differQty = detail.getRealQty().subtract(detail.getTheoreticalQty());
            if((!compareMap.containsKey(key)) || compareMap.get(key).compareTo(differQty)!=0) {
                return false;
            }
        }
        return true;
    }
    public void createTask(StockTakingHead head, List<StockTakingDetail> detailList,Long round,Long dueTime) throws BizCheckedException {
        List<TaskEntry> taskEntryList=new ArrayList<TaskEntry>();
        for(StockTakingDetail detail:detailList) {
            TaskInfo taskInfo = new TaskInfo();
            taskInfo.setPlanId(head.getTakingId());
            taskInfo.setTaskName("盘点任务["+taskInfo.getLocationId()+"]");
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
