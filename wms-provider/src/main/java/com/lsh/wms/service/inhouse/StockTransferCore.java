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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        TaskInfo taskInfo = taskEntry.getTaskInfo();
        if(taskInfo.getFromLocationId().compareTo(fromLocationId) != 0) {
            throw new BizCheckedException("2040005");
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
            List<StockMove> moveList = new ArrayList<StockMove>();
            moveList.add(move);
            moveRpcService.move(moveList);
            taskInfo.setQtyDone(qtyDone);
        }
        taskInfo.setStatus(TaskConstant.Doing);
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
            move.setToContainerId(containerId);
            move.setSkuId(taskInfo.getSkuId());
            move.setOwnerId(taskInfo.getOwnerId());
            List<StockMove> moveList = new ArrayList<StockMove>();
            moveList.add(move);
            moveRpcService.move(moveList);
        }
        taskRpcService.done(taskId);
    }

    public Long getNextOutbound(List<TaskEntry> entryList) {
        Long taskId = entryList.get(0).getTaskInfo().getTaskId();
        Long ext = entryList.get(0).getTaskInfo().getExt1();
        for (TaskEntry entry : entryList){
            TaskInfo taskInfo = entry.getTaskInfo();
            if (taskInfo.getExt1() < ext) {
                taskId = taskInfo.getTaskId();
            }
        }
        return taskId;
    }

    public Long getNextInbound(List<TaskEntry> entryList) {
        Long taskId = entryList.get(0).getTaskInfo().getTaskId();
        Long ext = entryList.get(0).getTaskInfo().getExt1();
        for (TaskEntry entry : entryList){
            TaskInfo taskInfo = entry.getTaskInfo();
            if (taskInfo.getExt1() < ext) {
                taskId = taskInfo.getTaskId();
            }
        }
        return taskId;
    }

    //TODO
    public BaseinfoLocation getNearestLocation(BaseinfoLocation currentLocation) {
        BaseinfoLocation targetLocation;
        targetLocation = currentLocation;
        return targetLocation;
    }
}
