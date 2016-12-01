package com.lsh.wms.task.service.task.shelve;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.wms.api.service.shelve.IShelveRpcService;
import com.lsh.wms.api.service.stock.IStockMoveRpcService;
import com.lsh.wms.core.constant.*;
import com.lsh.wms.core.service.container.ContainerService;
import com.lsh.wms.core.service.item.ItemLocationService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.stock.StockLotService;
import com.lsh.wms.core.service.stock.StockMoveService;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.core.service.stock.StockSummaryService;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.model.baseinfo.BaseinfoContainer;
import com.lsh.wms.model.baseinfo.BaseinfoItemLocation;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.shelve.ShelveTaskHead;
import com.lsh.wms.model.stock.StockDelta;
import com.lsh.wms.model.stock.StockLot;
import com.lsh.wms.model.stock.StockQuant;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.task.service.handler.AbsTaskHandler;
import com.lsh.wms.task.service.handler.TaskHandlerFactory;
import com.lsh.wms.core.service.shelve.ShelveTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    private static final Logger logger = LoggerFactory.getLogger(ShelveTaskHandler.class);
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
    private StockMoveService stockMoveService;
    @Autowired
    private ItemLocationService itemLocationService;
    @Reference
    private IShelveRpcService iShelveRpcService;
    @Reference
    private IStockMoveRpcService iStockMoveRpcService;
    @Autowired
    private StockSummaryService stockSummaryService;

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
        Long lotId = quant.getLotId();
        // 获取批次信息
        StockLot stockLot = stockLotService.getStockLotByLotId(lotId);

        ObjUtils.bean2bean(quant, taskInfo);
        ObjUtils.bean2bean(quant, taskHead);

        taskInfo.setType(TaskConstant.TYPE_SHELVE);
        taskInfo.setFromLocationId(quant.getLocationId());
        taskInfo.setQtyDone(quant.getQty());
        taskInfo.setOrderId(stockLot.getPoId());

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
        Long lotId = quant.getLotId();
        // 获取批次信息
        StockLot stockLot = stockLotService.getStockLotByLotId(lotId);
        taskHead.setReceiptId(stockLot.getReceiptId());
        taskHead.setOrderId(stockLot.getPoId());
        taskHead.setSkuId(quant.getSkuId());
        taskHead.setOwnerId(quant.getOwnerId());
        taskHead.setLotId(lotId);
        taskHead.setSupplierId(quant.getSupplierId());
        taskService.create(taskHead);
    }

    public void assignConcrete(Long taskId, Long staffId) throws BizCheckedException{
        TaskInfo taskInfo = baseTaskService.getTaskInfoById(taskId);
        ShelveTaskHead taskHead = taskService.getShelveTaskHead(taskId);
        if (taskHead == null) {
            throw new BizCheckedException("2030009");
        }
        Long containerId = taskHead.getContainerId();
        BaseinfoContainer container = containerService.getContainer(containerId);
        // 获取目标location
        BaseinfoLocation targetLocation = iShelveRpcService.assginShelveLocation(container, taskInfo.getSubType(), taskId);
        if (targetLocation == null) {
            throw new BizCheckedException("2030005");
        }
        taskService.assign(taskId, staffId, targetLocation.getLocationId());
        // 锁location
        locationService.lockLocation(targetLocation.getLocationId());
        // move到仓库location_id
        stockMoveService.moveWholeContainer(taskHead.getContainerId(), taskId, staffId, taskInfo.getFromLocationId(), locationService.getWarehouseLocation().getLocationId());
        //iStockMoveRpcService.moveWholeContainer(taskHead.getContainerId(), taskId, staffId, taskInfo.getFromLocationId(), locationService.getWarehouseLocationId());
    }

    public void doneConcrete(Long taskId, Long locationId) throws BizCheckedException{
        ShelveTaskHead taskHead = taskService.getShelveTaskHead(taskId);
        TaskInfo taskInfo = baseTaskService.getTaskInfoById(taskId);
        if (taskHead == null) {
            throw new BizCheckedException("2030009");
        }
        Long lotId = taskHead.getLotId();
        Long itemId = taskInfo.getItemId();
        BaseinfoLocation realLocation = locationService.getLocation(locationId);
        if (realLocation == null) {
            throw new BizCheckedException("2030006");
        }
        // 实际上架位置和分配位置不一致
        if (!locationId.equals(taskHead.getAllocLocationId())) {
            // 拣货位
            if (realLocation.getRegionType().equals(LocationConstant.SHELFS) && realLocation.getBinUsage().equals(BinUsageConstant.BIN_UASGE_PICK)) {
                // 检查是否是该商品的拣货位
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("itemId", itemId);
                params.put("pickLocationid", locationId);
                params.put("userstatus", 1);
                List<BaseinfoItemLocation> itemLocations = itemLocationService.getItemLocation(params);
                if (itemLocations.size() < 1) {
                    throw new BizCheckedException("2030017");
                }
            }
            // 存货位
            if (realLocation.getRegionType().equals(LocationConstant.SHELFS) && realLocation.getBinUsage().equals(BinUsageConstant.BIN_UASGE_STORE)) {
                // 检查是否有库存
                List<StockQuant> stockQuants = stockQuantService.getQuantsByLocationId(locationId);
                if (stockQuants.size() > 0) {
                    throw new BizCheckedException("2030018");
                }
                // 检查位置锁定状态
                if (locationService.checkLocationLockStatus(locationId)) {
                    throw new BizCheckedException("2030011");
                }
            }
            // 地堆
            if (realLocation.getType().equals(LocationConstant.BIN) && realLocation.getBinUsage().equals(BinUsageConstant.BIN_FLOOR_STORE)) {
                // 判断是否为同一批次的
                List<StockQuant> stockQuants = stockQuantService.getQuantsByLocationId(locationId);
                if (!lotId.equals(stockQuants.get(0).getLotId())) {
                    throw new BizCheckedException("2030019");
                }
            }
            // 检查位置使用状态
            if (realLocation.getCanUse().equals(0)) {
                throw new BizCheckedException("2030007");
            }
        }
        // move到目标location_id
        // 拣货位需要合盘
        if (realLocation.getType().equals(LocationConstant.BIN) && realLocation.getBinUsage().equals(BinUsageConstant.BIN_PICK_STORE)) {
            List<StockQuant> quants = stockQuantService.getQuantsByLocationId(locationId);
            if (quants.isEmpty()) {
                stockMoveService.moveWholeContainer(taskHead.getContainerId(), taskId, taskHead.getOperator(), locationService.getWarehouseLocation().getLocationId(), locationId);
                //iStockMoveRpcService.moveWholeContainer(taskHead.getContainerId(), taskId, taskHead.getOperator(), locationService.getWarehouseLocationId(), locationId);
            } else {
                Long containerId = quants.get(0).getContainerId();
                stockMoveService.moveWholeContainer(taskHead.getContainerId(), containerId, taskId, taskHead.getOperator(), locationService.getWarehouseLocation().getLocationId(), locationId);
                //iStockMoveRpcService.moveWholeContainer(taskHead.getContainerId(), containerId, taskId, taskHead.getOperator(), locationService.getWarehouseLocationId(), locationId);
            }
        } else if (realLocation.getType().equals(LocationConstant.BIN) && realLocation.getBinUsage().equals(BinUsageConstant.BIN_FLOOR_STORE)) {
            // 地堆区需要合盘
            List<StockQuant> quants = stockQuantService.getQuantsByLocationId(locationId);
            Long containerId = 0L;
            if (quants.isEmpty()) {
                containerId = containerService.createContainerByType(ContainerConstant.PALLET).getContainerId();
            } else {
                containerId = quants.get(0).getContainerId();
            }
            stockMoveService.moveWholeContainer(taskHead.getContainerId(), containerId, taskId, taskHead.getOperator(), locationService.getWarehouseLocation().getLocationId(), locationId);
            //iStockMoveRpcService.moveWholeContainer(taskHead.getContainerId(), containerId, taskId, taskHead.getOperator(), locationService.getWarehouseLocationId(), locationId);
        } else {
            stockMoveService.moveWholeContainer(taskHead.getContainerId(), taskId, taskHead.getOperator(), locationService.getWarehouseLocation().getLocationId(), locationId);
            //iStockMoveRpcService.moveWholeContainer(taskHead.getContainerId(), taskId, taskHead.getOperator(), locationService.getWarehouseLocationId(), locationId);
        }
        taskService.done(taskId, locationId);
        // 释放分配的location
        locationService.unlockLocation(taskHead.getAllocLocationId());

        // 更新可用库存
        StockDelta delta = new StockDelta();
        delta.setItemId(taskInfo.getItemId());
        delta.setInhouseQty(taskInfo.getQty());
        delta.setBusinessId(taskId);
        delta.setType(StockConstant.TYPE_SHELVE);
        stockSummaryService.changeStock(delta);

    }

    public void getConcrete(TaskEntry taskEntry) {
        taskEntry.setTaskHead(taskService.getShelveTaskHead(taskEntry.getTaskInfo().getTaskId()));
    }

    public void getHeadConcrete(TaskEntry taskEntry) {
        taskEntry.setTaskHead(taskService.getShelveTaskHead(taskEntry.getTaskInfo().getTaskId()));
    }
}
