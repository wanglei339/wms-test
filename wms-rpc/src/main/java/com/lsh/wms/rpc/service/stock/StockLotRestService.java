package com.lsh.wms.rpc.service.stock;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.json.JsonUtils;
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

    @GET
    @Path("getStockLotByLotId")
    public String getStockLotByLotId(@QueryParam("lotId") long iLotId) {
        logger.info("lotId = " + iLotId);
        StockLot StockLot = stockLotService.getStockLotByLotId(iLotId);
        return JsonUtils.SUCCESS(StockLot);
    }

    @POST
    @Path("insertLot")
    public String insertLot(StockLot lot) {
        int result = stockLotService.insertLot(lot);
        if(result == 0) return "Success!";
        return "Failure!";
    }

    @POST
    @Path("updateLot")
    public String updateLot(StockLot lot) {
        int result = stockLotService.updateLot(lot);
        if(result == 0) return "Success!";
        return "Failure!";
    }

    @POST
    @Path("searchLot")
    public String searchLot(Map<String, Object> mapQuery) {
        List<StockLot> StockLotlist = stockLotService.searchLot(mapQuery);
        return JsonUtils.SUCCESS(StockLotlist);
    }

}