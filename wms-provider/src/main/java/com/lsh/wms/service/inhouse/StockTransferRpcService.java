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
import com.lsh.wms.model.so.ObdDetail;
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
import java.util.*;
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
        if (plan.getSubType() == 1) {
            //整托
            //计算数量和单位
            StockQuantCondition condition = new StockQuantCondition();
            condition.setLocationId(fromLocation.getLocationId());
            condition.setItemId(plan.getItemId());
            condition.setReserveTaskId(0L);
            BigDecimal total = stockQuantRpcService.getQty(condition);
            List<StockQuant> quantList = stockQuantRpcService.getQuantList(condition);
            if (quantList.isEmpty()) {
                throw new BizCheckedException("2550032");
            }
            plan.setQty(total);
            plan.setPackUnit(quantList.get(0).getPackUnit());
            plan.setPackName(quantList.get(0).getPackName());
            plan.setUomQty(PackUtil.EAQty2UomQty(total, plan.getPackName()));
            plan.setContainerId(quantList.get(0).getContainerId());
        } else {
            plan.setQty(PackUtil.UomQty2EAQty(plan.getUomQty(), plan.getPackName()));
            plan.setPackUnit(PackUtil.Uom2PackUnit(plan.getPackName()));
        }
        //检查移出库位库存量
        core.checkFromLocation(plan.getItemId(), fromLocation, plan.getQty());
        //补充验证移出库位
        if(plan.getSubType() == 1){
            if(fromLocation.getRegionType() != LocationConstant.SHELFS || fromLocation.getBinUsage() != BinUsageConstant.BIN_UASGE_STORE){
                throw new BizCheckedException("2550047");
            }
        }
        if(toLocation != null) {
            //检查移入库位
            core.checkToLocation(plan.getItemId(), toLocation);
        }
        /*
        if (toLocation.getCanStore() != 1) {
            throw new BizCheckedException("2550020");
        }
        */
        Long containerId = plan.getContainerId();
        //移库单位是箱或ea时,生成新的托盘ID
        if (plan.getSubType() != 1) {
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
    }

    public void scanToLocation(TaskEntry taskEntry, BaseinfoLocation location) throws BizCheckedException {
        if(taskEntry.getTaskInfo().getSubType() == 1){
            if(location.getRegionType() != LocationConstant.SHELFS || location.getBinUsage() != BinUsageConstant.BIN_UASGE_STORE){
                throw new BizCheckedException("2550047");
            }
        }
        core.inbound(taskEntry, location);
    }

    public Long assign(Long staffId) throws BizCheckedException {
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("status", TaskConstant.Assigned);
        mapQuery.put("operator", staffId);
        List<TaskEntry> list = taskRpcService.getTaskList(TaskConstant.TYPE_STOCK_TRANSFER, mapQuery);
        //task exist
        if (!list.isEmpty()) {
            Collections.sort(list, new Comparator<TaskEntry>() {
                //此处可以设定一个排序规则,对波次中的订单优先级进行排序
                public int compare(TaskEntry o1, TaskEntry o2) {
                    return o2.getTaskInfo().getUpdatedAt().compareTo(o1.getTaskInfo().getUpdatedAt());
                }
            });
            return list.get(0).getTaskInfo().getTaskId();
        }
        //get new task
        mapQuery.clear();
        mapQuery.put("status", TaskConstant.Draft);
        list = taskRpcService.getTaskList(TaskConstant.TYPE_STOCK_TRANSFER, mapQuery);
        if (list.isEmpty()) {
            return 0L;
        }
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
