package com.lsh.wms.rpc.service.stock;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.service.stock.IStockQuantRpcService;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.stock.StockMoveService;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.stock.StockQuant;
import com.lsh.wms.model.stock.StockQuantCondition;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.*;

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
    private LocationService locationService;

    @Autowired
    private ItemService itemService;

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

    public int getItemStockCount(Map<String, Object> mapQuery) {
        return itemService.countItem(mapQuery);
    }

    public Map<Long, Map<String, BigDecimal>> getItemStockList(Map<String, Object> mapQuery) {
        Map<Long, Map<String, BigDecimal>> itemQuant = new HashMap<Long, Map<String, BigDecimal>>();
        HashMap<String, Object> mapCondition = new HashMap<String, Object>();
        List<BaseinfoItem> itemList = itemService.searchItem(mapQuery);
        List<Long> itemIdList = new ArrayList<Long>();
        for (BaseinfoItem item : itemList) {
            itemIdList.add(item.getItemId());
        }
        mapCondition.put("itemList", itemIdList);
        // get all inventory record
        Long lossLocationId = locationService.getInventoryLostLocationId();
        mapCondition.put("locationId", lossLocationId);
        HashSet<Long> lossQuantSet = new HashSet<Long>();
        List<StockQuant> lossQuantList = quantService.getQuants(mapCondition);
        for (StockQuant quant : lossQuantList) {
            lossQuantSet.add(quant.getId());
        }
        // get all quant
        mapCondition.put("locationId", locationService.getWarehouseLocationId());
        List<StockQuant> quantList = quantService.getQuants(mapCondition);
        BigDecimal total, freeze, loss, lossDefect, lossRefund, defect, refund;
        Long isFrozen, reserveTaskId, isNormal, isDefect, isRefund;
        for (StockQuant quant : quantList) {
            total = BigDecimal.ZERO;
            loss = BigDecimal.ZERO;
            freeze = BigDecimal.ZERO;
            defect = BigDecimal.ZERO;
            refund = BigDecimal.ZERO;
            lossDefect = BigDecimal.ZERO;
            lossRefund = BigDecimal.ZERO;

            Long itemId = quant.getItemId();
            isFrozen = quant.getIsFrozen();
            reserveTaskId = quant.getReserveTaskId();
            isNormal = 1L;
            if (isFrozen == 1 || reserveTaskId != 0) {
                isNormal = 0L;
            }
            isDefect = quant.getIsDefect();
            isRefund = quant.getIsRefund();
            BigDecimal qty = quant.getQty();
            total = total.add(qty);
            if (isNormal == 0) {
                freeze = freeze.add(qty);
            }
            if (lossQuantSet.contains(quant.getId())) {
                loss = loss.add(qty);
                if (isDefect == 1) {
                    lossDefect = lossDefect.add(qty);
                } else if (isRefund == 1) {
                    lossRefund = lossRefund.add(qty);
                }
            }
            if (isDefect == 1) {
                defect = defect.add(qty);
            }
            if (isRefund == 1) {
                refund = refund.add(qty);
            }
            BigDecimal reTotal = total.subtract(loss);
            BigDecimal reDefect = defect.subtract(lossDefect);
            BigDecimal reRefund = refund.subtract(lossRefund);
            BigDecimal normal = reTotal.subtract(reDefect.add(reRefund));
            BigDecimal available = normal.subtract(freeze);
            Map<String, BigDecimal> item = itemQuant.get(itemId);
            if (item != null) {
                reTotal = item.get("total").add(reTotal);
                available = item.get("available").add(available);
                freeze = item.get("freeze").add(freeze);
                reDefect = item.get("defect").add(reDefect);
                reRefund = item.get("refund").add(reRefund);
            }
            Map<String, BigDecimal> result = new HashMap<String, BigDecimal>();
            result.put("total", reTotal);
            result.put("available", available);
            result.put("freeze", freeze);
            result.put("defect", reDefect);
            result.put("refund", reRefund);
            itemQuant.put(quant.getItemId(), result);
        }
        int size = itemList.size();
        for (int i = 0; i < size; i++) {
            Long itemId = itemIdList.get(i);
            if (itemQuant.get(itemId) == null) {
                Map<String, BigDecimal> result = new HashMap<String, BigDecimal>();
                result.put("total", BigDecimal.ZERO);
                result.put("available", BigDecimal.ZERO);
                result.put("freeze", BigDecimal.ZERO);
                result.put("defect", BigDecimal.ZERO);
                result.put("refund", BigDecimal.ZERO);
                itemQuant.put(itemId, result);
            }
        }

        return itemQuant;
    }

    public int getLocationStockCount(Map<String, Object> mapQuery) {
        return quantService.countStockQuant(mapQuery);
    }

    public List<StockQuant> getLocationStockList(Map<String, Object> mapQuery) {
        return quantService.getQuants(mapQuery);
    }
}
