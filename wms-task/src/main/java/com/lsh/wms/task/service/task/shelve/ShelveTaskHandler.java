package com.lsh.wms.task.service.task.shelve;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.api.service.shelve.IShelveRpcService;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.service.container.ContainerService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.stock.StockLotService;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.model.baseinfo.BaseinfoContainer;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.shelve.ShelveTaskHead;
import com.lsh.wms.model.stock.StockLot;
import com.lsh.wms.model.stock.StockQuant;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.task.service.handler.AbsTaskHandler;
import com.lsh.wms.task.service.handler.TaskHandlerFactory;
import com.lsh.wms.core.service.shelve.ShelveTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

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
    @Reference
    private IShelveRpcService iShelveRpcService;

    @PostConstruct
    public void postConstruct() {
        handlerFactory.register(TaskConstant.TYPE_SHELVE, this);
    }

    protected void createConcrete(TaskEntry taskEntry) throws BizCheckedException {
        ShelveTaskHead taskHead = (ShelveTaskHead) taskEntry.getTaskHead();
        taskHead.setTaskId(taskEntry.getTaskInfo().getTaskId());
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
        BaseinfoLocation targetLocation = iShelveRpcService.assginShelveLocation(container);
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
        // TODO: 锁location
    }

    protected void assignConcrete(Long taskId, Long staffId) {
        taskService.assign(taskId, staffId);
        // TODO: move到仓库location_id,需考虑事务下沉
    }

    protected void doneConcrete(Long taskId, Long locationId) throws BizCheckedException{
        ShelveTaskHead taskHead = taskService.getShelveTaskHead(taskId);
        // 实际上架位置和分配位置不一致
        if (!locationId.equals(taskHead.getAllocLocationId())) {
            BaseinfoLocation realLocation = locationService.getLocation(locationId);
            if (realLocation == null) {
                throw new BizCheckedException("2030006");
            }
            // TODO: 要改
            if (locationService.isLocationInUse(locationId)) {
                throw new BizCheckedException("2030007");
            }
        }
        // TODO: move到目标location_id
        taskService.done(taskId, locationId);
        // TODO: 释放location
    }

    protected void getConcrete(TaskEntry taskEntry) {
        taskEntry.setTaskHead(taskService.getShelveTaskHead(taskEntry.getTaskInfo().getTaskId()));
    }

    protected void getHeadConcrete(TaskEntry taskEntry) {
        taskEntry.setTaskHead(taskService.getShelveTaskHead(taskEntry.getTaskInfo().getTaskId()));
    }
}
