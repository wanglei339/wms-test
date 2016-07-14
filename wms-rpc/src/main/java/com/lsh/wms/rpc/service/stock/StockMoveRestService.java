package com.lsh.wms.rpc.service.stock;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.google.common.collect.Lists;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.BeanMapTransUtils;
import com.lsh.wms.api.service.stock.IStockMoveRestService;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.service.stock.StockMoveService;
import com.lsh.wms.model.stock.StockMove;
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
 * Created by mali on 16/7/11.
 */

@Service(protocol = "rest")
@Path("stock_move")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class StockMoveRestService implements IStockMoveRestService {
    private static Logger logger = LoggerFactory.getLogger(StockMoveRestService.class);

    @Autowired
    private StockMoveService stockMoveService;

    @GET
    @Path("getInboundQty")
    public String getInboundQty(@QueryParam("locationId") Long locationId,
                                @QueryParam("skuId") Long skuId) {
        HashMap<String, Object> mapQuery = new HashMap<String, Object>();

        mapQuery.put("skuId", skuId);
        List<Long> locationList = Lists.newArrayList(locationId);
        //TODO getLocationList
        mapQuery.put("toLocationList", locationList);

        List<Long> statusList = Lists.newArrayList(TaskConstant.Assigned, TaskConstant.Allocated);
        mapQuery.put("statusList", statusList);
        List<StockMove> moveList = stockMoveService.getMoveList(mapQuery);

        BigDecimal total = BigDecimal.ZERO;
        for (StockMove move : moveList) {
            total = total.add(move.getQty());
        }
        return JsonUtils.SUCCESS(total);
    }

    @GET
    @Path("getOutboundQty")
    public String getOutboundQty(@QueryParam("locationId") Long locationId,
                                 @QueryParam("skuId") Long skuId) {
        HashMap<String, Object> mapQuery = new HashMap<String, Object>();

        mapQuery.put("skuId", skuId);
        List<Long> locationList = Lists.newArrayList(locationId);
        //TODO getLocationList
        mapQuery.put("fromLocationList", locationList);
        List<Long> statusList = Lists.newArrayList(TaskConstant.Assigned, TaskConstant.Allocated);
        mapQuery.put("statusList", statusList);
        List<StockMove> moveList = stockMoveService.getMoveList(mapQuery);
        BigDecimal total = BigDecimal.ZERO;
        for (StockMove move : moveList) {
            total = total.add(move.getQty());
        }
        return JsonUtils.SUCCESS(total);
    }

    @GET
    @Path("assign")
    public String assign(@QueryParam("moveId") Long moveId) {
        stockMoveService.assign(moveId);
        return JsonUtils.SUCCESS();
    }

    @GET
    @Path("allocate")
    public String allocate(@QueryParam("moveId") Long moveId,
                           @QueryParam("operator") Long operator) {
        stockMoveService.allocate(moveId, operator);
        return JsonUtils.SUCCESS();
    }

    @GET
    @Path("done")
    public String done(@QueryParam("moveId") Long moveId,
                       @QueryParam("qtyDone") BigDecimal qtyDone) {
        stockMoveService.done(moveId, qtyDone);
        return JsonUtils.SUCCESS();
    }

    @GET
    @Path("cancel")
    public String cancel(@QueryParam("moveId") Long moveId) {
        stockMoveService.cancel(moveId);
        return JsonUtils.SUCCESS();
    }

    @POST
    @Path("create")
    public String create(Map<String, Object> mapInput) {
        StockMove move = BeanMapTransUtils.map2Bean(mapInput, StockMove.class);
        try {
            stockMoveService.create(move);
        } catch (Exception ex) {
            logger.error(ex.getCause().getMessage());
            return JsonUtils.EXCEPTION_ERROR("create failed");
        }
        return JsonUtils.SUCCESS();
    }

    @GET
    @Path("getHistory")
    public String getHistory(@QueryParam("move_id") Long move_id) {
        List<StockQuantMoveRel> moveRels=stockMoveService.getHistoryById(move_id);
        return JsonUtils.SUCCESS(moveRels);
    }

}
