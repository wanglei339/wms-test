package com.lsh.wms.rpc.service.inhouse;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.wms.api.service.inhouse.IProcurementRpcService;
import com.lsh.wms.core.constant.ContainerConstant;
import com.lsh.wms.core.constant.LocationConstant;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.service.container.ContainerService;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.baseinfo.BaseinfoItemLocation;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.stock.StockQuant;
import com.lsh.wms.model.stock.StockQuantCondition;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.model.transfer.StockTransferPlan;
import com.lsh.wms.rpc.service.item.ItemRpcService;
import com.lsh.wms.rpc.service.location.LocationRpcService;
import com.lsh.wms.rpc.service.stock.StockQuantRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by mali on 16/7/30.
 */
@Service(protocol = "dubbo")
public class ProcurementRpcService implements IProcurementRpcService{
    @Autowired
    private StockQuantRpcService quantService;
    @Autowired
    private BaseTaskService baseTaskService;
    @Autowired
    private StockQuantRpcService stockQuantRpcService;
    @Autowired
    private StockQuantRpcService stockQuantService;
    @Autowired
    private ContainerService containerService;
    @Autowired
    private LocationRpcService locationRpcService;

    @Autowired
    private ItemRpcService itemRpcService;

    private static Logger logger = LoggerFactory.getLogger(ProcurementRpcService.class);


    @Autowired
    private ItemService itemService;

    public boolean needProcurement(Long locationId, Long itemId) throws BizCheckedException {
        StockQuantCondition condition = new StockQuantCondition();
        condition.setItemId(itemId);
        condition.setLocationId(locationId);
        BigDecimal qty = quantService.getQty(condition);
        if (qty.equals(BigDecimal.ZERO)) {
            return true;
        }
        StockQuant quant = quantService.getQuantList(condition).get(0);
        qty = qty.divide(quant.getPackUnit(),4);
        BaseinfoItem itemInfo = itemService.getItem(itemId);
        qty = qty.divide(itemInfo.getPackUnit(),4);
        if (itemInfo.getItemLevel() == 1) {
            return qty.compareTo(new BigDecimal(5.0)) >= 0 ? false : true;
        } else if (itemInfo.getItemLevel() == 2) {
            return qty.compareTo(new BigDecimal(3.0)) >= 0 ? false : true;
        } else if (itemInfo.getItemLevel() == 3) {
            return qty.compareTo(new BigDecimal(2.0)) >= 0 ? false : true;
        } else {
            return false;
        }
    }

    public BigDecimal getProcurementQty(BaseinfoItemLocation itemLocation) throws BizCheckedException {
        BigDecimal qty = BigDecimal.ZERO;
        if (itemLocation.getPickLocationid() == 0L) {
            // 阁楼去补货需要计算补货数量
        }
        return qty;
    }


    public TaskEntry addProcurementPlan(StockTransferPlan plan) throws BizCheckedException {
        if(!this.checkPlan(plan)){
            logger.error("error plan ：" + plan.toString());
            return null;
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

        this.fillTransferPlan(plan);

        if ( plan.getQty().compareTo(total) > 0) { // 移库要求的数量超出实际库存数量
            throw new BizCheckedException("2550008");
        }
        List<StockQuant> quantList = stockQuantService.getQuantList(condition);
        Long containerId = quantList.get(0).getContainerId();
        if (plan.getSubType().equals(2L)) {
            containerId = containerService.createContainerByType(ContainerConstant.CAGE).getContainerId();
        }

        ObjUtils.bean2bean(plan, taskInfo);
        taskInfo.setTaskName("补货任务[ " + taskInfo.getFromLocationId() + " => " + taskInfo.getToLocationId() + "]");
        taskInfo.setType(TaskConstant.TYPE_PROCUREMENT);
        taskInfo.setContainerId(containerId);
        taskEntry.setTaskInfo(taskInfo);
        return taskEntry;
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

}
