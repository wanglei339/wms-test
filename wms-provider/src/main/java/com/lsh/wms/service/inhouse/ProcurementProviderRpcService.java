package com.lsh.wms.service.inhouse;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.wms.api.service.inhouse.IProcurementProveiderRpcService;
import com.lsh.wms.api.service.inhouse.IProcurementRpcService;
import com.lsh.wms.api.service.item.IItemRpcService;
import com.lsh.wms.api.service.location.ILocationRpcService;
import com.lsh.wms.api.service.stock.IStockQuantRpcService;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.constant.ContainerConstant;
import com.lsh.wms.core.constant.LocationConstant;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.service.container.ContainerService;
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
import java.util.*;

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

    @Reference
    private IItemRpcService itemRpcService;

    @Reference
    private IItemRpcService itemLocationService;

    @Autowired
    private BaseTaskService baseTaskService;

    public void addProcurementPlan(StockTransferPlan plan) throws BizCheckedException {
        if(!this.checkPlan(plan)){
            logger.error("error plan ：" + plan.toString());
            return;
        }
        if (baseTaskService.checkTaskByToLocation(plan.getToLocationId(), TaskConstant.TYPE_PROCUREMENT)) {
            throw new BizCheckedException("2550015");
        }
        StockQuantCondition condition = new StockQuantCondition();
        TaskEntry taskEntry = new TaskEntry();
        TaskInfo taskInfo = new TaskInfo();
        condition.setLocationId(plan.getFromLocationId());
        condition.setItemId(plan.getItemId());
        BigDecimal total = stockQuantService.getQty(condition);

        core.fillTransferPlan(plan);

        if ( plan.getQty().compareTo(total) > 0) { // 移库要求的数量超出实际库存数量
            throw new BizCheckedException("2550008");
        }
        List<StockQuant> quantList = stockQuantService.getQuantList(condition);
        Long containerId = quantList.get(0).getContainerId();
        if (plan.getSubType().equals(2L)) {
            containerId = containerService.createContainerByType(2L).getContainerId();
        }

        ObjUtils.bean2bean(plan, taskInfo);
        taskInfo.setTaskName("补货任务[ " + taskInfo.getFromLocationId() + " => " + taskInfo.getToLocationId() + "]");
        taskInfo.setType(TaskConstant.TYPE_PROCUREMENT);
        taskInfo.setContainerId(containerId);
        taskEntry.setTaskInfo(taskInfo);
        taskRpcService.create(TaskConstant.TYPE_PROCUREMENT, taskEntry);
    }

    public void updateProcurementPlan(StockTransferPlan plan)  throws BizCheckedException {
        TaskEntry entry =  taskRpcService.getTaskEntryById(plan.getTaskId());
        if(entry == null){
            throw new BizCheckedException("3040001");
        }
        StockQuantCondition condition = new StockQuantCondition();
        condition.setLocationId(plan.getFromLocationId());
        condition.setItemId(plan.getItemId());
        BigDecimal total = stockQuantService.getQty(condition);
        core.fillTransferPlan(plan);

        if ( plan.getQty().compareTo(total) > 0) { // 移库要求的数量超出实际库存数量

            throw new BizCheckedException("2550008");
        }
        TaskInfo taskInfo = entry.getTaskInfo();
        List<StockQuant> quantList = stockQuantService.getQuantList(condition);
        Long containerId = quantList.get(0).getContainerId();
        if (plan.getSubType().equals(2L)) {
            containerId = containerService.createContainerByType(2L).getContainerId();
        }

        ObjUtils.bean2bean(plan, taskInfo);
        taskInfo.setTaskName("补货任务[ " + taskInfo.getFromLocationId() + " => " + taskInfo.getToLocationId() + "]");
        taskInfo.setType(TaskConstant.TYPE_PROCUREMENT);
        taskInfo.setContainerId(containerId);
        entry.setTaskInfo(taskInfo);
        taskRpcService.update(TaskConstant.TYPE_PROCUREMENT, entry);
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
                    List<BaseinfoLocation> shelfList = locationService.getLocationsByType("shelf_store_bin");
                    List<Long> shelfBinList = new ArrayList<Long>();
                    for (BaseinfoLocation shelf : shelfList) {
                        if (shelf.getIsLocked().compareTo(0)==0) {
                            shelfBinList.add(shelf.getLocationId());
                        }
                    }
                    condition.setLocationIdList(shelfBinList);
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
                    plan.setSubType(1L);
                    plan.setUomQty(BigDecimal.ONE);
                    plan.setTestList(shelfBinList);
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
                    List<BaseinfoLocation> loftList = locationService.getLocationsByType("loft_store_bin");
                    List<Long> loftBinList = new ArrayList<Long>();
                    for (BaseinfoLocation loft : loftList ) {
                        if(loft.getIsLocked().compareTo(0)==0) {
                            loftBinList.add(loft.getLocationId());
                        }
                    }
                    condition.setLocationIdList(loftBinList);
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
                        plan.setSubType(2L);
                        plan.setTestList(loftBinList);
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
    public boolean checkPlan(StockTransferPlan plan) throws BizCheckedException {
        StockQuantCondition condition = new StockQuantCondition();
        Long fromLocationId = plan.getFromLocationId();
        Long toLocationId = plan.getToLocationId();
        BaseinfoLocation fromLocation = locationRpcService.getLocation(fromLocationId);
        BaseinfoLocation toLocation = locationRpcService.getLocation(toLocationId);
        //货架捡货位只能在货架存货位取货，阁楼捡货位只能在阁楼捡货位取货
        if(fromLocation!=null && toLocation!=null &&
                (fromLocation.getType().equals(LocationConstant.LOFT_STORE_BIN) && toLocation.getType().equals(LocationConstant.LOFT_PICKING_BIN))
                || (fromLocation.getType().equals(LocationConstant.SHELF_STORE_BIN) && toLocation.getType().equals(LocationConstant.SHELF_PICKING_BIN))){
            condition.setLocationId(fromLocationId);
            List<StockQuant> quants = stockQuantService.getQuantList(condition);
            List<BaseinfoItemLocation> itemLocations = itemRpcService.getItemLocationByLocationID(toLocationId);
            for(StockQuant quant: quants) {
                for(BaseinfoItemLocation itemLocation:itemLocations){
                    if(itemLocation.getItemId().compareTo(quant.getItemId())==0){
                        return true;
                    }
                }
            }
        }

        return false;
    }
    public Set<Long> getOutBoundLocation(Long itemId,Long locationId) {
        StockQuantCondition condition = new StockQuantCondition();
        Set<Long> outBondLocations = new HashSet<Long>();
        condition.setItemId(itemId);
        BaseinfoLocation pickLocation = locationService.getLocation(locationId);
        if(pickLocation.getType().compareTo(LocationConstant.LOFT_PICKING_BIN)==0){
            List<StockQuant> quants = stockQuantService.getQuantList(condition);
            for(StockQuant quant:quants){
                BaseinfoLocation location = locationService.getLocation(quant.getLocationId());
                if(location.getType().compareTo(LocationConstant.LOFT_STORE_BIN)==0 && location.getIsLocked().compareTo(1)!=0){
                    outBondLocations.add(location.getLocationId());
                }
            }
        }else if(pickLocation.getType().compareTo(LocationConstant.SHELF_PICKING_BIN) ==0){
            List<StockQuant> quants = stockQuantService.getQuantList(condition);
            for(StockQuant quant:quants){
                BaseinfoLocation location = locationService.getLocation(quant.getLocationId());
                if(location.getType().compareTo(LocationConstant.SHELF_STORE_BIN)==0 && location.getIsLocked().compareTo(1)!=0){
                    outBondLocations.add(location.getLocationId());
                }
            }
        }
        return outBondLocations;
    }

}
