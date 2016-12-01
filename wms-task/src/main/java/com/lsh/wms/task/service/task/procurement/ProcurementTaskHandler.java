package com.lsh.wms.task.service.task.procurement;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.wms.api.service.stock.IStockMoveRpcService;
import com.lsh.wms.core.constant.ContainerConstant;
import com.lsh.wms.core.constant.LocationConstant;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.service.container.ContainerService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.stock.StockMoveService;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.model.stock.StockMove;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.task.service.handler.AbsTaskHandler;
import com.lsh.wms.task.service.handler.TaskHandlerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;

/**
 * Created by mali on 16/8/2.
 */
@Component
public class ProcurementTaskHandler extends AbsTaskHandler {
    @Autowired
    private TaskHandlerFactory handlerFactory;
    @Autowired
    private StockMoveService moveService;
    @Autowired
    private BaseTaskService baseTaskService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private ContainerService containerService;

    @PostConstruct
    public void postConstruct() {
        handlerFactory.register(TaskConstant.TYPE_PROCUREMENT, this);
    }

    public void calcPerformance(TaskInfo taskInfo) {

        taskInfo.setTaskPackQty(taskInfo.getQtyDone());
        taskInfo.setTaskEaQty(taskInfo.getQtyDone().multiply(taskInfo.getPackUnit()));
    }
    public void doneConcrete(Long taskId){
        TaskInfo info = baseTaskService.getTaskByTaskId(taskId);
        Long fromLocationId = locationService.getWarehouseLocation().getLocationId();
        if (info.getSubType().compareTo(1L) == 0) {
            moveService.moveWholeContainer(info.getContainerId(), taskId, info.getOperator(), fromLocationId, info.getToLocationId());
        }else {
            StockMove move = new StockMove();
            ObjUtils.bean2bean(info, move);
            if (info.getSubType().compareTo(2L) == 0) {
                move.setQty(info.getQtyDone().multiply(info.getPackUnit()).setScale(0, BigDecimal.ROUND_HALF_UP));
            }
            move.setFromLocationId(fromLocationId);
            move.setToLocationId(info.getToLocationId());
            Long newContainerId = containerService.createContainerByType(ContainerConstant.PALLET).getContainerId();
            Long toContainerId= containerService.getContaierIdByLocationId(info.getToLocationId());
            if (toContainerId == null || toContainerId.equals(0L)) {
                toContainerId = newContainerId;
            }
            move.setFromContainerId(info.getContainerId());
            move.setToContainerId(toContainerId);
            move.setSkuId(info.getSkuId());
            move.setOwnerId(info.getOwnerId());
            moveService.move(move);
        }
    }
}
