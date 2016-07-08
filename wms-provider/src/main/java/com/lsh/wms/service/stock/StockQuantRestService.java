package com.lsh.wms.service.stock;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.wms.model.stock.StockQuant;
import com.lsh.wms.api.service.stock.IStockQuantRestService;
import com.lsh.wms.core.service.stock.StockQuantService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by mali on 16/6/29.
 */
@Service(protocol = "rest")
@Path("stock_quant")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class StockQuantRestService implements IStockQuantRestService {
    @Autowired
    private StockQuantService stockQuantService;
    
    @GET
    @Path("getOnhand")
    public String getOnhandQty(@QueryParam("skuId") Integer skuId,
                          @QueryParam("locationId") Integer locationId,
                          @QueryParam("containerId") Integer containerId) {
        BigDecimal total =new BigDecimal(0.0);
        List<StockQuant> quantList = stockQuantService.getQuants(skuId, locationId, containerId);
        for ( StockQuant quant : quantList) {
            total = total.add(quant.getQty());
        }
        return total.toString();
    }
}
