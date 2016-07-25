package com.lsh.wms.rpc.service.stock;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.alibaba.fastjson.JSON;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
     *
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
    public String freeze(Map<String, Object> mapCondition) {
        List<StockQuant> quantList = stockQuantService.getQuants(mapCondition);
        for (StockQuant quant : quantList) {
            stockQuantService.freeze(quant);
        }
        return JsonUtils.SUCCESS();
    }

    @POST
    @Path("unfreeze")
    public String unFreeze(Map<String, Object> mapCondition) {
        List<StockQuant> quantList = stockQuantService.getQuants(mapCondition);
        for (StockQuant quant : quantList) {
            stockQuantService.unFreeze(quant);
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

        Map<Long, BigDecimal> total = new HashMap<Long, BigDecimal>();
        Map<Long, BigDecimal> freeze = new HashMap<Long, BigDecimal>();
        Map<Long, BigDecimal> loss = new HashMap<Long, BigDecimal>();
        Map<Long, BigDecimal> defect = new HashMap<Long, BigDecimal>();
        Map<Long, BigDecimal> refund = new HashMap<Long, BigDecimal>();

        for (BaseinfoItem item : itemList) {

            Long itemId = item.getItemId();

            total = stockQuantService.getItemCount(itemId, locationList, false);
            freeze = stockQuantService.getItemCount(itemId, locationList, true);
            loss = stockQuantService.getItemCount(itemId, locationListLoss, false);
            defect = stockQuantService.getItemCount(itemId, locationListDefect, true);
            refund = stockQuantService.getItemCount(itemId, locationListRefund, true);

            Map<String, BigDecimal> result = new HashMap<String, BigDecimal>();
            result.put("total", total.get(itemId).subtract(loss.get(itemId)));
            result.put("freeze", freeze.get(itemId));
            result.put("loss", loss.get(itemId));
            result.put("defect", defect.get(itemId));
            result.put("refund", refund.get(itemId));

            itemQuant.put(itemId,result);
        }

        return JsonUtils.SUCCESS(itemQuant);
    }

    @POST
    @Path("getLocationStockCount")
    public String getLocationStockCount(Map<String, Object> mapQuery) {
        return JsonUtils.SUCCESS(locationService.getStoreLocationIds(locationService.getWarehouseLocationId()).size());
    }

    @POST
    @Path("getLocationStockList")
    public String getLocationStockList(Map<String, Object> mapQuery) {

        int pn = Integer.valueOf((mapQuery.get("start")).toString());
        int rn = Integer.valueOf((mapQuery.get("limit")).toString());
        Map<Long, List<StockQuant>> locationDetail = new HashMap<Long, List<StockQuant>>();

        List<Long> locationList = locationService.getStoreLocationIds(locationService.getWarehouseLocationId());
        List<Long> selectedLocationList = locationList.subList(pn, Math.min(rn, (locationList.size() - pn)));
        for (Long location : selectedLocationList) {
            locationDetail.put(location,stockQuantService.getQuantsByLocationId(location));
        }
        return JsonUtils.SUCCESS(locationDetail);
    }

}
