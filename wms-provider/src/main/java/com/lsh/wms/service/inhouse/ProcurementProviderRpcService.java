package com.lsh.wms.service.inhouse;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.wms.api.service.inhouse.IProcurementProveiderRpcService;
import com.lsh.wms.api.service.inhouse.IProcurementRpcService;
import com.lsh.wms.api.service.location.ILocationRpcService;
import com.lsh.wms.api.service.stock.IStockQuantRpcService;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.service.container.ContainerService;
import com.lsh.wms.core.service.item.ItemLocationService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.model.baseinfo.BaseinfoItemLocation;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.stock.StockQuant;
import com.lsh.wms.model.stock.StockQuantCondition;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.model.transfer.StockTransferPlan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mali on 16/8/2.
 */
@Service(protocol = "dubbo")
public class ProcurementProviderRpcService implements IProcurementProveiderRpcService {
    private static final Logger logger = LoggerFactory.getLogger(ProcurementProviderRpcService.class);

    @Autowired
    private StockTransferCore core;

    @Reference
    private IStockQuantRpcService stockQuantService;

    @Reference
    private IProcurementRpcService rpcService;

    @Autowired
    private ContainerService containerService;

    @Reference
    private ITaskRpcService taskRpcService;

    @Autowired
    private LocationService locationService;

    @Reference
    private ILocationRpcService locationRpcService;

    @Autowired
    private ItemLocationService itemLocationService;

    @Autowired
    private BaseTaskService baseTaskService;

    public void addProcurementPlan(StockTransferPlan plan) throws BizCheckedException {
        StockQuantCondition condition = new StockQuantCondition();
        TaskEntry taskEntry = new TaskEntry();
        TaskInfo taskInfo = new TaskInfo();
        condition.setLocationId(plan.getFromLocationId());
        condition.setItemId(plan.getItemId());
        BigDecimal total = stockQuantService.getQty(condition);

        if ( plan.getQty().compareTo(total) > 0) { // 移库要求的数量超出实际库存数量
            throw new BizCheckedException(plan.getQty().toString() + "====" + total.toString());
        }
        List<StockQuant> quantList = stockQuantService.getQuantList(condition);
        Long containerId = quantList.get(0).getContainerId();
        if (plan.getPackName() == "pallet") {
            containerId = containerService.createContainerByType(2L).getId();
        }
        core.fillTransferPlan(plan);

        ObjUtils.bean2bean(plan, taskInfo);
        taskInfo.setTaskName("补货任务[ " + taskInfo.getFromLocationId() + " => " + taskInfo.getToLocationId() + "]");
        taskInfo.setType(TaskConstant.TYPE_PROCUREMENT);
        taskInfo.setContainerId(containerId);
        taskEntry.setTaskInfo(taskInfo);
        taskRpcService.create(TaskConstant.TYPE_PROCUREMENT, taskEntry);
    }

    public void updateProcurementPlan(StockTransferPlan plan)  throws BizCheckedException {
        StockQuantCondition condition = new StockQuantCondition();
        condition.setLocationId(plan.getFromLocationId());
        condition.setItemId(plan.getItemId());
        BigDecimal total = stockQuantService.getQty(condition);
        core.fillTransferPlan(plan);

        if ( plan.getQty().compareTo(total) > 0) { // 移库要求的数量超出实际库存数量
            throw new BizCheckedException(plan.getQty().toString() + "====" + total.toString());
        }
        TaskEntry taskEntry = new TaskEntry();
        TaskInfo taskInfo = new TaskInfo();
        taskInfo.setSubType(2L);
        List<StockQuant> quantList = stockQuantService.getQuantList(condition);
        Long containerId = quantList.get(0).getContainerId();
        if (plan.getPackName() == "pallet") {
            taskInfo.setSubType(1L);
            containerId = containerService.createContainerByType(2L).getId();
        }

        ObjUtils.bean2bean(plan, taskInfo);
        taskInfo.setTaskName("补货任务[ " + taskInfo.getFromLocationId() + " => " + taskInfo.getToLocationId() + "]");
        taskInfo.setType(TaskConstant.TYPE_PROCUREMENT);
        taskInfo.setContainerId(containerId);
        taskEntry.setTaskInfo(taskInfo);
        taskRpcService.create(TaskConstant.TYPE_PROCUREMENT, taskEntry);
    }

    private void createShelfProcurement() throws BizCheckedException {
        List<BaseinfoLocation> shelfLocationList = locationService.getLocationsByType("shelf_collection_bin");
        for (BaseinfoLocation shelfCollectionBin : shelfLocationList) {
            List<BaseinfoItemLocation> itemLocationList = itemLocationService.getItemLocationByLocationID(shelfCollectionBin.getLocationId());
            for (BaseinfoItemLocation itemLocation : itemLocationList) {
                if (rpcService.needProcurement(itemLocation.getPickLocationid(), itemLocation.getItemId())) {
                    if (baseTaskService.checkTaskByToLocation(itemLocation.getPickLocationid(), TaskConstant.TYPE_PROCUREMENT)) {
                        continue;
                    }
                    // 找合适的quant
                    StockQuantCondition condition = new StockQuantCondition();
                    List<BaseinfoLocation> shelfList = locationService.getLocationsByType("shelf");
                    List<Long> shelfBinList = new ArrayList<Long>();
                    for (BaseinfoLocation shelf : shelfList) {
                        shelfBinList.addAll(locationService.getStoreLocationIds(shelf.getLocationId()));
                    }
                    condition.setLocationList(shelfBinList);
                    condition.setItemId(itemLocation.getItemId());
                    List<StockQuant> quantList = stockQuantService.getQuantList(condition);
                    if (quantList.isEmpty()) {
                        logger.warn("ItemId:" + itemLocation.getItemId() + "缺货异常");
                        continue;
                    }
                    StockQuant quant = quantList.get(0);
                    // 创建任务
                    StockTransferPlan plan = new StockTransferPlan();
                    plan.setItemId(itemLocation.getItemId());
                    plan.setFromLocationId(quant.getLocationId());
                    plan.setToLocationId(itemLocation.getPickLocationid());
                    plan.setPackName("pallet");
                    plan.setUomQty(BigDecimal.ONE);
                    this.addProcurementPlan(plan);
                }
            }
        }
    }

    public Long assign(Long staffId) throws BizCheckedException {
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("status", TaskConstant.Draft);
        List<TaskEntry> list = taskRpcService.getTaskList(TaskConstant.TYPE_PROCUREMENT, mapQuery);
        if (list.isEmpty()) {
            return 0L;
        } else {
            taskRpcService.assign(list.get(0).getTaskInfo().getTaskId(), staffId);
            return list.get(0).getTaskInfo().getTaskId();
        }
    }

    public void createLoftProcurement() throws BizCheckedException {
        List<BaseinfoLocation> loftPickLocationList = locationService.getLocationsByType("loft_collection_bin");
        for (BaseinfoLocation loftPick : loftPickLocationList) {
            List<BaseinfoItemLocation> itemLocationList = itemLocationService.getItemLocationByLocationID(loftPick.getLocationId());
            for (BaseinfoItemLocation itemLocation : itemLocationList) {
                if (rpcService.needProcurement(itemLocation.getPickLocationid(),itemLocation.getItemId())) {
                    if (baseTaskService.checkTaskByToLocation(itemLocation.getPickLocationid(), TaskConstant.TYPE_PROCUREMENT)) {
                        continue;
                    }
                    // 找合适的quant
                    StockQuantCondition condition = new StockQuantCondition();
                    List<BaseinfoLocation> loftList = locationService.getLocationsByType("loft");
                    List<Long> loftBinList = new ArrayList<Long>();
                    for (BaseinfoLocation loft : loftList ) {
                        loftBinList.addAll(locationService.getStoreLocationIds(loft.getLocationId()));
                    }
                    condition.setLocationList(loftBinList);
                    condition.setItemId(itemLocation.getItemId());
                    List<StockQuant> quantList = stockQuantService.getQuantList(condition);
                    if (quantList.isEmpty()) {
                        logger.warn("ItemId:" + itemLocation.getItemId() + "缺货异常");
                        continue;
                    }
                    BigDecimal requiredQty = new BigDecimal("3");
                    for (StockQuant quant : quantList) {
                        BigDecimal quantQty =  quant.getQty().divide(quant.getPackUnit());
                        // 创建任务
                        StockTransferPlan plan = new StockTransferPlan();
                        plan.setItemId(itemLocation.getItemId());
                        plan.setFromLocationId(quant.getLocationId());
                        plan.setToLocationId(itemLocation.getPickLocationid());
                        plan.setPackName(quant.getPackName());
                        plan.setUomQty(requiredQty);
                        this.addProcurementPlan(plan);
                        requiredQty = requiredQty.subtract(quantQty);
                        if (quantQty.compareTo(BigDecimal.ZERO) <= 0) {
                            break;
                        }
                    }
                }
            }
        }
    }

    public void createProcurement() throws BizCheckedException {
        this.createShelfProcurement();
        this.createLoftProcurement();
    }

    public void scanFromLocation(Map<String, Object> params) throws BizCheckedException {
        core.outbound(params);
    }

    public void scanToLocation(Map<String, Object> params) throws  BizCheckedException {
        Long taskId = Long.valueOf(params.get("taskId").toString());
        core.inbound(params);
        taskRpcService.done(taskId);
    }


}
