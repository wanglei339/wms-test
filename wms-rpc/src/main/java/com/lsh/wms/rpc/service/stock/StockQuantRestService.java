package com.lsh.wms.rpc.service.stock;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.BeanMapTransUtils;
import com.lsh.wms.api.service.stock.IStockQuantRestService;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.model.stock.StockQuant;
import com.lsh.wms.model.stock.StockQuantCondition;
import com.lsh.wms.model.stock.StockQuantMoveRel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
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
    private StockQuantRpcService stockQuantRpcService;

    @Autowired
    private StockQuantService stockQuantService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private ItemService itemService;

    @POST
    @Path("getOnhandQty")
    public String getOnhandQty(StockQuantCondition condition) throws BizCheckedException {
        BigDecimal total = stockQuantRpcService.getQty(condition);
        return JsonUtils.SUCCESS(total);
    }

    @POST
    @Path("getList")
    public String getList(StockQuantCondition condition) throws BizCheckedException {
        condition.setExcludeLocation("INVENTORY LOSS");
        List<StockQuant> quantList = stockQuantRpcService.getQuantList(condition);
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
        return JsonUtils.SUCCESS(stockQuantRpcService.freeze(mapCondition));
    }

    @POST
    @Path("unfreeze")
    public String unFreeze(Map<String, Object> mapCondition) throws BizCheckedException {
        return JsonUtils.SUCCESS(stockQuantRpcService.unfreeze(mapCondition));
    }

    @POST
    @Path("toDefect")
    public String toDefect(Map<String, Object> mapCondition) throws BizCheckedException {
        return JsonUtils.SUCCESS(stockQuantRpcService.toDefect(mapCondition));
    }

    @POST
    @Path("toRefund")
    public String toRefund(Map<String, Object> mapCondition) throws BizCheckedException {
        return JsonUtils.SUCCESS(stockQuantRpcService.toRefund(mapCondition));
    }

    @GET
    @Path("getHistory")
    public String getHistory(@QueryParam("quant_id") Long quant_id) {
        List<StockQuantMoveRel> moveRels = stockQuantService.getHistoryById(quant_id);
        return JsonUtils.SUCCESS(moveRels);
    }
    @GET
    @Path("writeOffQuant")
    public String writeOffQuant(@QueryParam("quantId") Long quantId,@QueryParam("realQty") BigDecimal realQty) {
        logger.info("--------"+quantId+"-------------"+realQty);
        stockQuantRpcService.writeOffQuant(quantId,realQty);
        return JsonUtils.SUCCESS();
    }

    @POST
    @Path("getItemStockCount")
    public String getItemStockCount(Map<String, Object> mapQuery) {
        return JsonUtils.SUCCESS(stockQuantRpcService.getItemStockCount(mapQuery));
    }

    @POST
    @Path("getItemStockList")
    public String getItemStockList(Map<String, Object> mapQuery) {
        return JsonUtils.SUCCESS(stockQuantRpcService.getItemStockList(mapQuery));
    }

    @POST
    @Path("getLocationStockCount")
    public String getLocationStockCount(Map<String, Object> mapQuery) {
        return JsonUtils.SUCCESS(stockQuantRpcService.getLocationStockCount(mapQuery));
    }

    @POST
    @Path("getLocationStockList")
    public String getLocationStockList(Map<String, Object> mapQuery) {
        return JsonUtils.SUCCESS(stockQuantRpcService.getLocationStockList(mapQuery));
    }

    @GET
    @Path("traceQuant")
    public String traceQuant(@QueryParam("quantId") Long quantId) {
        return JsonUtils.SUCCESS(stockQuantRpcService.traceQuant(quantId));
    }

}
