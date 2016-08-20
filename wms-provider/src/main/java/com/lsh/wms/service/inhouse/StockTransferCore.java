package com.lsh.wms.service.inhouse;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.wms.api.service.item.IItemRpcService;
import com.lsh.wms.api.service.stock.IStockMoveRpcService;
import com.lsh.wms.api.service.stock.IStockQuantRpcService;
import com.lsh.wms.api.service.system.ISysUserRpcService;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.dao.task.TaskInfoDao;
import com.lsh.wms.core.service.container.ContainerService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.stock.StockMove;
import com.lsh.wms.model.stock.StockQuant;
import com.lsh.wms.model.stock.StockQuantCondition;
import com.lsh.wms.model.system.SysUser;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.model.transfer.StockTransferPlan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by mali on 16/7/30.
 */
@Component
public class StockTransferCore {

    private static Logger logger = LoggerFactory.getLogger(StockTransferCore.class);

    @Reference
    private IItemRpcService itemRpcService;

    @Reference
    private IStockQuantRpcService stockQuantRpcService;

    @Reference
    private IStockMoveRpcService moveRpcService;

    @Reference
    private ITaskRpcService taskRpcService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private TaskInfoDao taskInfoDao;

    @Reference
    private ISysUserRpcService iSysUserRpcService;

    @Autowired
    private ContainerService containerService;

    public void fillTransferPlan(StockTransferPlan plan) throws BizCheckedException {
        StockQuantCondition condition = new StockQuantCondition();
        condition.setLocationId(plan.getFromLocationId());
        condition.setItemId(plan.getItemId());
        StockQuant quant = stockQuantRpcService.getQuantList(condition).get(0);
        plan.setPackUnit(quant.getPackUnit());
        plan.setPackName(quant.getPackName());
        if (plan.getSubType().compareTo(1L)==0) {
            BigDecimal total = stockQuantRpcService.getQty(condition);
            plan.setQty(total);
        } else {
            BigDecimal requiredQty = plan.getUomQty().multiply(quant.getPackUnit());
            plan.setQty(requiredQty);
        }
    }

    public void outbound(Map<String, Object> params) throws BizCheckedException {
        Long staffId = 0L;
        Long taskId = Long.valueOf(params.get("taskId").toString());
        Long fromLocationId = Long.valueOf(params.get("locationId").toString());
        try {
            staffId= iSysUserRpcService.getSysUserById(Long.valueOf(params.get("uId").toString())).getStaffId();
        }catch (Exception e){
            throw new BizCheckedException("2550013");
        }
        TaskEntry taskEntry = taskRpcService.getTaskEntryById(taskId);
        if (taskEntry == null) {
            throw new BizCheckedException("2550005");
        }
        StockQuantCondition condition = new StockQuantCondition();
        condition.setLocationId(fromLocationId);
        condition.setItemId(taskEntry.getTaskInfo().getItemId());
        List<StockQuant> quants = stockQuantRpcService.getQuantList(condition);

        TaskInfo taskInfo = taskEntry.getTaskInfo();
        if(taskInfo.getType().compareTo(TaskConstant.TYPE_ATTIC_SHELVE)==0){
            taskInfo.setExt4(1L);
            if(quants == null || quants.size()==0){
                throw new BizCheckedException("2550008");
            }
            StockQuant quant = quants.get(0);
            if(quant.getItemId().compareTo(taskInfo.getItemId())!=0)
                throw new BizCheckedException("2040005");{
            }
        }else {
            if (taskInfo.getFromLocationId().compareTo(fromLocationId) != 0) {
                throw new BizCheckedException("2040005");
            }
        }
        Long containerId = taskInfo.getContainerId();
        Long toLocationId = locationService.getWarehouseLocationId();
        if (taskInfo.getSubType().compareTo(1L)==0) {
            moveRpcService.moveWholeContainer(containerId, taskId, staffId, fromLocationId, toLocationId);
        } else {

            BigDecimal qtyDone = new BigDecimal(params.get("uomQty").toString()).multiply(taskInfo.getPackUnit());
            if(taskInfo.getQty().compareTo(qtyDone) < 0){
                throw new BizCheckedException("2550008");
            }
            StockMove move = new StockMove();
            ObjUtils.bean2bean(taskInfo, move);
            move.setQty(qtyDone);
            move.setToLocationId(toLocationId);
            move.setFromContainerId(quants.get(0).getContainerId());
            move.setToContainerId(taskInfo.getContainerId());
            List<StockMove> moveList = new ArrayList<StockMove>();
            moveList.add(move);
            moveRpcService.move(moveList);
            taskInfo.setQtyDone(qtyDone);
        }
        //taskInfo.setStatus(TaskConstant.Doing);
        taskInfo.setExt3(1L);
        taskInfoDao.update(taskInfo);
    }

    public void inbound(Map<String,Object> params) throws BizCheckedException {
        Long staffId = 0L;
        Long taskId = Long.valueOf(params.get("taskId").toString());
        Long toLocationId = Long.valueOf(params.get("locationId").toString());
        try {
            staffId= iSysUserRpcService.getSysUserById(Long.valueOf(params.get("uId").toString())).getStaffId();
        }catch (Exception e){
            throw new BizCheckedException("2550013");
        }
        TaskEntry taskEntry = taskRpcService.getTaskEntryById(taskId);
        if (taskEntry == null) {
            throw new BizCheckedException("2550005");
        }
        TaskInfo taskInfo = taskEntry.getTaskInfo();
        if(taskInfo.getToLocationId().compareTo(toLocationId) != 0) {
            throw new BizCheckedException("2040007");
        }
        Long containerId = taskInfo.getContainerId();
        Long fromLocationId = locationService.getWarehouseLocationId();
        if (taskInfo.getSubType().compareTo(1L)==0) {
            moveRpcService.moveWholeContainer(containerId, taskId, staffId, fromLocationId, toLocationId);
        } else {
            BigDecimal qtyDone = new BigDecimal(params.get("uomQty").toString()).multiply(taskInfo.getPackUnit());
            if(taskInfo.getQtyDone().compareTo(qtyDone) != 0){
                throw new BizCheckedException("2550014");
            }
            StockMove move = new StockMove();
            ObjUtils.bean2bean(taskInfo, move);
            move.setQty(qtyDone);
            move.setFromLocationId(fromLocationId);
            move.setToLocationId(toLocationId);
            move.setFromContainerId(containerId);
            StockQuantCondition condition = new StockQuantCondition();
            condition.setLocationId(toLocationId);
            condition.setItemId(taskInfo.getItemId());
            List<StockQuant> quants = stockQuantRpcService.getQuantList(condition);
            Long toContainerId;
            if (quants == null || quants.size() == 0) {
                toContainerId = containerService.createContainerByType(1L).getContainerId();
            } else {
                toContainerId = quants.get(0).getContainerId();
            }
            move.setToContainerId(toContainerId);
            move.setSkuId(taskInfo.getSkuId());
            move.setOwnerId(taskInfo.getOwnerId());
            List<StockMove> moveList = new ArrayList<StockMove>();
            moveList.add(move);
            moveRpcService.move(moveList);
        }
        taskRpcService.done(taskId);
    }

    public void sortOutbound(List<TaskEntry> entryList) {
        Collections.sort(entryList, new Comparator<TaskEntry>() {
            public int compare (TaskEntry entry1, TaskEntry entry2) {
                try {
                    TaskInfo info1 = entry1.getTaskInfo(), info2 = entry2.getTaskInfo();
                    //sort fromLocationId
                    if (info1.getFromLocationId().compareTo(info2.getFromLocationId()) < 0) {
                        return 1;
                    } else {
                        return 0;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        });
        Long order = 1L;
        for (TaskEntry entry : entryList) {
            entry.getTaskInfo().setExt1(order);
            order ++;
            taskInfoDao.update(entry.getTaskInfo());
        }
    }

    public void sortInbound(List<TaskEntry> entryList) {
        Collections.sort(entryList, new Comparator<TaskEntry>() {
            public int compare (TaskEntry entry1, TaskEntry entry2) {
                try {
                    TaskInfo info1 = entry1.getTaskInfo(), info2 = entry2.getTaskInfo();
                    //sort toLocationId
                    if (info1.getToLocationId().compareTo(info2.getToLocationId()) < 0) {
                        return 1;
                    } else {
                        return 0;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        });
        Long order = 1L;
        for (TaskEntry entry : entryList) {
            entry.getTaskInfo().setExt2(order);
            order ++;
            taskInfoDao.update(entry.getTaskInfo());
        }

    }

    public Long getFirstOutbound(Long staffId) {
        Map<String ,Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("status", TaskConstant.Assigned);
        mapQuery.put("operator", staffId);
        mapQuery.put("ext1", 1);
        List<TaskEntry> entryList = taskRpcService.getTaskList(TaskConstant.TYPE_STOCK_TRANSFER, mapQuery);
        return entryList.get(0).getTaskInfo().getTaskId();
    }

    public Long getFirstInbound(Long staffId) {
        Map<String ,Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("operator", staffId);
        mapQuery.put("status", TaskConstant.Assigned);
        mapQuery.put("ext2", 1);
        List<TaskEntry> entryList = taskRpcService.getTaskList(TaskConstant.TYPE_STOCK_TRANSFER, mapQuery);
        return entryList.get(0).getTaskInfo().getTaskId();
    }


    public Long getNextOutbound(TaskEntry entry) {
        Long ext1 = entry.getTaskInfo().getExt1();
        Long operator = entry.getTaskInfo().getOperator();
        Map<String ,Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("status", TaskConstant.Assigned);
        mapQuery.put("ext1", ext1 + 1);
        mapQuery.put("operator", operator);
        List<TaskEntry> entryList = taskRpcService.getTaskList(TaskConstant.TYPE_STOCK_TRANSFER, mapQuery);
        if (entryList.isEmpty()) {
            return 0L;
        }
        return entryList.get(0).getTaskInfo().getTaskId();
    }

    public Long getNextInbound(TaskEntry entry) {
        Long ext2 = entry.getTaskInfo().getExt2();
        Long operator = entry.getTaskInfo().getOperator();
        Map<String ,Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("status", TaskConstant.Assigned);
        mapQuery.put("operator", operator);
        mapQuery.put("ext2", ext2 + 1);
        List<TaskEntry> entryList = taskRpcService.getTaskList(TaskConstant.TYPE_STOCK_TRANSFER, mapQuery);
        if (entryList.isEmpty()) {
            return 0L;
        }
        return entryList.get(0).getTaskInfo().getTaskId();
    }

    //TODO
    public BaseinfoLocation getNearestLocation(BaseinfoLocation currentLocation) {
        BaseinfoLocation targetLocation;
        targetLocation = currentLocation;
        return targetLocation;
    }

    //TODO
    public Long getNearestTask(List<TaskEntry> entryList) {
        return entryList.get(0).getTaskInfo().getTaskId();
    }
}
