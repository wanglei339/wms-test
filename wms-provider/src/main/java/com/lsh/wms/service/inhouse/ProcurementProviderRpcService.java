package com.lsh.wms.service.inhouse;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.wms.api.service.inhouse.IProcurementProveiderRpcService;
import com.lsh.wms.api.service.inhouse.IProcurementRpcService;
import com.lsh.wms.api.service.stock.IStockQuantRpcService;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.service.container.ContainerService;
import com.lsh.wms.core.service.item.ItemLocationService;
import com.lsh.wms.core.service.location.LocationService;
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
import java.util.List;
import java.util.Map;

/**
 * Created by mali on 16/8/2.
 */
@Service(protocol = "dubbo")
public class ProcurementProviderRpcService implements IProcurementProveiderRpcService {
    private static final Logger logger = LoggerFactory.getLogger(ProcurementProviderRpcService.class);

    @Autowired
    private StockTransferCore core;

    @Reference
    private IStockQuantRpcService stockQuantService;

    @Reference
    private IProcurementRpcService rpcService;

    @Autowired
    private ContainerService containerService;

    @Reference
    private ITaskRpcService taskRpcService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private ItemLocationService itemLocationService;

    public void addProcurementPlan(StockTransferPlan plan) throws BizCheckedException {
        StockQuantCondition condition = new StockQuantCondition();
        condition.setLocationId(plan.getFromLocationId());
        condition.setItemId(plan.getItemId());
        BigDecimal total = stockQuantService.getQty(condition);
        core.fillTransferPlan(plan);

        if ( plan.getQty().compareTo(total) > 0) { // 移库要求的数量超出实际库存数量
            throw new BizCheckedException(plan.getQty().toString() + "====" + total.toString());
        }

        List<StockQuant> quantList = stockQuantService.getQuantList(condition);
        Long containerId = quantList.get(0).getContainerId();
        if (plan.getPackName() == "pallet") {
            containerId = containerService.createContainerByType(2L).getId();
        }

        TaskEntry taskEntry = new TaskEntry();
        TaskInfo taskInfo = new TaskInfo();
        ObjUtils.bean2bean(plan, taskInfo);
        taskInfo.setTaskName("补货任务[ " + taskInfo.getFromLocationId() + " => " + taskInfo.getToLocationId() + "]");
        taskInfo.setType(TaskConstant.TYPE_PROCUREMENT);
        taskInfo.setContainerId(containerId);
        taskEntry.setTaskInfo(taskInfo);
        taskRpcService.create(TaskConstant.TYPE_PROCUREMENT, taskEntry);
    }

    public void createProcurement() throws BizCheckedException {
        List<BaseinfoLocation> shelfLocationList = locationService.getLocationsByType("shelf_collection_bin");
        for (BaseinfoLocation shelf : shelfLocationList) {
            List<BaseinfoItemLocation> itemLocationList = itemLocationService.getItemLocationByLocationID(shelf.getLocationId());
            for (BaseinfoItemLocation itemLocation : itemLocationList) {
                if ( rpcService.needProcurement(itemLocation.getPickLocationid(), itemLocation.getItemId())) {
                    // 找合适的quant
                    StockQuantCondition condition = new StockQuantCondition();
                    List<BaseinfoLocation> shelfList = locationService.getLocationsByType("shelf");
                    List<Long> shelfIdList = locationService.getLocationIds(shelfList);
                    condition.setLocationList(shelfIdList);
                    condition.setItemId(itemLocation.getItemId());
                    List<StockQuant> quantList = stockQuantService.getQuantList(condition);
                    StockQuant quant = quantList.get(0);
                    // 创建任务
                    StockTransferPlan plan = new StockTransferPlan();
                    plan.setItemId(itemLocation.getItemId());
                    plan.setFromLocationId(quant.getLocationId());
                    plan.setToLocationId(itemLocation.getPickLocationid());
                    plan.setPackName("pallet");
                    plan.setUomQty(BigDecimal.ONE);
                    this.addProcurementPlan(plan);
                }
            }
        }

        List<BaseinfoLocation> loftPickLocationList = locationService.getLocationsByType("loft_collection_bin");
        for (BaseinfoLocation loftPick : loftPickLocationList) {
            List<BaseinfoItemLocation> itemLocationList = itemLocationService.getItemLocationByLocationID(loftPick.getLocationId());
            for (BaseinfoItemLocation itemLocation : itemLocationList) {
                if (rpcService.needProcurement(itemLocation.getPickLocationid(),itemLocation.getItemId())) {
                    // 找合适的quant
                    StockQuantCondition condition = new StockQuantCondition();
                    List<BaseinfoLocation> loftList = locationService.getLocationsByType("loft");
                    List<Long> loftIdList = locationService.getLocationIds(loftList);
                    condition.setLocationList(loftIdList);
                    condition.setItemId(itemLocation.getItemId());
                    List<StockQuant> quantList = stockQuantService.getQuantList(condition);
                    StockQuant quant = quantList.get(0);
                    // 创建任务
                    StockTransferPlan plan = new StockTransferPlan();
                    plan.setItemId(itemLocation.getItemId());
                    plan.setFromLocationId(quant.getLocationId());
                    plan.setToLocationId(itemLocation.getPickLocationid());
                    plan.setPackName(quant.getPackName());
                    plan.setUomQty(new BigDecimal("3"));
                    this.addProcurementPlan(plan);
                }
            }
        }
    }

    public void scanFromLocation(Map<String, Object> params) throws BizCheckedException {

    }

    public void scanToLocation(Map<String, Object> params) throws  BizCheckedException {

    }


}
