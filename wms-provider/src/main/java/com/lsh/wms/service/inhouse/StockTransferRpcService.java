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

        //TODO
        //get available location
        BaseinfoLocation toLocation = locationService.getLocation(toLocationId);
        if (toLocation == null) {
            throw new BizCheckedException("2060012");
        }
        if(toLocation.getCanUse() == 2) {
            toLocation = core.getNearestLocation(toLocation);
            toLocationId = toLocation.getLocationId();
        }
        if (locationService.checkLocationLockStatus(toLocationId)) {
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
        if (plan.getSubType() == 2) {
            containerId = containerService.createContainerByType(2L).getId();
        }
        TaskEntry taskEntry = new TaskEntry();
        TaskInfo taskInfo = new TaskInfo();
        ObjUtils.bean2bean(plan, taskInfo);
        taskInfo.setTaskName("移库任务[ " + taskInfo.getFromLocationId() + " => " + taskInfo.getToLocationId() + "]");
        taskInfo.setType(TaskConstant.TYPE_STOCK_TRANSFER);
        taskInfo.setContainerId(containerId);
        taskInfo.setExt1(RandomUtils.genId() % 100);
        taskEntry.setTaskInfo(taskInfo);
        Long taskId = taskRpcService.create(TaskConstant.TYPE_STOCK_TRANSFER, taskEntry);
        logger.info("taskId: " + taskId);
    }

    public void updatePlan(StockTransferPlan plan)  throws BizCheckedException {
        this.addPlan(plan);
        this.cancelPlan(plan.getTaskId());
    }

    public void cancelPlan(Long taskId){
        taskRpcService.cancel(taskId);
    }

    public Map<String, Object> scanFromLocation(Map<String, Object> params) throws BizCheckedException {
        Long taskId = Long.valueOf(params.get("taskId").toString());
        TaskInfo taskInfo = taskRpcService.getTaskEntryById(taskId).getTaskInfo();
        Map<String, Object> mapQurey = new HashMap<String, Object>();
        mapQurey.put("status", TaskConstant.Assigned);
        mapQurey.put("operator", taskInfo.getOperator());
        List<TaskEntry> entryList = taskRpcService.getTaskList(TaskConstant.TYPE_STOCK_TRANSFER, mapQurey);
        Map<String, Object> next = new HashMap<String, Object>();
        Long nextLocationId;
        if (entryList == null) {
            throw new BizCheckedException("2550016");
        } else {
            Long nextOutTask = core.getNextOutbound(entryList);
            if (!nextOutTask.equals(taskId)) {
                throw new BizCheckedException("2550018");
            }
            core.outbound(params);
            //inbound
            if (entryList.size() == 1) {
                mapQurey.put("status", TaskConstant.Doing);
                List<TaskEntry> list = taskRpcService.getTaskList(TaskConstant.TYPE_STOCK_TRANSFER, mapQurey);
                Long nextInTask = core.getNextInbound(list);
                TaskInfo nextInfo = taskRpcService.getTaskEntryById(nextInTask).getTaskInfo();
                nextLocationId = nextInfo.getToLocationId();
                next.put("type", 2);
                next.put("taskId",nextInTask);
            } else {
                mapQurey.put("status", TaskConstant.Assigned);
                entryList = taskRpcService.getTaskList(TaskConstant.TYPE_STOCK_TRANSFER, mapQurey);
                nextOutTask = core.getNextOutbound(entryList);
                TaskInfo nextInfo = taskRpcService.getTaskEntryById(nextOutTask).getTaskInfo();
                nextLocationId = nextInfo.getFromLocationId();
                next.put("type", 1);
                next.put("taskId", nextOutTask);
            }
            next.put("locationId", nextLocationId);
            next.put("locationCode", locationService.getLocation(nextLocationId).getLocationCode());
            return next;
        }
    }

    public Map<String, Object> scanToLocation(Map<String, Object> params) throws BizCheckedException {
        Long taskId = Long.valueOf(params.get("taskId").toString());
        TaskInfo taskInfo = taskRpcService.getTaskEntryById(taskId).getTaskInfo();
        Map<String, Object> mapQurey = new HashMap<String, Object>();
        mapQurey.put("status", TaskConstant.Doing);
        mapQurey.put("operator", taskInfo.getOperator());
        List<TaskEntry> entryList = taskRpcService.getTaskList(TaskConstant.TYPE_STOCK_TRANSFER, mapQurey);
        if (entryList == null) {
            throw new BizCheckedException("2550017");
        } else {
            if (!entryList.get(0).getTaskInfo().getTaskId().equals(taskId)) {
                throw new BizCheckedException("2550019");
            }
            core.inbound(params);
            Map<String, Object> next = new HashMap<String, Object>();
            if (entryList.size() == 1) {
                next.put("finished", true);
            } else {
                Long nextLocationId = entryList.get(1).getTaskInfo().getToLocationId();
                next.put("type", 2);
                next.put("taskId", entryList.get(1).getTaskInfo().getToLocationId());
                next.put("locationId", nextLocationId);
                next.put("locationCode", locationService.getLocation(nextLocationId).getLocationCode());
            }
            return next;
        }
    }

    public Long assign(Long staffId) throws BizCheckedException {
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("status", TaskConstant.Doing);
        mapQuery.put("operator", staffId);
        List<TaskEntry> list = taskRpcService.getTaskList(TaskConstant.TYPE_STOCK_TRANSFER, mapQuery);
        //task exist
        if (!list.isEmpty()) {
            mapQuery.put("status", TaskConstant.Assigned);
            List<TaskEntry> entryList = taskRpcService.getTaskList(TaskConstant.TYPE_STOCK_TRANSFER, mapQuery);
            if (!entryList.isEmpty()) {
                return core.getNextOutbound(entryList);
            }
            return core.getNextInbound(list);
        }
        //get new task
        mapQuery.clear();
        mapQuery.put("status", TaskConstant.Draft);
        list = taskRpcService.getTaskList(TaskConstant.TYPE_STOCK_TRANSFER, mapQuery);
        if (list.isEmpty()) {
            return 0L;
        }
        taskRpcService.assign(list.get(0).getTaskInfo().getTaskId(), staffId);
        return list.get(0).getTaskInfo().getTaskId();
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
                if (quant.getPackName().toLowerCase().compareTo("ea") == 0) {
                    plan.setSubType(1L);
                } else {
                    plan.setSubType(2L);
                }
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
                if (quant.getPackName().toLowerCase().compareTo("ea") == 0) {
                    plan.setSubType(1L);
                } else {
                    plan.setSubType(2L);
                }
                this.addPlan(plan);
            }
        }
    }

    public void createStockTransfer() throws BizCheckedException{
        this.createScrap();
        this.createReturn();
    }
}
