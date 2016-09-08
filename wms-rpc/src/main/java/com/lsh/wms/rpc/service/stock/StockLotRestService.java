package com.lsh.wms.rpc.service.stock;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.service.stock.IStockLotRestService;
import com.lsh.wms.model.stock.StockLot;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

/**
 * Created by Ming on 7/14/16.
 */

@Service(protocol = "rest")
@Path("stock_lot")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})

public class StockLotRestService implements IStockLotRestService {

    @Autowired
    private StockLotRpcService stockLotRpcService;

    @GET
    @Path("getStockLotById")
    public String getLotById(@QueryParam("lotId") long lotId) {
        StockLot stockLot = stockLotRpcService.getLotByLotId(lotId);
        return JsonUtils.SUCCESS(stockLot);
    }


    /***
     * skuId         商品id
     * serialNo      生产批次号
     * inDate        入库时间
     * productDate   生产时间
     * expireDate    保质期失效时间
     * itemId
     * poId          采购订单
     * receiptId     收货单
     * packUnit      包装单位
     * packName      包装名称
     *
     */
    @POST
    @Path("insertLot")
    public String insertLot(StockLot lot) {
        boolean isTrue = stockLotRpcService.insert(lot);
        if (isTrue) {
            return JsonUtils.SUCCESS();
        } else {
            return JsonUtils.EXCEPTION_ERROR("insertFail");
        }
    }

    @POST
    @Path("updateLot")
    public String updateLot(StockLot lot) {
        boolean isTrue = stockLotRpcService.update(lot);
        if (isTrue) {
            return JsonUtils.SUCCESS();
        } else {
            return JsonUtils.EXCEPTION_ERROR("updateFail");
        }
    }

    @POST
    @Path("searchLot")
    public String searchLot(Map<String, Object> mapQuery) {

        List<StockLot> StockLotlist = stockLotRpcService.search(mapQuery);
        return JsonUtils.SUCCESS(StockLotlist);
    }


}