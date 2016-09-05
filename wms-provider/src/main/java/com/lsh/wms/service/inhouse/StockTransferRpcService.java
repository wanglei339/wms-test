package com.lsh.wms.service.inhouse;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.wms.api.service.inhouse.IStockTransferRpcService;
import com.lsh.wms.api.service.item.IItemRpcService;
import com.lsh.wms.api.service.location.ILocationRpcService;
import com.lsh.wms.api.service.stock.IStockMoveRpcService;
import com.lsh.wms.api.service.stock.IStockQuantRpcService;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.constant.ContainerConstant;
import com.lsh.wms.core.constant.LocationConstant;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.dao.task.TaskInfoDao;
import com.lsh.wms.core.service.container.ContainerService;
import com.lsh.wms.core.service.item.ItemLocationService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.stock.StockQuantService;
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
 * Created by mali on 16/7/26.
 */

@Service(protocol = "dubbo")
public class StockTransferRpcService implements IStockTransferRpcService {
    private static final Logger logger = LoggerFactory.getLogger(StockTransferRpcService.class);

    @Reference
    private ITaskRpcService taskRpcService;

    @Autowired
    private BaseTaskService baseTaskService;

    @Reference
    private IItemRpcService itemRpcService;

    @Autowired
    private LocationService locationService;

    @Reference
    private IStockMoveRpcService moveRpcService;

    @Autowired
    private ContainerService containerService;

    @Autowired
    private StockTransferCore core;

    @Reference
    private ILocationRpcService locationRpcService;

    @Autowired
    private StockQuantService quantService;

    @Reference
    private IStockQuantRpcService stockQuantService;

    @Autowired
    private ItemLocationService itemLocationService;

    @Autowired
    private TaskInfoDao taskInfoDao;

    public boolean checkPlan(StockTransferPlan plan) throws BizCheckedException {
        if (plan.getUomQty().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BizCheckedException("2550034");
        }
        Long fromLocationId = plan.getFromLocationId();
        Long toLocationId = plan.getToLocationId();
        if (fromLocationId.compareTo(toLocationId) == 0) {
            throw new BizCheckedException("2550017");
        }
        BaseinfoLocation fromLocation = locationService.getLocation(fromLocationId);
        BaseinfoLocation toLocation = locationService.getLocation(toLocationId);
        if (fromLocation == null || toLocation == null) {
            throw new BizCheckedException("2550016");
        }
        if (fromLocation.getType().equals(LocationConstant.TEMPORARY) || toLocation.getType().equals(LocationConstant.TEMPORARY)) {
            throw new BizCheckedException("2550035");
        }
        if (toLocation.getCanStore() != 1) {
            throw new BizCheckedException("2550020");
        }
        if( toLocation.getCanUse() != 1) {
            throw new BizCheckedException("2550006");
        }
        Long itemId = plan.getItemId();
        StockQuantCondition condition = new StockQuantCondition();
        condition.setLocationId(fromLocationId);
        condition.setItemId(itemId);
        condition.setReserveTaskId(0L);
        BigDecimal total = stockQuantService.getQty(condition);
        List<StockQuant> quantList = stockQuantService.getQuantList(condition);

        Long taskId = plan.getTaskId();
        if (!taskId.equals(0L)) {
            TaskEntry taskEntry = taskRpcService.getTaskEntryById(taskId);
            if (taskEntry == null) {
                throw new BizCheckedException("3040001");
            }
            TaskInfo taskInfo = taskEntry.getTaskInfo();
            condition.setReserveTaskId(taskId);
            total = total.add(stockQuantService.getQty(condition));
            List<StockQuant> list = stockQuantService.getQuantList(condition);
            if (quantList.isEmpty() && list.isEmpty()) {
                throw new BizCheckedException("2550032");
            }
            if (toLocationId.compareTo(taskInfo.getToLocationId()) != 0 && locationService.checkLocationLockStatus(toLocationId)) {
                throw new BizCheckedException("2550010");
            }
        } else {
            if (quantList.isEmpty()) {
                throw new BizCheckedException("2550032");
            }
            if (locationService.checkLocationLockStatus(toLocationId)) {
                throw new BizCheckedException("2550010");
            }
        }

        List<StockQuant> toQuants = quantService.getQuantsByLocationId(toLocationId);
        Long locationType = toLocation.getType();
        if( toQuants != null && toQuants.size() > 0
                && locationType.compareTo(LocationConstant.FLOOR) != 0
                && locationType.compareTo(LocationConstant.BACK_AREA) != 0
                && locationType.compareTo(LocationConstant.DEFECTIVE_AREA) != 0) {
            // 拣货位
            if (locationType.compareTo(LocationConstant.LOFT_PICKING_BIN) == 0 || locationType.compareTo(LocationConstant.SHELF_PICKING_BIN) == 0) {
                List<BaseinfoItemLocation> itemLocations = itemLocationService.getItemLocationByLocationID(toLocationId);
                if (itemLocations.size() > 0 && itemLocations.get(0).getItemId().compareTo(itemId) != 0) {
                    throw new BizCheckedException("2550004");
                }
            } else { //其余货位
                if (toQuants.get(0).getItemId().compareTo(itemId) != 0) {
                    throw new BizCheckedException("2550004");
                }
                if (toQuants.get(0).getLotId().compareTo(quantList.get(0).getLotId()) != 0) {
                    throw new BizCheckedException("2550003");
                }
            }
        }
        core.fillTransferPlan(plan);
        BigDecimal requiredQty = plan.getQty().multiply(plan.getPackUnit()).setScale(0, BigDecimal.ROUND_HALF_UP);
        if ( requiredQty.compareTo(total) > 0) { // 移库要求的数量超出实际库存数量
            throw new BizCheckedException("2550002");
        }
        plan.setContainerId(quantList.get(0).getContainerId());
        return true;
    }

    public Long addPlan(StockTransferPlan plan) throws BizCheckedException {
        BaseinfoLocation fromLocation = locationService.getLocation( plan.getFromLocationId());
        BaseinfoLocation toLocation = locationService.getLocation(plan.getToLocationId());
        if (fromLocation == null || toLocation == null) {
            throw new BizCheckedException("2550016");
        }
        if (fromLocation.getLocationId().equals(toLocation.getLocationId())) {
            throw new BizCheckedException("2550017");
        }
        if (fromLocation.getCanStore() != 1) {
            fromLocation = core.getNearestLocation(fromLocation);
            plan.setFromLocationId(fromLocation.getLocationId());
        }
        if (toLocation.getCanStore() != 1) {
            toLocation = core.getNearestLocation(toLocation);
            plan.setToLocationId(toLocation.getLocationId());
        }
        Long taskId = plan.getTaskId();
        plan.setTaskId(0L);
        if (checkPlan(plan)) {
            plan.setTaskId(taskId);
            Long containerId = plan.getContainerId();
            if (plan.getSubType().compareTo(2L) == 0 || plan.getSubType().compareTo(3L) == 0) {
                containerId = containerService.createContainerByType(ContainerConstant.CAGE).getContainerId();
            }
            TaskEntry taskEntry = new TaskEntry();
            TaskInfo taskInfo = new TaskInfo();
            ObjUtils.bean2bean(plan, taskInfo);
            taskInfo.setTaskName("移库任务[ " + taskInfo.getFromLocationId() + " => " + taskInfo.getToLocationId() + "]");
            taskInfo.setType(TaskConstant.TYPE_STOCK_TRANSFER);
            taskInfo.setContainerId(containerId);
            taskInfo.setQtyDone(taskInfo.getQty());
            taskEntry.setTaskInfo(taskInfo);
            taskId = taskRpcService.create(TaskConstant.TYPE_STOCK_TRANSFER, taskEntry);
        }
        return taskId;
    }

    public void updatePlan(StockTransferPlan plan) throws BizCheckedException {
        Long taskId = plan.getTaskId();
        TaskEntry taskEntry = taskRpcService.getTaskEntryById(taskId);
        if (taskEntry == null) {
            throw new BizCheckedException("3040001");
        }
        TaskInfo taskInfo = taskEntry.getTaskInfo();
        if (!taskInfo.getStatus().equals(TaskConstant.Draft)) {
            throw new BizCheckedException("2550033");
        }
        BaseinfoLocation fromLocation = locationService.getLocation( plan.getFromLocationId());
        BaseinfoLocation toLocation = locationService.getLocation(plan.getToLocationId());
        if (fromLocation == null || toLocation == null) {
            throw new BizCheckedException("2550016");
        }
        if (fromLocation.getLocationId().equals(toLocation.getLocationId())) {
            throw new BizCheckedException("2550017");
        }
        if (fromLocation.getCanStore() != 1) {
            fromLocation = core.getNearestLocation(fromLocation);
            plan.setFromLocationId(fromLocation.getLocationId());
        }
        if (toLocation.getCanStore() != 1) {
            toLocation = core.getNearestLocation(toLocation);
            plan.setToLocationId(toLocation.getLocationId());
        }
        if (checkPlan(plan)) {
            this.cancelPlan(plan.getTaskId());
            plan.setTaskId(0L);
            this.addPlan(plan);
        }
    }

    public void cancelPlan(Long taskId){
        taskRpcService.cancel(taskId);
    }

    public Map<String, Object> scanFromLocation(Map<String, Object> params) throws BizCheckedException {
        Long taskId = Long.valueOf(params.get("taskId").toString());
        Long locationId = Long.valueOf(params.get("locationId").toString());
        TaskEntry taskEntry = taskRpcService.getTaskEntryById(taskId);
        Long nextOutTask = core.getNextOutbound(taskEntry);
        if (!taskEntry.getTaskInfo().getFromLocationId().equals(locationId)) {
            throw new BizCheckedException("2550018");
        }
        core.outbound(params);
        Map<String, Object> next = new HashMap<String, Object>();
        Long nextLocationId, nextItem, subType;
        String packName;
        BigDecimal qty;
        //inbound
        if (nextOutTask == 0) {
            Long nextInTask = core.getFirstInbound(taskEntry.getTaskInfo().getOperator());
            TaskInfo nextInfo = taskRpcService.getTaskEntryById(nextInTask).getTaskInfo();
            nextLocationId = nextInfo.getToLocationId();
            nextItem = nextInfo.getItemId();
            packName = nextInfo.getPackName();
            subType = nextInfo.getSubType();
            qty = nextInfo.getQtyDone();
            next.put("type", 2);
            next.put("taskId",nextInTask);
        } else {//outbound
            TaskInfo nextInfo = taskRpcService.getTaskEntryById(nextOutTask).getTaskInfo();
            nextLocationId = nextInfo.getFromLocationId();
            nextItem = nextInfo.getItemId();
            packName = nextInfo.getPackName();
            subType = nextInfo.getSubType();
            qty = nextInfo.getQty();
            next.put("type", 1);
            next.put("taskId", nextOutTask);
        }
        next.put("locationId", nextLocationId);
        next.put("locationCode", locationService.getLocation(nextLocationId).getLocationCode());
        next.put("itemId", nextItem);
        next.put("itemName", itemRpcService.getItem(nextItem).getSkuName());
        next.put("packName", packName);
        next.put("uomQty", qty);
        if (subType.compareTo(1L) == 0) {
            next.put("uomQty", "整托");
        }
        next.put("subType", subType);
        return next;
    }

    public Map<String, Object> scanToLocation(Map<String, Object> params) throws BizCheckedException {
        Long taskId = Long.valueOf(params.get("taskId").toString());
        Long locationId = Long.valueOf(params.get("locationId").toString());
        TaskEntry taskEntry = taskRpcService.getTaskEntryById(taskId);
        Long nextInTask = core.getNextInbound(taskEntry);
        if (!taskEntry.getTaskInfo().getToLocationId().equals(locationId)) {
            throw new BizCheckedException("2550019");
        }
        core.inbound(params);
        Map<String, Object> next = new HashMap<String, Object>();
        if (nextInTask == 0) {
            next.put("response", true);
        } else {
            TaskInfo nextInfo = taskRpcService.getTaskEntryById(nextInTask).getTaskInfo();
            Long nextLocationId = nextInfo.getToLocationId();
            next.put("type", 2);
            next.put("taskId", nextInfo.getTaskId());
            next.put("locationId", nextLocationId);
            next.put("locationCode", locationService.getLocation(nextLocationId).getLocationCode());
            next.put("itemId", nextInfo.getItemId());
            next.put("itemName", itemRpcService.getItem(nextInfo.getItemId()).getSkuName());
            next.put("packName", nextInfo.getPackName());
            next.put("uomQty", nextInfo.getQtyDone());
            if (nextInfo.getSubType().compareTo(1L) == 0) {
                next.put("uomQty", "整托");
            }
            next.put("subType", nextInfo.getSubType());
        }
        return next;
    }

    public Long assign(Long staffId) throws BizCheckedException {
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("status", TaskConstant.Assigned);
        mapQuery.put("operator", staffId);
        List<TaskEntry> list = taskRpcService.getTaskList(TaskConstant.TYPE_STOCK_TRANSFER, mapQuery);
        //task exist
        if (!list.isEmpty()) {
            mapQuery.put("ext3", 0L);
            List<TaskEntry> entryList = taskRpcService.getTaskList(TaskConstant.TYPE_STOCK_TRANSFER, mapQuery);
            //outbound
            if (!entryList.isEmpty()) {
                TaskEntry nextEntry = entryList.get(0);
                Long ext1 = nextEntry.getTaskInfo().getExt1();
                for (TaskEntry entry : entryList) {
                    Long nextExt1 = entry.getTaskInfo().getExt1();
                    if (nextExt1.compareTo(ext1) < 0) {
                        nextEntry = entry;
                        ext1 = nextExt1;
                    }
                }
                return nextEntry.getTaskInfo().getTaskId();
            }
            TaskEntry nextEntry = list.get(0);
            Long ext2 = nextEntry.getTaskInfo().getExt2();
            for (TaskEntry entry : list) {
                Long nextExt2 = entry.getTaskInfo().getExt2();
                if (nextExt2.compareTo(ext2) < 0) {
                    nextEntry = entry;
                    ext2 = nextExt2;
                }
            }
            return nextEntry.getTaskInfo().getTaskId();
        }
        //get new task
        mapQuery.clear();
        mapQuery.put("status", TaskConstant.Draft);
        list = taskRpcService.getTaskList(TaskConstant.TYPE_STOCK_TRANSFER, mapQuery);
        if (list.isEmpty()) {
            return 0L;
        }
        List<Long> taskList = core.getMoreTasks(list);
//        for (Long task : taskList) {
//            taskRpcService.assign(task, staffId);
//        }
        taskRpcService.assign(list.get(0).getTaskInfo().getTaskId(), staffId);
        return Long.valueOf(this.sortTask(staffId).get("taskId").toString());
    }

    public Map<String, Object> sortTask(Long staffId) {
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("status", TaskConstant.Assigned);
        mapQuery.put("operator", staffId);
        List<TaskEntry> entryList = taskRpcService.getTaskList(TaskConstant.TYPE_STOCK_TRANSFER, mapQuery);
        if (entryList.isEmpty()) {
            throw new BizCheckedException("3040001");
        }
        core.sortOutbound(entryList);
        core.sortInbound(entryList);
        Long taskId = core.getFirstOutbound(staffId);
        TaskInfo nextInfo = taskRpcService.getTaskEntryById(taskId).getTaskInfo();
        Long nextLocationId = nextInfo.getFromLocationId();
        Map<String, Object> next = new HashMap<String, Object>();
        next.put("type", 1);
        next.put("taskId", nextInfo.getTaskId());
        next.put("locationId", nextLocationId);
        next.put("locationCode", locationService.getLocation(nextLocationId).getLocationCode());
        next.put("itemId", nextInfo.getItemId());
        next.put("itemName", itemRpcService.getItem(nextInfo.getItemId()).getSkuName());
        next.put("packName", nextInfo.getPackName());
        next.put("uomQty", nextInfo.getQty());
        next.put("subType", nextInfo.getSubType());
        return next;
    }

    private void createScrap() throws BizCheckedException {
        StockQuantCondition condition = new StockQuantCondition();
        condition.setIsDefect(1L);
        condition.setReserveTaskId(0L);
        List<StockQuant> quantList = stockQuantService.getQuantList(condition);
        Long toLocationId = locationService.getDefectiveLocationId();
        for (StockQuant quant : quantList) {
            BaseinfoLocation location = locationService.getLocation(quant.getLocationId());
            if (location == null) {
                continue;
            }
            if (location.getType().compareTo(LocationConstant.DEFECTIVE_AREA) != 0) {
                StockTransferPlan plan = new StockTransferPlan();
                plan.setFromLocationId(quant.getLocationId());
                plan.setToLocationId(toLocationId);
                plan.setQty(quant.getQty().divide(quant.getPackUnit()));
                plan.setItemId(quant.getItemId());
                Long subType = 2L;
                if (location.getType().equals(LocationConstant.SPLIT_SHELF_BIN)) {
                    subType = 3L;
                }
                plan.setSubType(subType);
                this.addPlan(plan);
            }
        }
    }

    private void createReturn() throws BizCheckedException {
        StockQuantCondition condition = new StockQuantCondition();
        condition.setIsRefund(1L);
        condition.setReserveTaskId(0L);
        List<StockQuant> quantList = stockQuantService.getQuantList(condition);
        Long toLocationId = locationService.getBackLocationId();
        for (StockQuant quant : quantList) {
            BaseinfoLocation location = locationService.getLocation(quant.getLocationId());
            if (location == null) {
                continue;
            }
            if (location.getType().compareTo(LocationConstant.BACK_AREA) != 0) {
                StockTransferPlan plan = new StockTransferPlan();
                plan.setFromLocationId(quant.getLocationId());
                plan.setToLocationId(toLocationId);
                plan.setItemId(quant.getItemId());
                Long subType = 2L;
                if (location.getType().equals(LocationConstant.SPLIT_SHELF_BIN)) {
                    subType = 3L;
                }
                plan.setSubType(subType);
                plan.setQty(quant.getQty().divide(quant.getPackUnit()));
                this.addPlan(plan);
            }
        }
    }

    public void createStockTransfer() throws BizCheckedException {
        this.createScrap();
        this.createReturn();
    }

    public Long allocateToLocationId(StockQuant quant) throws BizCheckedException {
        Long locationId = quant.getLocationId();
        BaseinfoLocation location = locationService.getLocation(locationId);
        if (location == null ){
            throw new BizCheckedException("2060012");
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("canStore", LocationConstant.CAN_STORE);
        params.put("isValid", LocationConstant.IS_VALID);
        params.put("canUse", LocationConstant.CAN_USE);
        params.put("isLocked", LocationConstant.UNLOCK);
        List <BaseinfoLocation> shelfLocationList = new ArrayList<BaseinfoLocation>();
        List <BaseinfoLocation> loftLocationList = new ArrayList<BaseinfoLocation>();
        if (location.getType().equals(LocationConstant.SPLIT_SHELF_BIN)) {
            params.put("type", LocationConstant.SPLIT_SHELF_BIN);
            shelfLocationList = locationService.getBaseinfoLocationList(params);
            if (shelfLocationList != null && !shelfLocationList.isEmpty()) {
                shelfLocationList.remove(location);
            }
        } else {
            params.put("type", LocationConstant.SHELF_STORE_BIN);
            shelfLocationList = locationService.getBaseinfoLocationList(params);
            if (shelfLocationList != null && !shelfLocationList.isEmpty()) {
                shelfLocationList.remove(location);
            }
            params.put("type", LocationConstant.LOFT_STORE_BIN);
            loftLocationList = locationService.getBaseinfoLocationList(params);
            if (loftLocationList != null && !loftLocationList.isEmpty()) {
                loftLocationList.remove(location);
                shelfLocationList.addAll(loftLocationList);
            }
        }
        StockQuantCondition condition = new StockQuantCondition();
        condition.setItemId(quant.getItemId());
        condition.setLotId(quant.getLotId());
        condition.setLocationList(shelfLocationList);
        List<StockQuant> quantList = stockQuantService.getQuantList(condition);
        // find empty location
        if (quantList == null || quantList.isEmpty()) {
            params.put("curContainerVol", 0L);
            loftLocationList = locationService.getBaseinfoLocationList(params);
            if (loftLocationList != null && !loftLocationList.isEmpty()) {
                loftLocationList.remove(location);
            }
            if (!location.getType().equals(LocationConstant.SPLIT_SHELF_BIN)) {
                params.put("type", LocationConstant.SHELF_STORE_BIN);
                shelfLocationList = locationService.getBaseinfoLocationList(params);
                if (shelfLocationList != null && !shelfLocationList.isEmpty()) {
                    shelfLocationList.remove(location);
                    loftLocationList.addAll(shelfLocationList);
                }
            }
            if (loftLocationList == null || loftLocationList.isEmpty()) {
                return 0L;
            }
            return loftLocationList.get(0).getLocationId();
        }
        return quantList.get(0).getLocationId();
    }
}
