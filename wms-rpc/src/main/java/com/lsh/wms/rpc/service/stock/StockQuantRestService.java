package com.lsh.wms.rpc.service.stock;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.alibaba.fastjson.JSON;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.BeanMapTransUtils;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.stock.StockLotService;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.stock.StockLot;
import com.lsh.wms.model.stock.StockQuant;
import com.lsh.wms.api.service.stock.IStockQuantRestService;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.model.stock.StockQuantMoveRel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.parsing.Location;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by mali on 16/6/29.
 */
@Service(protocol = "rest")
@Path("stock_quant")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class StockQuantRestService implements IStockQuantRestService {

    private static Logger logger = LoggerFactory.getLogger(StockQuantRestService.class);

    @Autowired
    private StockQuantService stockQuantService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private StockLotService stockLotService;

    @Autowired
    private ItemService itemService;

    @GET
    @Path("getOnhandQty")
    public String getOnhandQty(@QueryParam("skuId") Long skuId,
                               @QueryParam("locationId") Long locationId,
                               @QueryParam("ownerId") Long ownerId) {
        HashMap<String, Object> condition = new HashMap<String, Object>();
        condition.put("skuId", skuId);
        condition.put("ownerId", ownerId);
        List<Long> locationList = locationService.getStoreLocationIds(locationId);
        condition.put("locationList", locationList);
        List<StockQuant> quantList = stockQuantService.getQuants(condition);

        BigDecimal total = BigDecimal.ZERO;
        for (StockQuant quant : quantList) {
            total = total.add(quant.getQty());
        }
        return JsonUtils.SUCCESS(total);
    }

    @POST
    @Path("getList")
    public String getList(Map<String, Object> mapQuery) {
        List<Long> locationList = locationService.getStoreLocationIds(Long.parseLong(mapQuery.get("locationId").toString()));
        mapQuery.put("locationList", locationList);
        mapQuery.remove("locationId");
        List<StockQuant> quantList = stockQuantService.getQuants(mapQuery);
        return JsonUtils.SUCCESS(quantList);
    }

    /***
     * skuId 商品码
     * locationId 存储位id
     * containerId 容器设备id
     * qty 商品数量
     * supplierId 货物供应商id
     * ownerId 货物所属公司id
     * inDate 入库时间
     * expireDate 保质期失效时间
     * itemId
     * packUnit 包装单位
     * packName 包装名称
     */
    @POST
    @Path("create")
    public String create(Map<String, Object> mapInput) {
        StockQuant quant = BeanMapTransUtils.map2Bean(mapInput, StockQuant.class);
        try {
            stockQuantService.create(quant);
        } catch (Exception ex) {
            logger.error(ex.getCause().getMessage());
            return JsonUtils.EXCEPTION_ERROR("create failed");
        }
        return JsonUtils.SUCCESS();
    }

    @POST
    @Path("freeze")
    public String freeze(Map<String, Object> mapCondition) throws BizCheckedException {
        List<StockQuant> quantList = stockQuantService.getQuants(mapCondition);
        BigDecimal requiredQty = new BigDecimal(mapCondition.get("qty").toString());

        for (StockQuant quant : quantList) {
            if(requiredQty.compareTo(BigDecimal.ZERO) == 0) break;
            // need > have
            if (requiredQty.compareTo(quant.getQty()) == 1) {
                stockQuantService.freeze(quant);
                requiredQty = requiredQty.subtract(quant.getQty());
            }
            else {
                // need < have
                if (requiredQty.compareTo(quant.getQty()) == -1) {
                    stockQuantService.split(quant, requiredQty);
                }
                stockQuantService.freeze(quant);
                requiredQty = BigDecimal.ZERO;
            }
        }
        if (requiredQty.compareTo(BigDecimal.ZERO) == 1) {
            throw new BizCheckedException("2550001", "商品数量不足");
        }
        return JsonUtils.SUCCESS();
    }

    @POST
    @Path("unfreeze")
    public String unFreeze(Map<String, Object> mapCondition) throws BizCheckedException {
        List<StockQuant> quantList = stockQuantService.getQuants(mapCondition);
        BigDecimal requiredQty = new BigDecimal(mapCondition.get("qty").toString());

        for (StockQuant quant : quantList) {
            if(requiredQty.compareTo(BigDecimal.ZERO) == 0) break;
            // need > have
            if (requiredQty.compareTo(quant.getQty()) == 1) {
                stockQuantService.unFreeze(quant);
                requiredQty = requiredQty.subtract(quant.getQty());
            }
            else {
                // need < have
                if (requiredQty.compareTo(quant.getQty()) == -1) {
                    stockQuantService.split(quant, requiredQty);
                }
                stockQuantService.unFreeze(quant);
                requiredQty = BigDecimal.ZERO;
            }
        }
        if (requiredQty.compareTo(BigDecimal.ZERO) == 1) {
            throw new BizCheckedException("2550001", "商品数量不足");
        }
        return JsonUtils.SUCCESS();
    }

    @POST
    @Path("toDefect")
    public  String toDefect(Map<String, Object> mapCondition) throws BizCheckedException {
        List<StockQuant> quantList = stockQuantService.getQuants(mapCondition);
        BigDecimal requiredQty = new BigDecimal(mapCondition.get("qty").toString());

        for (StockQuant quant : quantList) {
            if(requiredQty.compareTo(BigDecimal.ZERO) == 0) break;
            // need > have
            if (requiredQty.compareTo(quant.getQty()) == 1) {
                stockQuantService.toDefect(quant);
                requiredQty = requiredQty.subtract(quant.getQty());
            }
            else {
                // need < have
                if (requiredQty.compareTo(quant.getQty()) == -1) {
                    stockQuantService.split(quant, requiredQty);
                }
                stockQuantService.toDefect(quant);
                requiredQty = BigDecimal.ZERO;
            }
        }
        if (requiredQty.compareTo(BigDecimal.ZERO) == 1) {
            throw new BizCheckedException("2550001", "商品数量不足");
        }
        return JsonUtils.SUCCESS();
    }

    @POST
    @Path("toRefund")
    public String toRefund(Map<String, Object> mapCondition) throws BizCheckedException {
        List<StockQuant> quantList = stockQuantService.getQuants(mapCondition);
        BigDecimal requiredQty = new BigDecimal(mapCondition.get("qty").toString());

        for (StockQuant quant : quantList) {
            if(requiredQty.compareTo(BigDecimal.ZERO) == 0) break;
            // need > have
            if (requiredQty.compareTo(quant.getQty()) == 1) {
                stockQuantService.toRefund(quant);
                requiredQty = requiredQty.subtract(quant.getQty());
            }
            else {
                // need < have
                if (requiredQty.compareTo(quant.getQty()) == -1) {
                    stockQuantService.split(quant, requiredQty);
                }
                stockQuantService.toRefund(quant);
                requiredQty = BigDecimal.ZERO;
            }
        }
        if (requiredQty.compareTo(BigDecimal.ZERO) == 1) {
            throw new BizCheckedException("2550001", "商品数量不足");
        }
        return JsonUtils.SUCCESS();
    }

    @GET
    @Path("getHistory")
    public String getHistory(@QueryParam("quant_id") Long quant_id) {
        List<StockQuantMoveRel> moveRels=stockQuantService.getHistoryById(quant_id);
        return JsonUtils.SUCCESS(moveRels);
    }

    @POST
    @Path("getItemStockCount")
    public String getItemStockCount(Map<String, Object> mapQuery) {
        return JsonUtils.SUCCESS(itemService.countItem(mapQuery));
    }

    @POST
    @Path("getItemStockList")
    public String getItemStockList(Map<String, Object> mapQuery) {
        Map<Long, Map<String, BigDecimal>> itemQuant = new HashMap<Long, Map<String, BigDecimal>>();

        List<BaseinfoItem> itemList= itemService.searchItem(mapQuery);

        List<Long> locationList = locationService.getStoreLocationIds(locationService.getWarehouseLocationId());
        List<Long> locationListLoss = locationService.getStoreLocationIds(locationService.getInventoryLostLocationId());
        List<Long> locationListDefect = locationService.getStoreLocationIds(locationService.getDefectiveLocationId());
        List<Long> locationListRefund = locationService.getStoreLocationIds(locationService.getBackLocationId());

        BigDecimal total, freeze, loss, defect, refund;

        for (BaseinfoItem item : itemList) {
            Long itemId = item.getItemId();
            total = stockQuantService.getItemCount(itemId, locationList, true);
            freeze = stockQuantService.getItemCount(itemId, locationList, false);
            loss = stockQuantService.getItemCount(itemId, locationListLoss, true);
            defect = stockQuantService.getItemCount(itemId, locationListDefect, true);
            refund = stockQuantService.getItemCount(itemId, locationListRefund, true);

            BigDecimal reTotal = total.subtract(loss);
            BigDecimal normal = reTotal.subtract(defect.add(refund));
            BigDecimal available = normal.subtract(freeze);

            Map<String, BigDecimal> result = new HashMap<String, BigDecimal>();
            result.put("total", reTotal);
            result.put("available",available);
            result.put("freeze", freeze);
            result.put("defect", defect);
            result.put("refund", refund);
            itemQuant.put(itemId,result);
        }
        return JsonUtils.SUCCESS(itemQuant);
    }

    @POST
    @Path("getLocationStockCount")
    public String getLocationStockCount(Map<String, Object> mapQuery) {
        return JsonUtils.SUCCESS(stockQuantService.countStockQuant(mapQuery));
    }

    @POST
    @Path("getLocationStockList")
    public String getLocationStockList(Map<String, Object> mapQuery) {
        return JsonUtils.SUCCESS(stockQuantService.getQuants(mapQuery));
    }

}
