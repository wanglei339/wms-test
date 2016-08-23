package com.lsh.wms.service.inhouse;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.api.service.inhouse.IStockTransferRpcService;
import com.lsh.wms.api.service.item.IItemRpcService;
import com.lsh.wms.api.service.location.ILocationRpcService;
import com.lsh.wms.api.service.stock.IStockMoveRpcService;
import com.lsh.wms.api.service.stock.IStockQuantRpcService;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.dao.task.TaskInfoDao;
import com.lsh.wms.core.service.container.ContainerService;
import com.lsh.wms.core.service.item.ItemLocationService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.constant.LocationConstant;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.model.baseinfo.BaseinfoItemLocation;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.stock.StockQuant;
import com.lsh.wms.model.stock.StockQuantCondition;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.model.transfer.StockTransferPlan;
import com.lsh.wms.model.transfer.StockTransferTaskDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.config.Task;

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

    public void addPlan(StockTransferPlan plan)  throws BizCheckedException {
        Long fromLocationId = plan.getFromLocationId();
        Long toLocationId = plan.getToLocationId();
        Long itemId = plan.getItemId();

        BaseinfoLocation toLocation = locationService.getLocation(toLocationId);
        if (toLocation == null) {
            throw new BizCheckedException("2060012");
        }
//        if (toLocation.getCanStore() != 1) {
//            throw new BizCheckedException("2550020");
//        }
        //TODO
        //get available location
        if (toLocation.getCanUse() != 1) {
            toLocation = core.getNearestLocation(toLocation);
            toLocationId = toLocation.getLocationId();
        }
        if (!locationService.checkLocationLockStatus(toLocationId)) {
            throw new BizCheckedException("2550010");
        }
        StockQuantCondition condition = new StockQuantCondition();
        condition.setLocationId(fromLocationId);
        condition.setItemId(itemId);
        condition.setReserveTaskId(0L);
        List<StockQuant> quantList = stockQuantService.getQuantList(condition);
        if (quantList.isEmpty()) {
            throw new BizCheckedException("2550002");
        }
        List<StockQuant> toQuants = quantService.getQuantsByLocationId(toLocationId);
        Long locationType = toLocation.getType();
        if( toQuants.size() > 0 ){
            // 地堆区
            if( locationType.compareTo(LocationConstant.FLOOR) == 0 ){
                if( toLocation.getCanUse() == 2) {
                    throw new BizCheckedException("2550006");
                }
            } else {
                // 拣货位
                if (locationType.compareTo(LocationConstant.LOFT_PICKING_BIN) == 0 || locationType.compareTo(LocationConstant.SHELF_PICKING_BIN) == 0) {
                    List<BaseinfoItemLocation> itemLocations = itemLocationService.getItemLocationByLocationID(toLocationId);
                    if (itemLocations.get(0).getItemId().compareTo(itemId) != 0) {
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
        }
        core.fillTransferPlan(plan);
        BigDecimal total = stockQuantService.getQty(condition);
        BigDecimal requiredQty = plan.getQty();
        if ( requiredQty.compareTo(total) > 0) { // 移库要求的数量超出实际库存数量
            throw new BizCheckedException("2550002");
        }
        Long containerId = quantList.get(0).getContainerId();
        if (plan.getSubType().compareTo(2L) == 0) {
            containerId = containerService.createContainerByType(2L).getContainerId();
        }
        TaskEntry taskEntry = new TaskEntry();
        TaskInfo taskInfo = new TaskInfo();
        ObjUtils.bean2bean(plan, taskInfo);
        taskInfo.setTaskName("移库任务[ " + taskInfo.getFromLocationId() + " => " + taskInfo.getToLocationId() + "]");
        taskInfo.setType(TaskConstant.TYPE_STOCK_TRANSFER);
        taskInfo.setContainerId(containerId);
        taskEntry.setTaskInfo(taskInfo);
        Long taskId = taskRpcService.create(TaskConstant.TYPE_STOCK_TRANSFER, taskEntry);
        logger.info("taskId: " + taskId);
    }

    public void updatePlan(StockTransferPlan plan)  throws BizCheckedException {
        Long taskId = plan.getTaskId();
        TaskEntry taskEntry = taskRpcService.getTaskEntryById(taskId);
        if (taskEntry == null) {
            throw new BizCheckedException("3040001");
        }
        TaskInfo taskInfo = taskEntry.getTaskInfo();
        locationService.getLocation(taskInfo.getToLocationId()).setIsLocked(0); //unLock
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("reserveTaskId", taskId);
        List<StockQuant> quantList = quantService.getQuants(mapQuery);
        for (StockQuant quant : quantList) {
            quant.setReserveTaskId(0L);
        }
        this.addPlan(plan);
        this.cancelPlan(plan.getTaskId());
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
        Long nextLocationId, nextItem;
        String packName;
        BigDecimal packUnit, qty;
        //inbound
        if (nextOutTask == 0) {
            Long nextInTask = core.getFirstInbound(taskEntry.getTaskInfo().getOperator());
            TaskInfo nextInfo = taskRpcService.getTaskEntryById(nextInTask).getTaskInfo();
            nextLocationId = nextInfo.getToLocationId();
            nextItem = nextInfo.getItemId();
            packName = nextInfo.getPackName();
            packUnit = nextInfo.getPackUnit();
            qty = nextInfo.getQtyDone();
            next.put("type", 2);
            next.put("taskId",nextInTask);
        } else {//outbound
            TaskInfo nextInfo = taskRpcService.getTaskEntryById(nextOutTask).getTaskInfo();
            nextLocationId = nextInfo.getFromLocationId();
            nextItem = nextInfo.getItemId();
            packName = nextInfo.getPackName();
            packUnit = nextInfo.getPackUnit();
            qty = nextInfo.getQty();
            next.put("type", 1);
            next.put("taskId", nextOutTask);
        }
        next.put("locationId", nextLocationId);
        next.put("locationCode", locationService.getLocation(nextLocationId).getLocationCode());
        next.put("itemId", nextItem);
        next.put("itemName", itemRpcService.getItem(nextItem).getSkuName());
        next.put("packName", packName);
        next.put("uomQty", qty.divide(packUnit));
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
            next.put("uomQty", nextInfo.getQtyDone().divide(nextInfo.getPackUnit()));
        }
        return next;
    }

    public Long assign(Long staffId) throws BizCheckedException {
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        //mapQuery.put("status", TaskConstant.Doing);
        mapQuery.put("status", TaskConstant.Assigned);
        mapQuery.put("operator", staffId);
        List<TaskEntry> list = taskRpcService.getTaskList(TaskConstant.TYPE_STOCK_TRANSFER, mapQuery);
        //task exist
        if (!list.isEmpty()) {
            //mapQuery.put("status", TaskConstant.Assigned);
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
        logger.info("New Task");
        //get new task
        mapQuery.clear();
        mapQuery.put("status", TaskConstant.Draft);
        list = taskRpcService.getTaskList(TaskConstant.TYPE_STOCK_TRANSFER, mapQuery);
        if (list.isEmpty()) {
            return 0L;
        }

//        Long taskId = core.getNearestTask(list);
//        taskRpcService.assign(taskId, staffId);
//        return taskId;

        List<Long> taskList = core.getMoreTasks(list);
        for (Long task : taskList) {
            taskRpcService.assign(task, staffId);
        }
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
        next.put("uomQty", nextInfo.getQty().divide(nextInfo.getPackUnit()));
        return next;
    }

    private void createScrap() throws BizCheckedException {
        StockQuantCondition condition = new StockQuantCondition();
        condition.setIsDefect(1L);
        List<StockQuant> quantList = stockQuantService.getQuantList(condition);
        Long toLocationId = locationService.getDefectiveLocationId();

        for (StockQuant quant : quantList) {
            if(baseTaskService.checkTaskByToLocation(toLocationId, TaskConstant.TYPE_STOCK_TRANSFER)){
                logger.warn("任务已存在");
                continue;
            }
            if (locationService.getLocation(quant.getLocationId()).getType().compareTo(LocationConstant.DEFECTIVE_AREA) != 0) {
                StockTransferPlan plan = new StockTransferPlan();
                plan.setFromLocationId(quant.getLocationId());
                plan.setToLocationId(toLocationId);
                plan.setQty(quant.getQty());
                plan.setItemId(quant.getItemId());
                plan.setSubType(2L);
                plan.setPackName(quant.getPackName());
                this.addPlan(plan);
            }
        }
    }

    private void createReturn() throws BizCheckedException {
        StockQuantCondition condition = new StockQuantCondition();
        condition.setIsRefund(1L);
        List<StockQuant> quantList = stockQuantService.getQuantList(condition);
        Long toLocationId = locationService.getBackLocationId();

        for (StockQuant quant : quantList) {
            if(baseTaskService.checkTaskByToLocation(toLocationId, TaskConstant.TYPE_STOCK_TRANSFER)){
                logger.warn("任务已存在");
                continue;
            }
            if (locationService.getLocation(quant.getLocationId()).getType().compareTo(LocationConstant.BACK_AREA) != 0) {
                StockTransferPlan plan = new StockTransferPlan();
                plan.setFromLocationId(quant.getLocationId());
                plan.setToLocationId(toLocationId);
                plan.setQty(quant.getQty());
                plan.setItemId(quant.getItemId());
                plan.setPackName(quant.getPackName());
                plan.setSubType(2L);
                this.addPlan(plan);
            }
        }
    }

    public void createStockTransfer() throws BizCheckedException{
        this.createScrap();
        this.createReturn();
    }
}
