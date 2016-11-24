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
import com.lsh.wms.core.constant.BinUsageConstant;
import com.lsh.wms.core.constant.ContainerConstant;
import com.lsh.wms.core.constant.LocationConstant;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.dao.redis.RedisStringDao;
import com.lsh.wms.core.dao.task.TaskInfoDao;
import com.lsh.wms.core.service.container.ContainerService;
import com.lsh.wms.core.service.item.ItemLocationService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.core.service.utils.PackUtil;
import com.lsh.wms.model.baseinfo.BaseinfoItemLocation;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.stock.StockQuant;
import com.lsh.wms.model.stock.StockQuantCondition;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.model.transfer.StockTransferPlan;
import org.apache.tools.ant.taskdefs.Pack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by mali on 16/7/26.
 */

@Service(protocol = "dubbo")
public class StockTransferRpcService implements IStockTransferRpcService {
    private static final Logger logger = LoggerFactory.getLogger(StockTransferRpcService.class);

    @Reference
    private IStockQuantRpcService stockQuantRpcService;

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
    private TaskInfoDao taskInfoDao;

    @Autowired
    private RedisStringDao redisStringDao;

    public Long addPlan(StockTransferPlan plan) throws BizCheckedException {
        Long fromLocationId = plan.getFromLocationId(),
                toLocationId = plan.getToLocationId();
        if (fromLocationId.compareTo(toLocationId) == 0) {
            throw new BizCheckedException("2550017");
        }
        BaseinfoLocation fromLocation = locationService.getLocation(fromLocationId);
        BaseinfoLocation toLocation = toLocationId == 0 ? null : locationService.getLocation(toLocationId);
        if (fromLocation == null) {
            throw new BizCheckedException("2550016");
        }
        //检查移入库位
        plan.setQty(PackUtil.UomQty2EAQty(plan.getUomQty(), plan.getPackName()));
        plan.setPackUnit(PackUtil.Uom2PackUnit(plan.getPackName()));
        //检查移出库位库存量
        core.checkFromLocation(plan.getItemId(), fromLocation, plan.getQty());
        if(toLocation != null) {
            core.checkToLocation(plan.getItemId(), toLocation);
        }
        if (plan.getSubType().equals(1L)) {
            throw new BizCheckedException("2550040");
        }
        /*
        if (toLocation.getCanStore() != 1) {
            throw new BizCheckedException("2550020");
        }
        */
        Long containerId = plan.getContainerId();
        //移库单位是箱或ea时,生成新的托盘ID
        if (plan.getSubType().compareTo(2L) == 0 || plan.getSubType().compareTo(3L) == 0) {
            containerId = containerService.createContainerByType(ContainerConstant.PALLET).getContainerId();
        }
        TaskEntry taskEntry = new TaskEntry();
        TaskInfo taskInfo = new TaskInfo();
        ObjUtils.bean2bean(plan, taskInfo);
        taskInfo.setExt9(plan.getTargetDesc() == null ? "" : plan.getTargetDesc());
        taskInfo.setTaskName("移库任务[ " + taskInfo.getFromLocationId() + " => " + taskInfo.getToLocationId() + "]");
        taskInfo.setType(TaskConstant.TYPE_STOCK_TRANSFER);
        taskInfo.setContainerId(containerId);
        taskInfo.setQtyDone(taskInfo.getQty());
        taskInfo.setStep(1);
        taskInfo.setQtyUom(plan.getUomQty());
        taskInfo.setTaskPackQty(plan.getUomQty());
        taskInfo.setTaskQty(plan.getQty());
        taskInfo.setTaskEaQty(plan.getQty());
        taskInfo.setOwnerId(itemRpcService.getItem(plan.getItemId()).getOwnerId());
        taskEntry.setTaskInfo(taskInfo);
        return taskRpcService.create(TaskConstant.TYPE_STOCK_TRANSFER, taskEntry);
    }

    public void cancelPlan(Long taskId) {
        taskRpcService.cancel(taskId);
    }

    public void scanFromLocation(TaskEntry taskEntry,
                                                BaseinfoLocation location,
                                                BigDecimal uomQty) throws BizCheckedException {
        String uom = taskEntry.getTaskInfo().getPackName();
        if (!taskEntry.getTaskInfo().getFromLocationId().equals(location.getLocationId())) {
            throw new BizCheckedException("2550018");
        }
        if (uomQty.compareTo(BigDecimal.ZERO) == 0) {
            taskRpcService.cancel(taskEntry.getTaskInfo().getTaskId());
            taskEntry.getTaskInfo().setStatus(TaskConstant.Cancel);
        }
        core.outbound(taskEntry, location, uomQty, uom);
        /*
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
            next.put("taskId", nextInTask.toString());
        } else {//outbound
            TaskInfo nextInfo = taskRpcService.getTaskEntryById(nextOutTask).getTaskInfo();
            nextLocationId = nextInfo.getFromLocationId();
            nextItem = nextInfo.getItemId();
            packName = nextInfo.getPackName();
            subType = nextInfo.getSubType();
            qty = nextInfo.getQty();
            next.put("type", 1);
            next.put("taskId", nextOutTask.toString());
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
        */
    }

    public void scanToLocation(TaskEntry taskEntry, BaseinfoLocation location) throws BizCheckedException {
        core.inbound(taskEntry, location);
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
//        List<Long> taskList = core.getMoreTasks(list);
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
        next.put("taskId", nextInfo.getTaskId().toString());
        next.put("locationId", nextLocationId);
        next.put("locationCode", locationService.getLocation(nextLocationId).getLocationCode());
        next.put("itemId", nextInfo.getItemId());
        next.put("itemName", itemRpcService.getItem(nextInfo.getItemId()).getSkuName());
        next.put("packName", nextInfo.getPackName());
        next.put("uomQty", nextInfo.getQty());
        next.put("subType", nextInfo.getSubType());
        return next;
    }
}
