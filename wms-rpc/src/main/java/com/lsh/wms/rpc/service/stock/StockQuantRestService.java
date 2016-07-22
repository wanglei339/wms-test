package com.lsh.wms.rpc.service.stock;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.alibaba.fastjson.JSON;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.BeanMapTransUtils;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.stock.StockLotService;
import com.lsh.wms.model.stock.StockLot;
import com.lsh.wms.model.stock.StockQuant;
import com.lsh.wms.api.service.stock.IStockQuantRestService;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.model.stock.StockQuantMoveRel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.util.HashMap;
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


    @GET
    @Path("skuOverview")
    public String overviewBySku(@QueryParam("pn") Long pn,
                                @QueryParam("rn") Long rn) {
        //TODO
        // 获取仓库根Id locatonService.getWarehouseLocationId
        // 获取根节点下所有能够储存货物的库位 locationService.getStoreLocationIds
        // 查询商品维度的总库存数据
        // 查询盘库盘盈区的id下库存总数，从上面的库存总数中减去
        // 查询残损区库存
        // 查询退货区库存

        return JsonUtils.SUCCESS();
    }

    @GET
    @Path("skuOverview")
    public String overviewByLocation(@QueryParam("pn") Long pn,
                                    @QueryParam("rn") Long rn) {
        //TODO
        // 获取仓库根Id locationService.getWarehouseLocationId
        // 获取根节点下所有能够储存货物的库位 locationService.getStoreLocation
        // 根据pn, rn 找到相应的locationIdList
        // 根据location查寻对应的quant

        return JsonUtils.SUCCESS();
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

}
