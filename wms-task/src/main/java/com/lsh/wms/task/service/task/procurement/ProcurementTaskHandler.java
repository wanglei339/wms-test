package com.lsh.wms.task.service.task.procurement;

import com.lsh.base.common.utils.ObjUtils;
import com.lsh.wms.core.constant.ContainerConstant;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.service.container.ContainerService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.stock.StockMoveService;
import com.lsh.wms.core.service.stock.StockQuantService;
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
    private StockQuantService quantService;
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
        taskInfo.setTaskPackQty(taskInfo.getQty().divide(taskInfo.getPackUnit(),2,BigDecimal.ROUND_HALF_DOWN));
        taskInfo.setTaskEaQty(taskInfo.getQty());
        taskInfo.setQtyDone(taskInfo.getQty());
    }
    public void doneConcrete(Long taskId){
        TaskInfo info = baseTaskService.getTaskByTaskId(taskId);
        Long fromLocationId = locationService.getFatherRegionBySonId(info.getFromLocationId()).getLocationId();
        if (info.getSubType().compareTo(1L) == 0) {
            info.setQty(quantService.getQuantQtyByContainerId(info.getContainerId()));
            baseTaskService.update(info);
        }
        StockMove move = new StockMove();
        ObjUtils.bean2bean(info, move);

        move.setQty(info.getQty());
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
