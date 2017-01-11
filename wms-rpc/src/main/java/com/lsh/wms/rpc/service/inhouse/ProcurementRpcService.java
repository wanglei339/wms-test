package com.lsh.wms.rpc.service.inhouse;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.wms.api.service.inhouse.IProcurementRpcService;
import com.lsh.wms.core.constant.BinUsageConstant;
import com.lsh.wms.core.constant.ContainerConstant;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.service.container.ContainerService;
import com.lsh.wms.core.service.item.ItemLocationService;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.location.BaseinfoLocationBinService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.core.service.wave.WaveService;
import com.lsh.wms.model.baseinfo.*;
import com.lsh.wms.model.procurement.NeedAndOutQty;
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
    private WaveService waveService;
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
    private BaseinfoLocationBinService locationBinService;
    @Autowired
    private ItemRpcService itemRpcService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private ItemLocationService itemLocationService;

    private static Logger logger = LoggerFactory.getLogger(ProcurementRpcService.class);
    //判断商品是否需要补货
    public boolean needProcurement(Long locationId, Long itemId,Boolean checkMax) throws BizCheckedException {

        BaseinfoItemLocation baseinfoItemLocation = itemLocationService.getItemLocationByLocationID(locationId).get(0);
        StockQuantCondition condition = new StockQuantCondition();
        condition.setItemId(itemId);
        condition.setLocationId(locationId);
        //获取该拣货位上的商品总数
        BigDecimal qty = quantService.getQty(condition);
        //数量为0必须补货
        if (qty.compareTo(BigDecimal.ZERO)==0) {
            return true;
        }

        //获取待捡货数量，如果待捡货数量小于待捡货数量，则生成捡货任务
        BigDecimal unPickedQty = waveService.getUnPickedQty(itemId);
        if(unPickedQty.compareTo(qty)>0){
            return true;
        }

        if(checkMax){
            if (baseinfoItemLocation.getMaxQty().compareTo(qty) > 0){
                return true;
            }else {
                return false;
            }
        }else {
            if (baseinfoItemLocation.getMinQty().compareTo(qty) > 0) {
                return true;
            } else {
                return false;
            }
        }
        //       BaseinfoItem itemInfo = itemService.getItem(itemId);
//        if (itemInfo.getItemLevel() == 1) {
//            return qty.compareTo(new BigDecimal(5.0)) >= 0 ? false : true;
//        } else if (itemInfo.getItemLevel() == 2) {
//            return qty.compareTo(new BigDecimal(3.0)) >= 0 ? false : true;
//        } else if (itemInfo.getItemLevel() == 3) {
//            return qty.compareTo(new BigDecimal(2.0)) >= 0 ? false : true;
//        } else {
//            return false;
//        }
        //获取该商品的存货范围
//        BaseinfoItemQuantRange range = itemService.getItemRange(itemId);
//        if(range==null){
//            return qty.compareTo(this.getThreshold(locationId, itemId)) < 0;
//        }else {
//            BigDecimal minQty = range.getMinQty();
//            return qty.compareTo(minQty) < 0;
//        }
    }
    //判断商品是否需要补货
    public boolean needProcurementForLoft(Long locationId, Long itemId,Boolean checkMax) throws BizCheckedException {

        BaseinfoItemLocation baseinfoItemLocation = itemLocationService.getItemLocationByLocationID(locationId).get(0);
        StockQuantCondition condition = new StockQuantCondition();
        condition.setItemId(itemId);
        condition.setLocationId(locationId);
        //获取该拣货位上的商品总数
        BigDecimal qty = quantService.getQty(condition);
        //数量为0必须补货
        if (qty.compareTo(BigDecimal.ZERO)==0) {
            return true;
        }

        if(checkMax){
            if (baseinfoItemLocation.getMaxQty().compareTo(qty) > 0){
                return true;
            }else {
                return false;
            }
        }else {
            if (baseinfoItemLocation.getMinQty().compareTo(qty) > 0) {
                return true;
            } else {
                return false;
            }
        }
        //       BaseinfoItem itemInfo = itemService.getItem(itemId);
//        if (itemInfo.getItemLevel() == 1) {
//            return qty.compareTo(new BigDecimal(5.0)) >= 0 ? false : true;
//        } else if (itemInfo.getItemLevel() == 2) {
//            return qty.compareTo(new BigDecimal(3.0)) >= 0 ? false : true;
//        } else if (itemInfo.getItemLevel() == 3) {
//            return qty.compareTo(new BigDecimal(2.0)) >= 0 ? false : true;
//        } else {
//            return false;
//        }
        //获取该商品的存货范围
//        BaseinfoItemQuantRange range = itemService.getItemRange(itemId);
//        if(range==null){
//            return qty.compareTo(this.getThreshold(locationId, itemId)) < 0;
//        }else {
//            BigDecimal minQty = range.getMinQty();
//            return qty.compareTo(minQty) < 0;
//        }
    }
    //判断商品是否需要补货
    public NeedAndOutQty returnNeedAndOutQty(Long locationId, Long itemId,Boolean checkMax) throws BizCheckedException {
        NeedAndOutQty needAndOutQty = new NeedAndOutQty();
        boolean needProcurement = false;

        BaseinfoItemLocation baseinfoItemLocation = itemLocationService.getItemLocationByLocationID(locationId).get(0);
        StockQuantCondition condition = new StockQuantCondition();
        condition.setItemId(itemId);
        condition.setLocationId(locationId);
        //获取该拣货位上的商品总数
        BigDecimal qty = quantService.getQty(condition);

        //获取待捡货数量，如果待捡货数量小于待捡货数量，则生成捡货任务
        BigDecimal unPickedQty = waveService.getUnPickedQty(itemId);

        needAndOutQty.setOutQty(unPickedQty);
        //数量为0必须补货
        if (qty.compareTo(BigDecimal.ZERO)==0) {
            needAndOutQty.setNeedProcurement(true);
            return needAndOutQty;
        }

        if(unPickedQty.compareTo(qty)>0){
            needAndOutQty.setNeedProcurement(true);
            return needAndOutQty;
        }

        if(checkMax){
            if (baseinfoItemLocation.getMaxQty().compareTo(qty) > 0){
                needAndOutQty.setNeedProcurement(true);
                return needAndOutQty;
            }else {
                needAndOutQty.setNeedProcurement(false);
                return needAndOutQty;
            }
        }else {
            if (baseinfoItemLocation.getMinQty().compareTo(qty) > 0) {
                needAndOutQty.setNeedProcurement(true);
                return needAndOutQty;
            } else {
                needAndOutQty.setNeedProcurement(false);
                return needAndOutQty;
            }
        }
        //  BaseinfoItem itemInfo = itemService.getItem(itemId);
//        if (itemInfo.getItemLevel() == 1) {
//            return qty.compareTo(new BigDecimal(5.0)) >= 0 ? false : true;
//        } else if (itemInfo.getItemLevel() == 2) {
//            return qty.compareTo(new BigDecimal(3.0)) >= 0 ? false : true;
//        } else if (itemInfo.getItemLevel() == 3) {
//            return qty.compareTo(new BigDecimal(2.0)) >= 0 ? false : true;
//        } else {
//            return false;
//        }
        //获取该商品的存货范围
//        BaseinfoItemQuantRange range = itemService.getItemRange(itemId);
//        if(range==null){
//            return qty.compareTo(this.getThreshold(locationId, itemId)) < 0;
//        }else {
//            BigDecimal minQty = range.getMinQty();
//            return qty.compareTo(minQty) < 0;
//        }
    }
    //判断商品是否需要补货
    public NeedAndOutQty returnNeedAndOutQtyForShelf(Long locationId, Long itemId,Boolean checkMax) throws BizCheckedException {
        NeedAndOutQty needAndOutQty = new NeedAndOutQty();
        boolean needProcurement = false;

        BaseinfoItemLocation baseinfoItemLocation = itemLocationService.getItemLocationByLocationID(locationId).get(0);
        StockQuantCondition condition = new StockQuantCondition();
        condition.setItemId(itemId);
        condition.setLocationId(locationId);
        //获取该拣货位上的商品总数
        BigDecimal qty = quantService.getQty(condition);

        //获取待捡货数量，如果待捡货数量小于待捡货数量，则生成捡货任务
        BigDecimal unPickedQty = waveService.getUnPickedQty(itemId);

        needAndOutQty.setOutQty(unPickedQty);
        //数量为0必须补货
        if (qty.compareTo(BigDecimal.ZERO)==0) {
            needAndOutQty.setNeedProcurement(true);
            return needAndOutQty;
        }

        if(unPickedQty.compareTo(qty)>0){
            needAndOutQty.setNeedProcurement(true);
            return needAndOutQty;
        }else {
            needAndOutQty.setNeedProcurement(false);
            return needAndOutQty;
        }

        //  BaseinfoItem itemInfo = itemService.getItem(itemId);
//        if (itemInfo.getItemLevel() == 1) {
//            return qty.compareTo(new BigDecimal(5.0)) >= 0 ? false : true;
//        } else if (itemInfo.getItemLevel() == 2) {
//            return qty.compareTo(new BigDecimal(3.0)) >= 0 ? false : true;
//        } else if (itemInfo.getItemLevel() == 3) {
//            return qty.compareTo(new BigDecimal(2.0)) >= 0 ? false : true;
//        } else {
//            return false;
//        }
        //获取该商品的存货范围
//        BaseinfoItemQuantRange range = itemService.getItemRange(itemId);
//        if(range==null){
//            return qty.compareTo(this.getThreshold(locationId, itemId)) < 0;
//        }else {
//            BigDecimal minQty = range.getMinQty();
//            return qty.compareTo(minQty) < 0;
//        }
    }

    public BigDecimal getProcurementQty(BaseinfoItemLocation itemLocation) throws BizCheckedException {
        BigDecimal qty = BigDecimal.ZERO;
        if (itemLocation.getPickLocationid() == 0L) {
            // 阁楼去补货需要计算补货数量
        }
        return qty;
    }
    //获取商品需要补货的临界值=库位存储数量-托盘存储数量
    public BigDecimal getThreshold(Long locationId, Long itemId) {

        BaseinfoLocationBin bin = (BaseinfoLocationBin) locationBinService.getBaseinfoItemLocationModelById(locationId);
        //获取仓位体积
        BigDecimal pickVolume = bin.getVolume();
        BaseinfoItem item = itemService.getItem(itemId);

        BigDecimal bulk = BigDecimal.ONE;
        BigDecimal sum = BigDecimal.ONE;
        //计算包装单位的体积
        bulk = bulk.multiply(item.getPackLength());
        bulk = bulk.multiply(item.getPackHeight());
        bulk = bulk.multiply(item.getPackWidth());
        //计算码盘存储数量
        sum = sum.multiply(new BigDecimal(item.getPileX()));
        sum = sum.multiply(new BigDecimal(item.getPileY()));
        sum = sum.multiply(new BigDecimal(item.getPileZ()));
        //计算库位能存多少商品
        BigDecimal num = BigDecimal.ZERO;
        if(bulk.compareTo(BigDecimal.ZERO)==0){
            num = pickVolume;
        }else {
            num = pickVolume.divide(bulk, 0, BigDecimal.ROUND_UP);
        }
        return num.subtract(sum);
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
                ( fromLocation.getRegionType().equals(toLocation.getRegionType()) && fromLocation.getBinUsage().equals(BinUsageConstant.BIN_UASGE_STORE) && toLocation.getBinUsage().equals(BinUsageConstant.BIN_UASGE_PICK))){
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
