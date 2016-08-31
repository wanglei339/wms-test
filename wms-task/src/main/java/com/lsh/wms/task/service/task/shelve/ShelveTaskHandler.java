package com.lsh.wms.task.service.task.shelve;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.wms.api.service.shelve.IShelveRpcService;
import com.lsh.wms.api.service.stock.IStockMoveRestService;
import com.lsh.wms.api.service.stock.IStockMoveRpcService;
import com.lsh.wms.core.constant.ContainerConstant;
import com.lsh.wms.core.constant.LocationConstant;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.service.container.ContainerService;
import com.lsh.wms.core.service.item.ItemLocationService;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.stock.StockLotService;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.model.baseinfo.BaseinfoContainer;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.baseinfo.BaseinfoItemLocation;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.shelve.ShelveTaskHead;
import com.lsh.wms.model.stock.StockLot;
import com.lsh.wms.model.stock.StockQuant;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.task.service.handler.AbsTaskHandler;
import com.lsh.wms.task.service.handler.TaskHandlerFactory;
import com.lsh.wms.core.service.shelve.ShelveTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fengkun on 16/7/25.
 */
@Component
public class ShelveTaskHandler extends AbsTaskHandler {
    @Autowired
    private TaskHandlerFactory handlerFactory;
    @Autowired
    private BaseTaskService baseTaskService;
    @Autowired
    private ShelveTaskService taskService;
    @Autowired
    private ContainerService containerService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private StockQuantService stockQuantService;
    @Autowired
    private StockLotService stockLotService;
    @Autowired
    private ItemLocationService itemLocationService;
    @Reference
    private IShelveRpcService iShelveRpcService;
    @Reference
    private IStockMoveRpcService iStockMoveRpcService;

    @PostConstruct
    public void postConstruct() {
        handlerFactory.register(TaskConstant.TYPE_SHELVE, this);
    }

    public void create(TaskEntry taskEntry) {
        Long containerId = taskEntry.getTaskInfo().getContainerId();
        // 检查容器信息
        if (containerId == null || containerId.equals("")) {
            throw new BizCheckedException("2030003");
        }
        // 检查该容器是否已创建过任务
        if (baseTaskService.checkTaskByContainerId(containerId)) {
            throw new BizCheckedException("2030008");
        }
        // 获取quant
        List<StockQuant> quants = stockQuantService.getQuantsByContainerId(containerId);
        if (quants.size() < 1) {
            throw new BizCheckedException("2030001");
        }
        StockQuant quant = quants.get(0);

        TaskInfo taskInfo = new TaskInfo();
        ShelveTaskHead taskHead = new ShelveTaskHead();

        ObjUtils.bean2bean(quant, taskInfo);
        ObjUtils.bean2bean(quant, taskHead);

        taskInfo.setType(TaskConstant.TYPE_SHELVE);
        taskInfo.setFromLocationId(quant.getLocationId());
        taskInfo.setQtyDone(quant.getQty());

        taskEntry.setTaskInfo(taskInfo);
        taskEntry.setTaskHead(taskHead);

        super.create(taskEntry);
    }

    public void createConcrete(TaskEntry taskEntry) throws BizCheckedException {
        ShelveTaskHead taskHead = (ShelveTaskHead) taskEntry.getTaskHead();
        Long taskId = taskEntry.getTaskInfo().getTaskId();
        taskHead.setTaskId(taskId);
        Long containerId = taskHead.getContainerId();
        BaseinfoContainer container = containerService.getContainer(containerId);
        if (container == null) {
            throw new BizCheckedException("2030004");
        }
        List<StockQuant> quants = stockQuantService.getQuantsByContainerId(containerId);
        if (quants.size() < 1) {
            throw new BizCheckedException("2030001");
        }
        StockQuant quant = quants.get(0);
        // 获取目标location
        BaseinfoLocation targetLocation = iShelveRpcService.assginShelveLocation(container, taskEntry.getTaskInfo().getSubType(), taskId);
        if (targetLocation == null) {
            throw new BizCheckedException("2030005");
        }
        Long lotId = quant.getLotId();
        // 获取批次信息
        StockLot stockLot = stockLotService.getStockLotByLotId(lotId);
        taskHead.setReceiptId(stockLot.getReceiptId());
        taskHead.setOrderId(stockLot.getPoId());
        taskHead.setSkuId(quant.getSkuId());
        taskHead.setOwnerId(quant.getOwnerId());
        taskHead.setLotId(lotId);
        taskHead.setSupplierId(quant.getSupplierId());
        taskHead.setAllocLocationId(targetLocation.getLocationId());
        taskService.create(taskHead);
        // 锁location
        locationService.lockLocation(targetLocation.getFatherId());
    }

    public void assignConcrete(Long taskId, Long staffId) throws BizCheckedException{
        TaskInfo taskInfo = baseTaskService.getTaskInfoById(taskId);
        ShelveTaskHead taskHead = taskService.getShelveTaskHead(taskId);
        if (taskHead == null) {
            throw new BizCheckedException("2030009");
        }
        taskService.assign(taskId, staffId);
        // move到仓库location_id
        iStockMoveRpcService.moveWholeContainer(taskHead.getContainerId(), taskId, staffId, taskInfo.getFromLocationId(), locationService.getWarehouseLocationId());
    }

    public void doneConcrete(Long taskId, Long locationId) throws BizCheckedException{
        ShelveTaskHead taskHead = taskService.getShelveTaskHead(taskId);
        if (taskHead == null) {
            throw new BizCheckedException("2030009");
        }
        BaseinfoLocation realLocation = locationService.getLocation(locationId);
        // 实际上架位置和分配位置不一致
        if (!locationId.equals(taskHead.getAllocLocationId())) {
            if (realLocation == null) {
                throw new BizCheckedException("2030006");
            }
            // 检查位置锁定状态
            if (locationService.checkLocationLockStatus(locationId)) {
                throw new BizCheckedException("2030011");
            }
            // 检查位置使用状态
            if (realLocation.getCanUse().equals(0)) {
                throw new BizCheckedException("2030007");
            }
        }
        // move到目标location_id
        // 地堆区需要合盘
        if (realLocation.getType().equals(LocationConstant.FLOOR)) {
            List<StockQuant> quants = stockQuantService.getQuantsByLocationId(locationId);
            Long containerId = 0L;
            if (quants.isEmpty()) {
                containerId = containerService.createContainerByType(ContainerConstant.PALLET).getContainerId();
            } else {
                containerId = quants.get(0).getContainerId();
            }
            iStockMoveRpcService.moveWholeContainer(taskHead.getContainerId(), containerId, taskId, taskHead.getOperator(), locationService.getWarehouseLocationId(), locationId);
        } else {
            iStockMoveRpcService.moveWholeContainer(taskHead.getContainerId(), taskId, taskHead.getOperator(), locationService.getWarehouseLocationId(), locationId);
        }
        taskService.done(taskId, locationId);
        // 释放分配的location
        locationService.unlockLocation(taskHead.getAllocLocationId());
    }

    public void getConcrete(TaskEntry taskEntry) {
        taskEntry.setTaskHead(taskService.getShelveTaskHead(taskEntry.getTaskInfo().getTaskId()));
    }

    public void getHeadConcrete(TaskEntry taskEntry) {
        taskEntry.setTaskHead(taskService.getShelveTaskHead(taskEntry.getTaskInfo().getTaskId()));
    }
}
