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
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.service.container.ContainerService;
import com.lsh.wms.core.service.item.ItemLocationService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.constant.LocationConstant;
import com.lsh.wms.core.service.stock.StockQuantService;
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

import javax.swing.*;
import javax.ws.rs.*;
import java.math.BigDecimal;
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

    @Reference
    private IStockQuantRpcService stockQuantService;

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

    @Autowired
    private ItemLocationService itemLocationService;

    public void addPlan(StockTransferPlan plan)  throws BizCheckedException {
        Long fromLocationId = plan.getFromLocationId(), toLocationId = plan.getToLocationId();
        List <StockQuant> fromQuants = quantService.getQuantsByLocationId(fromLocationId);
        List <StockQuant> toQuants = quantService.getQuantsByLocationId(toLocationId);
        BaseinfoLocation location = locationService.getLocation(toLocationId);

        if( (fromQuants.size() == 0) || (fromQuants.get(0).getItemId() != plan.getItemId()) ){
            throw new BizCheckedException("2550003");
        }

        if( toQuants.size() > 0 ){
            if(location.getType() == LocationConstant.LOFT_PICKING_BIN || location.getType() == LocationConstant.SHELF_PICKING_BIN){
                List<BaseinfoItemLocation> itemLocations = itemLocationService.getItemLocationByLocationID(toLocationId);
                if(itemLocations.get(0).getItemId() != fromQuants.get(0).getItemId()){
                    throw new BizCheckedException("2550004");
                }
            } else {
                if( (toQuants.get(0).getItemId() != fromQuants.get(0).getItemId()) || (toQuants.get(0).getLotId() != fromQuants.get(0).getLotId()) ){
                    throw new BizCheckedException("2550004");
                }
            }
        }

        StockQuantCondition condition = new StockQuantCondition();
        condition.setLocationId(plan.getFromLocationId());
        condition.setItemId(plan.getItemId());
        BigDecimal total = stockQuantService.getQty(condition);
        core.fillTransferPlan(plan);

        if ( plan.getQty().compareTo(total) > 0) { // 移库要求的数量超出实际库存数量
            throw new BizCheckedException("2550002","商品数量不足");
        }

        List<StockQuant> quantList = stockQuantService.getQuantList(condition);
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
        taskEntry.setTaskInfo(taskInfo);
        taskRpcService.create(TaskConstant.TYPE_STOCK_TRANSFER, taskEntry);
    }

    public void updatePlan(StockTransferPlan plan)  throws BizCheckedException {
        Long taskId = plan.getTaskId();
        taskRpcService.cancel(taskId);
        TaskEntry taskEntry = taskRpcService.getTaskEntryById(taskId);
        TaskInfo taskInfo = taskEntry.getTaskInfo();
        if(plan.getItemId() == 0) {
            plan.setItemId(taskInfo.getItemId());
        }
        if(plan.getUomQty() == BigDecimal.ZERO) {
            plan.setUomQty(taskInfo.getQtyUom());
        }
        if(plan.getSubType() == 2) {
            plan.setSubType(taskInfo.getSubType());
        }
        if(plan.getToLocationId() == 0 ) {
            plan.setToLocationId(taskInfo.getToLocationId());
        }
        plan.setFromLocationId(taskInfo.getFromLocationId());
        this.addPlan(plan);
    }

    public void cancelPlan(Long taskId){
        taskRpcService.cancel(taskId);
    }

    public void scanFromLocation(Map<String, Object> params) throws BizCheckedException {
        core.outbound(params);
    }

    public void scanToLocation(Map<String, Object> params) throws BizCheckedException {
        core.inbound(params);
        Long taskId = Long.valueOf(params.get("taskId").toString());
        taskRpcService.done(taskId);
    }

    public Long assign(Long staffId) throws BizCheckedException {
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("status", TaskConstant.Assigned);
        mapQuery.put("operator", staffId);
        List<TaskEntry> list = taskRpcService.getTaskList(TaskConstant.TYPE_STOCK_TRANSFER,mapQuery);
        if (list.isEmpty()) {
            mapQuery.clear();
            mapQuery.put("status", TaskConstant.Draft);
            list = taskRpcService.getTaskList(TaskConstant.TYPE_STOCK_TRANSFER, mapQuery);
            if(list.isEmpty()) {
                return 0L;
            }
        }
        taskRpcService.assign(list.get(0).getTaskInfo().getTaskId(), staffId);
        return list.get(0).getTaskInfo().getTaskId();

    }
}
