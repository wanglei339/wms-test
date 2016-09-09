package com.lsh.wms.task.service.task.transfer;

import com.lsh.wms.core.constant.LocationConstant;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.core.service.transfer.StockTransferTaskService;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.task.service.TaskRpcService;
import com.lsh.wms.task.service.handler.AbsTaskHandler;
import com.lsh.wms.task.service.handler.TaskHandlerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;

/**
 * Created by mali on 16/7/25.
 */
@Component
public class StockTransferTaskHandler extends AbsTaskHandler {
    @Autowired
    private StockTransferTaskService stockTransferTaskService;

    @Autowired
    private TaskHandlerFactory handlerFactory;

    @Autowired
    private StockQuantService quantService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private TaskRpcService taskRpcService;

    @PostConstruct
    public void postConstruct() {
        handlerFactory.register(TaskConstant.TYPE_STOCK_TRANSFER, this);
    }

    public void createConcrete(TaskEntry taskEntry) {
        TaskInfo taskInfo = taskEntry.getTaskInfo();
        Long locationType = locationService.getLocation(taskInfo.getToLocationId()).getType();
        if (!locationType.equals(LocationConstant.DEFECTIVE_AREA) && !locationType.equals(LocationConstant.BACK_AREA)) {
            locationService.lockLocation(taskInfo.getToLocationId());
        }
//        Map<String, Object> mapQuery = new HashMap<String, Object>();
//        mapQuery.put("locationId", taskInfo.getFromLocationId());
//        mapQuery.put("itemId",taskInfo.getItemId());
//        mapQuery.put("reserveTaskId", 0L);
//        quantService.reserve(mapQuery,taskInfo.getTaskId(),taskInfo.getQty().multiply(taskInfo.getPackUnit()).setScale(0, BigDecimal.ROUND_HALF_UP));
    }

    public void doneConcrete(Long taskId) {
        TaskEntry taskEntry = taskRpcService.getTaskEntryById(taskId);
        TaskInfo taskInfo = taskEntry.getTaskInfo();
        locationService.unlockLocation(taskInfo.getToLocationId());
//        quantService.unReserve(taskInfo.getTaskId());
    }

    public void cancelConcrete(Long taskId) {
        TaskEntry taskEntry = taskRpcService.getTaskEntryById(taskId);
        TaskInfo taskInfo = taskEntry.getTaskInfo();
        locationService.unlockLocation(taskInfo.getToLocationId());
//        quantService.unReserve(taskInfo.getTaskId());
    }

    public void calcPerformance(TaskInfo taskInfo) {
        if (taskInfo.getPackName().equals("EA")) {
            taskInfo.setTaskPackQty(taskInfo.getQty().divide(taskInfo.getPackUnit(), 2 , BigDecimal.ROUND_DOWN));
            taskInfo.setTaskEaQty(taskInfo.getQty());
        } else {
            taskInfo.setTaskPackQty(taskInfo.getQty());
            taskInfo.setTaskEaQty(taskInfo.getQty().multiply(taskInfo.getPackUnit()).setScale(0, BigDecimal.ROUND_HALF_UP));
        }
    }
}
