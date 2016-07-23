package com.lsh.wms.task.handler;

import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.service.task.StockTakingTaskService;
import com.lsh.wms.model.task.StockTakingTask;
import com.lsh.wms.model.task.TaskEntry;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * Created by mali on 16/7/20.
 */

public class StockTakingTaskHandler extends AbsTaskHandler {
    @Autowired
    private StockTakingTaskService stockTakingTaskService;

    @Autowired
    private TaskHandlerFactory handlerFactory;

    @PostConstruct
    public void postConstruct() {
        handlerFactory.register(TaskConstant.TYPE_STOCK_TAKING, this);
    }

    protected void createConcrete(TaskEntry<?,?> taskEntry) {
        StockTakingTask task = (StockTakingTask) taskEntry.getTaskHead();
        stockTakingTaskService.create(task);
    }

}
