package com.lsh.wms.task.service.task.procurement;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsh.wms.api.service.stock.IStockQuantRpcService;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.model.stock.StockQuant;
import com.lsh.wms.model.stock.StockQuantCondition;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.task.service.handler.AbsTaskHandler;
import com.lsh.wms.task.service.handler.TaskHandlerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by mali on 16/8/2.
 */
@Component
public class ProcurementService extends AbsTaskHandler {
    @Autowired
    private TaskHandlerFactory handlerFactory;
    @Autowired
    private BaseTaskService baseTaskService;
    @Reference
    private IStockQuantRpcService stockQuantService;
    @Autowired
    private StockQuantService quantService;

    @PostConstruct
    public void postConstruct() {
        handlerFactory.register(TaskConstant.TYPE_PROCUREMENT, this);
    }

    public void calcPerformance(TaskInfo taskInfo) {
        if(taskInfo.getSubType().compareTo(1L)==0){
            taskInfo.setTaskQty(BigDecimal.ONE);
        }else {
            taskInfo.setTaskQty(taskInfo.getQtyDone());
        }

    }
    public void cancelConcrete(Long taskId) {

        StockQuantCondition condition = new StockQuantCondition();
        TaskInfo info = baseTaskService.getTaskInfoById(taskId);
        condition.setLocationId(info.getFromLocationId());
        condition.setItemId(info.getItemId());
        List<StockQuant> quants = stockQuantService.getQuantList(condition);
        for(StockQuant quant:quants){
            quant.setReserveTaskId(0L);
            quantService.update(quant);
        }
    }
    public void createConcrete(TaskEntry taskEntry) {
        StockQuantCondition condition = new StockQuantCondition();
        TaskInfo info = taskEntry.getTaskInfo();
        condition.setLocationId(info.getFromLocationId());
        condition.setItemId(info.getItemId());
        List<StockQuant> quants = stockQuantService.getQuantList(condition);
        for(StockQuant quant:quants){
            quant.setReserveTaskId(info.getTaskId());
            quantService.update(quant);
        }
    }
}
