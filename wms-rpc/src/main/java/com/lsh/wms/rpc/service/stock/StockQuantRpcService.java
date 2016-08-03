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
import com.lsh.wms.model.stock.StockMove;
import com.lsh.wms.model.stock.StockQuant;
import com.lsh.wms.model.stock.StockQuantCondition;
import com.lsh.wms.model.task.TaskInfo;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.map.HashedMap;
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
    private LocationService locationService;

    @Autowired
    private ItemService itemService;

    private Map<String, Object> getQueryCondition(StockQuantCondition condition) throws BizCheckedException {
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        try {
            if (condition.getLocationId() != null) {
                List<Long> locationList = locationService.getStoreLocationIds(condition.getLocationId());
                condition.setLocationId(0L);
                condition.setLocationList(locationList);
            }
            mapQuery = PropertyUtils.describe(condition);
        } catch (Exception e) {
            logger.error(e.getCause().getMessage());
            throw new BizCheckedException("3040001");
        }
        return mapQuery;
    }

    public BigDecimal getQty(StockQuantCondition condition) throws BizCheckedException {
        Map<String, Object> mapQuery = this.getQueryCondition(condition);
        BigDecimal total =  quantService.getQty(mapQuery);
        return total;
    }


    public List<StockQuant> getQuantList(StockQuantCondition condition) throws BizCheckedException {
        Map<String, Object> mapQuery = this.getQueryCondition(condition);
        List<StockQuant> quantList =  quantService.getQuants(mapQuery);
        return quantList == null ? new ArrayList<StockQuant>() : quantList;
    }

    public List<StockQuant> reserveByTask(TaskInfo taskInfo) throws BizCheckedException {
        BigDecimal requiredQty =taskInfo.getQty();

        StockQuantCondition condition = new StockQuantCondition();
        condition.setLocationId(taskInfo.getFromLocationId());
        condition.setItemId(taskInfo.getItemId());
        Map<String, Object> mapQuery = this.getQueryCondition(condition);
        BigDecimal total = this.getQty(condition);
        if (total.compareTo(requiredQty) < 0) {
            throw new BizCheckedException("2550001");
        }
        return quantService.reserve(mapQuery, taskInfo.getTaskId(), requiredQty);
    }

    public void unReserve(Long taskId) {
        quantService.unReserve(taskId);
    }


    public List<StockQuant> reserveByContainer(Long containerId, Long taskId) throws BizCheckedException {
        return quantService.reserveByContainer(containerId, taskId);
    }

    public void reserveByContainer(Long containerId) {
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("containerId", containerId);
        List<StockQuant> quantList = quantService.getQuants(mapQuery);
    }

    public String freeze(Map<String, Object> mapCondition) throws BizCheckedException {
        BigDecimal requiredQty = new BigDecimal(mapCondition.get("qty").toString());
        mapCondition.put("isFrozen", 0);
        if ((quantService.getQty(mapCondition)).compareTo(requiredQty) == -1) {
            throw new BizCheckedException("2550001", "商品数量不足");
        }
        List<StockQuant> quantList = quantService.getQuants(mapCondition);
        for (StockQuant quant : quantList) {
            if (requiredQty.compareTo(BigDecimal.ZERO) == 0) break;
            // need > have
            if (requiredQty.compareTo(quant.getQty()) == 1) {
                quantService.freeze(quant);
                requiredQty = requiredQty.subtract(quant.getQty());
            } else {
                if (requiredQty.compareTo(quant.getQty()) == -1) {
                    quantService.split(quant, requiredQty);
                }
                quantService.freeze(quant);
                break;
            }
        }
        return JsonUtils.SUCCESS();
    }

    public String unfreeze(Map<String, Object> mapCondition) throws BizCheckedException {
        BigDecimal requiredQty = new BigDecimal(mapCondition.get("qty").toString());
        mapCondition.put("isFrozen", 1);
        if ((quantService.getQty(mapCondition)).compareTo(requiredQty) == -1) {
            throw new BizCheckedException("2550001", "商品数量不足");
        }
        List<StockQuant> quantList = quantService.getQuants(mapCondition);;
        for (StockQuant quant : quantList) {
            if (requiredQty.compareTo(BigDecimal.ZERO) == 0) break;
            // need > have
            if (requiredQty.compareTo(quant.getQty()) == 1) {
                quantService.unFreeze(quant);
                requiredQty = requiredQty.subtract(quant.getQty());
            } else {
                if (requiredQty.compareTo(quant.getQty()) == -1) {
                    quantService.split(quant, requiredQty);
                }
                quantService.unFreeze(quant);
                break;
            }
        }
        return JsonUtils.SUCCESS();
    }

    public String toDefect(Map<String, Object> mapCondition) throws BizCheckedException {
        BigDecimal requiredQty = new BigDecimal(mapCondition.get("qty").toString());
        mapCondition.put("isDefect", 0);
        if ((quantService.getQty(mapCondition)).compareTo(requiredQty) == -1) {
            throw new BizCheckedException("2550001", "商品数量不足");
        }
        List<StockQuant> quantList = quantService.getQuants(mapCondition);
        for (StockQuant quant : quantList) {
            if(requiredQty.compareTo(BigDecimal.ZERO) == 0) break;
            // need > have
            if (requiredQty.compareTo(quant.getQty()) == 1) {
                quantService.toDefect(quant);
                requiredQty = requiredQty.subtract(quant.getQty());
            }
            else {
                if (requiredQty.compareTo(quant.getQty()) == -1) {
                    quantService.split(quant, requiredQty);
                }
                quantService.toDefect(quant);
                break;
            }
        }
        return JsonUtils.SUCCESS();
    }

    public String toRefund(Map<String, Object> mapCondition) throws BizCheckedException {
        BigDecimal requiredQty = new BigDecimal(mapCondition.get("qty").toString());
        mapCondition.put("isRefund", 0);
        if ((quantService.getQty(mapCondition)).compareTo(requiredQty) == -1) {
            throw new BizCheckedException("2550001", "商品数量不足");
        }
        List<StockQuant> quantList = quantService.getQuants(mapCondition);
        for (StockQuant quant : quantList) {
            if(requiredQty.compareTo(BigDecimal.ZERO) == 0) break;
            // need > have
            if (requiredQty.compareTo(quant.getQty()) == 1) {
                quantService.toRefund(quant);
                requiredQty = requiredQty.subtract(quant.getQty());
            }
            else {
                if (requiredQty.compareTo(quant.getQty()) == -1) {
                    quantService.split(quant, requiredQty);
                }
                quantService.toRefund(quant);
                break;
            }
        }
        return JsonUtils.SUCCESS();
    }

    public int getItemStockCount(Map<String, Object> mapQuery) {
        return itemService.countItem(mapQuery);
    }

    public Map<Long, Map<String, BigDecimal>>getItemStockList(Map<String, Object> mapQuery) {
        Map<Long, Map<String, BigDecimal>> itemQuant = new HashMap<Long, Map<String, BigDecimal>>();
        HashMap<String, Object> mapCondition = new HashMap<String, Object>();

        List<BaseinfoItem> itemList= itemService.searchItem(mapQuery);

        List<Long> locationList = locationService.getStoreLocationIds(locationService.getWarehouseLocationId());
        List<Long> locationListLoss = locationService.getStoreLocationIds(locationService.getInventoryLostLocationId());

        BigDecimal total, freeze, loss, lossDefect, lossRefund, defect, refund;

        for (BaseinfoItem item : itemList) {
            Long itemId = item.getItemId();

            mapCondition.clear();
            mapCondition.put("itemId", itemId);
            mapCondition.put("locationList",locationList);
            total = quantService.getQty(mapCondition);

            mapCondition.clear();
            mapCondition.put("itemId", itemId);
            mapCondition.put("locationList",locationList);
            mapCondition.put("isNormal",false);
            freeze = quantService.getQty(mapCondition);

            mapCondition.clear();
            mapCondition.put("itemId", itemId);
            mapCondition.put("locationList", locationListLoss);
            loss = quantService.getQty(mapCondition);

            mapCondition.clear();
            mapCondition.put("itemId", itemId);
            mapCondition.put("locationList", locationListLoss);
            mapCondition.put("isDefect", 1);
            lossDefect = quantService.getQty(mapCondition);

            mapCondition.clear();
            mapCondition.put("itemId", itemId);
            mapCondition.put("locationList", locationListLoss);
            mapCondition.put("isRefund", 1);
            lossRefund = quantService.getQty(mapCondition);

            mapCondition.clear();
            mapCondition.put("itemId", itemId);
            mapCondition.put("locationList",locationList);
            mapCondition.put("isDefect", 1);
            defect = quantService.getQty(mapCondition);

            mapCondition.clear();
            mapCondition.put("itemId", itemId);
            mapCondition.put("locationList",locationList);
            mapCondition.put("isRefund", 1);
            refund = quantService.getQty(mapCondition);

            BigDecimal reTotal = total.subtract(loss);
            BigDecimal reDefect = defect.subtract(lossDefect);
            BigDecimal reRefund = refund.subtract(lossRefund);
            BigDecimal normal = reTotal.subtract(reDefect.add(reRefund));
            BigDecimal available = normal.subtract(freeze);

            Map<String, BigDecimal> result = new HashMap<String, BigDecimal>();
            result.put("total", reTotal);
            result.put("available",available);
            result.put("freeze", freeze);
            result.put("defect", reDefect);
            result.put("refund", reRefund);
            itemQuant.put(itemId,result);
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
