package com.lsh.wms.task.service.task.taking;

import com.lsh.wms.core.constant.TaskConstant;
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
    private TaskHandlerFactory handlerFactory;

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
    public void assignConcrete(Long taskId, Long staffId) {
        StockTakingTask task = stockTakingTaskService.getTakingTaskByTaskId(taskId);
        StockTakingHead head = stockTakingService.getHeadById(task.getTakingId());
        head.setStatus(2L);
        stockTakingService.updateHead(head);
    }
    public void cancel(Long taskId) {
        StockTakingTask task = stockTakingTaskService.getTakingTaskByTaskId(taskId);
        task.setIsValid(0);
        stockTakingTaskService.updateTakingTask(task);
        Map<String,Object> queryMap = new HashMap<String, Object>();
        queryMap.put("takingId", task.getTakingId());
        List<StockTakingDetail> details= stockTakingService.queryTakingDetail(queryMap);
        for(StockTakingDetail detail:details){
            detail.setIsValid(0);
            baseTaskService.cancel(taskId,this);
            stockTakingService.updateDetail(detail);
        }
    }


}
