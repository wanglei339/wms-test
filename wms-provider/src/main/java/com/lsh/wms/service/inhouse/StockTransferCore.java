package com.lsh.wms.service.inhouse;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.wms.api.service.item.IItemRpcService;
import com.lsh.wms.api.service.stock.IStockMoveRpcService;
import com.lsh.wms.api.service.stock.IStockQuantRpcService;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.dao.task.TaskInfoDao;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.model.stock.StockMove;
import com.lsh.wms.model.stock.StockQuant;
import com.lsh.wms.model.stock.StockQuantCondition;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.model.transfer.StockTransferPlan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    public void fillTransferPlan(StockTransferPlan plan) throws BizCheckedException {

        if (plan.getPackName().equals("pallet")) {
            plan.setPackName(itemRpcService.getItem(plan.getItemId()).getPackName());
            StockQuantCondition condition = new StockQuantCondition();
            condition.setLocationId(plan.getFromLocationId());
            condition.setItemId(plan.getItemId());
            BigDecimal total = stockQuantRpcService.getQty(condition);
            StockQuant quant = stockQuantRpcService.getQuantList(condition).get(0);
            plan.setQty(total);
            plan.setPackUnit(quant.getPackUnit());
            plan.setPackName(quant.getPackName());
        } else {
            BigDecimal packUnit = itemRpcService.getPackUnit(plan.getPackName());
            plan.setPackUnit(packUnit);
            BigDecimal requiredQty = plan.getUomQty().multiply(packUnit);
            plan.setQty(requiredQty);
        }
    }

    public void outbound(Map<String, Object> params) throws BizCheckedException {
        Long taskId = Long.valueOf(params.get("taskId").toString());
        Long fromLocationId = Long.valueOf(params.get("locationId").toString());
        Long staffId = Long.valueOf(params.get("staffId").toString());

        TaskEntry taskEntry = taskRpcService.getTaskEntryById(taskId);
        if (taskEntry == null) {
            throw new BizCheckedException("3040001");
        }
        if (fromLocationId.compareTo(taskEntry.getTaskInfo().getFromLocationId()) != 0 ) {
            throw new BizCheckedException("2040005");
        }

        Long containerId = taskEntry.getTaskInfo().getContainerId();
        Long toLocationId = locationService.getAreaFatherId(fromLocationId);
        if (taskEntry.getTaskInfo().getPackName().equals("pallet")) {
            moveRpcService.moveWholeContainer(containerId, taskId, staffId, fromLocationId, toLocationId);

        } else {
            BigDecimal qtyDone = new BigDecimal(params.get("uomQty").toString());
            qtyDone = qtyDone.multiply(itemRpcService.getPackUnit(params.get("packName").toString()));
            StockMove move = new StockMove();
            ObjUtils.bean2bean(taskEntry.getTaskInfo(), move);
            move.setQty(qtyDone);
            move.setToLocationId(toLocationId);
            List<StockMove> moveList = new ArrayList<StockMove>();
            moveList.add(move);
            moveRpcService.move(moveList);

            if(taskEntry.getTaskInfo().getQty() != qtyDone) {
                taskEntry.getTaskInfo().setQtyDone(qtyDone);
            }
        }

        if(taskEntry.getTaskInfo().getFromLocationId() != fromLocationId) {
            taskEntry.getTaskInfo().setRealFromLocationId(fromLocationId);
        }
        taskInfoDao.update(taskEntry.getTaskInfo());
    }

    public void inbound(Map<String,Object> params) throws BizCheckedException {
        Long taskId = Long.valueOf(params.get("taskId").toString());
        Long toLocationId = Long.valueOf(params.get("locationId").toString());
        Long staffId = Long.valueOf(params.get("staffId").toString());

        TaskEntry taskEntry = taskRpcService.getTaskEntryById(taskId);
        if (taskEntry == null) {
            throw new BizCheckedException("3040001");
        }
        TaskInfo taskInfo = taskEntry.getTaskInfo();
        Long containerId = taskEntry.getTaskInfo().getContainerId();
        Long fromLocationId = locationService.getAreaFatherId(taskInfo.getFromLocationId());

        if (taskEntry.getTaskInfo().getPackName() == "pallet") {
            moveRpcService.moveWholeContainer(containerId, taskId, staffId, fromLocationId, toLocationId);
        } else {
            BigDecimal qtyDone = new BigDecimal(params.get("uomQty").toString());
            qtyDone = qtyDone.multiply(itemRpcService.getPackUnit(params.get("packName").toString()));
            StockMove move = new StockMove();
            ObjUtils.bean2bean(taskEntry.getTaskInfo(), move);
            move.setFromLocationId(fromLocationId);
            move.setToLocationId(toLocationId);
            List<StockMove> moveList = new ArrayList<StockMove>();
            moveList.add(move);
            moveRpcService.move(moveList);

            if(taskEntry.getTaskInfo().getQty() != qtyDone) {
                taskEntry.getTaskInfo().setQtyDone(qtyDone);
            }
        }
        taskRpcService.done(taskId);

        if(taskEntry.getTaskInfo().getToLocationId() != toLocationId) {
            taskEntry.getTaskInfo().setRealToLocationId(toLocationId);
        }
        taskInfoDao.update(taskEntry.getTaskInfo());
    }

}
