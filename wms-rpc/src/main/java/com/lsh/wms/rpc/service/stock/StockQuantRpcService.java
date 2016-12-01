package com.lsh.wms.rpc.service.stock;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.service.stock.IStockQuantRpcService;
import com.lsh.wms.core.constant.ContainerConstant;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.constant.WriteOffConstant;
import com.lsh.wms.core.service.container.ContainerService;
import com.lsh.wms.core.service.inventory.InventoryRedisService;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.stock.StockLotService;
import com.lsh.wms.core.service.stock.StockMoveService;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.core.service.stock.StockSummaryService;
import com.lsh.wms.core.service.taking.StockTakingService;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.stock.*;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mali on 16/7/28.
 */

@Service(protocol = "dubbo")
public class StockQuantRpcService implements IStockQuantRpcService {
    private static Logger logger = LoggerFactory.getLogger(StockQuantRpcService.class);

    @Autowired
    private StockQuantService quantService;

    @Autowired
    private StockMoveService moveService;
    @Autowired
    private StockTakingService stockTakingService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private ItemService itemService;
    @Autowired
    private ContainerService containerService;

    @Autowired
    private InventoryRedisService inventoryRedisService;
    @Autowired
    private StockLotService lotService;
    @Autowired
    private StockSummaryService stockSummaryService;

    private Map<String, Object> getQueryCondition(StockQuantCondition condition) throws BizCheckedException {
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        try {
            mapQuery = PropertyUtils.describe(condition);
        } catch (Exception e) {
            logger.error(e.getCause().getMessage());
            throw new BizCheckedException("3040001");
        }
        return mapQuery;
    }

    public BigDecimal getQty(StockQuantCondition condition) throws BizCheckedException {
        Map<String, Object> mapQuery = this.getQueryCondition(condition);
        BigDecimal total = quantService.getQty(mapQuery);
        return total;
    }

    public List<StockQuant> getQuantList(StockQuantCondition condition) throws BizCheckedException {
        Map<String, Object> mapQuery = this.getQueryCondition(condition);
        if (condition.getExcludeLocation() != null) {
            BaseinfoLocation location = locationService.getInventoryLostLocation();
            List<BaseinfoLocation> excludeLocationList = new ArrayList<BaseinfoLocation>();
            excludeLocationList.add(location);
            mapQuery.put("excludeLocationList", excludeLocationList);
        }
        List<StockQuant> quantList = quantService.getQuants(mapQuery);
        return quantList == null ? new ArrayList<StockQuant>() : quantList;
    }

    public String freeze(Map<String, Object> mapCondition) throws BizCheckedException {
        mapCondition.put("isFrozen", 0);
        mapCondition.put("isDefect", 0);
        mapCondition.put("isRefund", 0);
        BigDecimal total = quantService.getQty(mapCondition);
        BigDecimal requiredQty = BigDecimal.ZERO;
        if (mapCondition.get("qty") != null) {
            requiredQty = new BigDecimal(mapCondition.get("qty").toString());
        } else {
            requiredQty = total;
        }
        if (total.compareTo(requiredQty) < 0 || total.compareTo(BigDecimal.ZERO) == 0) {
            throw new BizCheckedException("2550001");
        }

        mapCondition.put("requiredQty", requiredQty);
        quantService.process(mapCondition, "freeze");

        return JsonUtils.SUCCESS();
    }

    public String unfreeze(Map<String, Object> mapCondition) throws BizCheckedException {
        mapCondition.put("canUnFreeze", true);
        BigDecimal total = quantService.getQty(mapCondition);
        BigDecimal requiredQty = BigDecimal.ZERO;
        if (mapCondition.get("qty") != null) {
            requiredQty = new BigDecimal(mapCondition.get("qty").toString());
        } else {
            requiredQty = total;
        }
        if (total.compareTo(requiredQty) == -1 || total.compareTo(BigDecimal.ZERO) == 0) {
            throw new BizCheckedException("2550001");
        }

        mapCondition.put("requiredQty", requiredQty);
        quantService.process(mapCondition, "unFreeze");

        return JsonUtils.SUCCESS();
    }

    public String toDefect(Map<String, Object> mapCondition) throws BizCheckedException {
        mapCondition.put("isDefect", 0);
        BigDecimal total = quantService.getQty(mapCondition);
        BigDecimal requiredQty = BigDecimal.ZERO;
        if (mapCondition.get("qty") != null) {
            requiredQty = new BigDecimal(mapCondition.get("qty").toString());
        } else {
            requiredQty = total;
        }

        if (total.compareTo(requiredQty) == -1 || total.compareTo(BigDecimal.ZERO) == 0) {
            throw new BizCheckedException("2550001");
        }
        mapCondition.put("requiredQty", requiredQty);
        quantService.process(mapCondition, "toDefect");

        return JsonUtils.SUCCESS();
    }

    public String toRefund(Map<String, Object> mapCondition) throws BizCheckedException {
        mapCondition.put("isRefund", 0);
        BigDecimal total = quantService.getQty(mapCondition);
        BigDecimal requiredQty = BigDecimal.ZERO;
        if (mapCondition.get("qty") != null) {
            requiredQty = new BigDecimal(mapCondition.get("qty").toString());
        } else {
            requiredQty = total;
        }
        if (total.compareTo(requiredQty) == -1 || total.compareTo(BigDecimal.ZERO) == 0) {
            throw new BizCheckedException("2550001");
        }
        mapCondition.put("requiredQty", requiredQty);
        quantService.process(mapCondition, "toRefund");

        return JsonUtils.SUCCESS();
    }
    public void writeOffQuant(Long quantId, BigDecimal realQty)throws BizCheckedException{
        StockQuant quant = quantService.getQuantById(quantId);
        StockMove move = new StockMove();
        StockLot lot = lotService.getStockLotByLotId(quant.getLotId());
        if(quant.getQty().compareTo(realQty) > 0) {
            move.setTaskId(WriteOffConstant.WRITE_OFF_TASK_ID);
            move.setSkuId(quant.getSkuId());
            move.setItemId(quant.getItemId());
            move.setOwnerId(quant.getOwnerId());
            move.setStatus(TaskConstant.Done);
            move.setLot(lot);

            move.setQty(quant.getQty().subtract(realQty));
            move.setFromLocationId(quant.getLocationId());
            move.setToLocationId(locationService.getInventoryLostLocation().getLocationId());
            move.setToContainerId(containerService.createContainerByType(ContainerConstant.CAGE).getContainerId());
        }else {
            move.setTaskId(WriteOffConstant.WRITE_OFF_TASK_ID);
            move.setSkuId(quant.getSkuId());
            move.setItemId(quant.getItemId());
            move.setOwnerId(quant.getOwnerId());
            move.setStatus(TaskConstant.Done);

            move.setQty(realQty.subtract(quant.getQty()));
            move.setFromLocationId(locationService.getInventoryLostLocation().getLocationId());
            move.setToLocationId(quant.getLocationId());
            move.setToContainerId(quant.getContainerId());
            move.setLot(lot);
        }
        stockTakingService.writeOffQuant(move,quant);
    }
    public int getItemStockCount(Map<String, Object> mapQuery) {
        return itemService.countItem(mapQuery);
    }

    public Map<Long, Map<String, BigDecimal>> getItemStockList(Map<String, Object> mapQuery) {
        Map<Long, Map<String, BigDecimal>> itemQuant = new HashMap<Long, Map<String, BigDecimal>>();
        Map<Long, BigDecimal> mapDefect = new HashMap<Long, BigDecimal>();
        Map<Long, BigDecimal> mapRefund = new HashMap<Long, BigDecimal>();

        List<BaseinfoItem> itemList = itemService.searchItem(mapQuery);
        List<Long> itemIdList = new ArrayList<Long>();
        for (BaseinfoItem item : itemList) {
            itemIdList.add(item.getItemId());
            mapDefect.put(item.getItemId(), BigDecimal.ZERO);
            mapRefund.put(item.getItemId(), BigDecimal.ZERO);
        }
        if (itemIdList.isEmpty()) {
            return itemQuant;
        }

        // get all defect quant
        HashMap<String, Object> mapCondition = new HashMap<String, Object>();
        mapCondition.put("itemList", itemIdList);
        mapCondition.put("locationId", locationService.getDefectiveLocation().getLocationId());
        List<StockQuant> quantList = quantService.getQuants(mapCondition);
        for (StockQuant quant : quantList) {
            Long itemId = quant.getItemId();
            BigDecimal qty = quant.getQty();
            mapDefect.put(itemId, mapDefect.get(itemId).add(qty));
        }

        // get all inventory loss quant
        mapCondition = new HashMap<String, Object>();
        mapCondition.put("itemList", itemIdList);
        mapCondition.put("locationId", locationService.getBackLocation().getLocationId());
        quantList = quantService.getQuants(mapCondition);
        for (StockQuant quant : quantList) {
            Long itemId = quant.getItemId();
            BigDecimal qty = quant.getQty();
            mapRefund.put(itemId, mapRefund.get(itemId).add(qty));
        }

        for (int i = 0; i < itemList.size(); i++) {
            Long itemId = itemIdList.get(i);
            StockSummary summary = stockSummaryService.getStockSummaryByItemId(itemId);
            Map<String, BigDecimal> result = new HashMap<String, BigDecimal>();
            result.put("total", summary == null ? BigDecimal.ZERO : summary.getInhouseQty());
            result.put("reserved", summary == null ? BigDecimal.ZERO : summary.getAllocQty());
            result.put("available", summary == null ? BigDecimal.ZERO : summary.getAvailQty());
            result.put("defect", mapDefect.get(itemId));
            result.put("refund", mapRefund.get(itemId));
            itemQuant.put(itemId, result);
        }
        return itemQuant;
    }
    public int getLocationStockCount(Map<String, Object> mapQuery) {
        return quantService.countStockQuant(mapQuery);
    }

    public List<StockQuant> getLocationStockList(Map<String, Object> mapQuery) {
        BaseinfoLocation location = locationService.getInventoryLostLocation();
        List<BaseinfoLocation> excludeLocationList = new ArrayList<BaseinfoLocation>();
        excludeLocationList.add(location);
        mapQuery.put("excludeLocationList", excludeLocationList);
        return quantService.getQuants(mapQuery);
    }

    public List<StockMove> traceQuant(Long quantId) {
        return moveService.traceQuant(quantId);
    }
}
