package com.lsh.wms.task.service.task.taking;

import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.service.taking.StockTakingService;
import com.lsh.wms.core.service.task.StockTakingTaskService;
import com.lsh.wms.model.taking.StockTakingDetail;
import com.lsh.wms.model.task.StockTakingTask;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.task.service.handler.AbsTaskHandler;
import com.lsh.wms.task.service.handler.TaskHandlerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

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
    private TaskHandlerFactory handlerFactory;

    @PostConstruct
    public void postConstruct() {
        handlerFactory.register(TaskConstant.TYPE_STOCK_TAKING, this);
    }

    protected void createConcrete(TaskEntry taskEntry) {
        StockTakingTask task = (StockTakingTask) taskEntry.getTaskHead();
        Long taskId=taskEntry.getTaskInfo().getTaskId();
        task.setTaskId(taskId);
        List<Object> stockTakingDetails = taskEntry.getTaskDetailList();
        for(Object detail:stockTakingDetails){
            StockTakingDetail stockTakingDetail =(StockTakingDetail)detail;
            stockTakingDetail.setTaskId(taskId);
            stockTakingService.updateDetail(stockTakingDetail);
        }
        stockTakingTaskService.create(task);
    }
    protected void getConcrete(TaskEntry taskEntry) {
        taskEntry.setTaskHead(stockTakingTaskService.getTakingTaskByTaskId(taskEntry.getTaskInfo().getTaskId()));
        taskEntry.setTaskDetailList((List<Object>) (List<?>) stockTakingService.getDetailByTaskId(taskEntry.getTaskInfo().getTaskId()));
    }
    protected void getHeadConcrete(TaskEntry taskEntry) {
        taskEntry.setTaskHead(stockTakingTaskService.getTakingTaskByTaskId(taskEntry.getTaskInfo().getTaskId()));
    }


}
