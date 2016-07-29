package com.lsh.wms.rpc.service.stock;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.api.service.stock.IStockLotRestService;
import com.lsh.wms.core.service.stock.StockLotService;
import com.lsh.wms.model.stock.StockLot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

public class StockLotRestService implements IStockLotRestService{
    private static Logger logger = LoggerFactory.getLogger(StockLotRestService.class);

    @Autowired
    private StockLotService stockLotService;

    @Autowired
    private StockLotRpcService stockLotRpcService;

    @GET
    @Path("getStockLotByLotId")
    public String getStockLotByLotId(@QueryParam("lotId") long iLotId) {
        StockLot stockLot = stockLotRpcService.getStockLotByLotId(iLotId);
        return JsonUtils.SUCCESS(stockLot);
    }

    @POST
    @Path("insertLot")
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
    public String insertLot(StockLot lot) {
        boolean isTrue =stockLotRpcService.insertLot(lot);
        if(isTrue) {
            return JsonUtils.SUCCESS();
        }else {
            return JsonUtils.EXCEPTION_ERROR("insertFail");
        }
    }

    @POST
    @Path("updateLot")
    public String updateLot(StockLot lot) {
        boolean isTrue =stockLotRpcService.updateLot(lot);
        if(isTrue) {
            return JsonUtils.SUCCESS();
        }else {
            return JsonUtils.EXCEPTION_ERROR("updateFail");
        }
    }

    @POST
    @Path("searchLot")
    public String searchLot(Map<String, Object> mapQuery) {

        List<StockLot> StockLotlist = stockLotRpcService.searchLot(mapQuery);
        return JsonUtils.SUCCESS(StockLotlist);
    }


}