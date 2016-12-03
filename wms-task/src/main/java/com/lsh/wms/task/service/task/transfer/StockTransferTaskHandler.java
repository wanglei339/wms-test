package com.lsh.wms.task.service.task.transfer;

import com.lsh.wms.core.constant.LocationConstant;
import com.lsh.wms.core.constant.StockConstant;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.stock.StockMoveService;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.core.service.stock.StockSummaryService;
import com.lsh.wms.core.service.transfer.StockTransferTaskService;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.stock.StockDelta;
import com.lsh.wms.model.stock.StockMove;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.task.service.TaskRpcService;
import com.lsh.wms.task.service.handler.AbsTaskHandler;
import com.lsh.wms.task.service.handler.TaskHandlerFactory;
import com.lsh.wms.task.service.task.back.BackInStorageTaskHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.List;
import java.util.StringTokenizer;

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

    @Autowired
    private StockSummaryService stockSummaryService;

    @Autowired
    private StockMoveService stockMoveService;

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

        int type = 0;
        BaseinfoLocation toLocation = locationService.getLocation(taskInfo.getToLocationId());
        if (toLocation.getType().equals(LocationConstant.DEFECTIVE_AREA)
                || toLocation.getType().equals(LocationConstant.DEFECTIVE_BIN)) {
            type = StockConstant.TYPE_TO_DEFECT;
        } else if  ( toLocation.getType().equals(LocationConstant.BACK_AREA)
                || toLocation.getType().equals(LocationConstant.BACK_BIN)) {
            type = StockConstant.TYPE_TO_REFUND;
        } else if (locationService.getLocation(taskInfo.getFromLocationId()).equals(LocationConstant.MARKET_RETURN_AREA)) {
            type = StockConstant.TYPE_MARKET_RETURN;
        }

        if (0 != type) {
            StockDelta stockDelta = new StockDelta();
            stockDelta.setItemId(taskInfo.getItemId());
            stockDelta.setInhouseQty(BigDecimal.ZERO.subtract(taskInfo.getTaskEaQty()));
            if (type == StockConstant.TYPE_MARKET_RETURN) {
                stockDelta.setInhouseQty(taskInfo.getTaskEaQty());
            }
            stockDelta.setType(type);
            stockDelta.setBusinessId(taskId);
            stockSummaryService.changeStock(stockDelta);
        }
    }

    public void doneConcrete(Long taskId, List<StockMove> moveList){
        for(StockMove move : moveList){
            if(move.getMoveHole() == 1L){
                stockMoveService.moveWholeContainer(move.getFromContainerId(),
                        move.getTaskId(),
                        move.getOperator(),
                        move.getFromLocationId(),
                        move.getToLocationId());
            }else{
                stockMoveService.move(move);
            }
        }
        this.doneConcrete(taskId);
    }

    public void cancelConcrete(Long taskId) {
        TaskEntry taskEntry = taskRpcService.getTaskEntryById(taskId);
        TaskInfo taskInfo = taskEntry.getTaskInfo();
        locationService.unlockLocation(taskInfo.getToLocationId());
//        quantService.unReserve(taskInfo.getTaskId());
    }

//    public void calcPerformance(TaskInfo taskInfo) {
//        if (taskInfo.getPackName().equals("EA")) {
//            taskInfo.setTaskPackQty(taskInfo.getQtyDone().divide(taskInfo.getPackUnit(), 2, BigDecimal.ROUND_DOWN));
//            taskInfo.setTaskEaQty(taskInfo.getQtyDone());
//        } else {
//            taskInfo.setTaskPackQty(taskInfo.getQtyDone());
//            taskInfo.setTaskEaQty(taskInfo.getQtyDone().multiply(taskInfo.getPackUnit()).setScale(0, BigDecimal.ROUND_HALF_UP));
//        }
//    }
}
