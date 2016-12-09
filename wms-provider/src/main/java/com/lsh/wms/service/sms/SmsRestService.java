package com.lsh.wms.service.sms;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.model.so.ObdDetail;
import com.lsh.wms.api.service.sms.ISmsRestService;
import com.lsh.wms.api.service.stock.IStockQuantRpcService;
import com.lsh.wms.core.constant.StockConstant;
import com.lsh.wms.core.dao.redis.RedisSortedSetDao;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.so.SoOrderService;
import com.lsh.wms.core.service.stock.*;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.core.service.task.MessageService;
import com.lsh.wms.model.so.ObdHeader;
import com.lsh.wms.model.stock.StockMove;
import com.lsh.wms.model.stock.StockQuant;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(protocol = "rest")
@Path("sms")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class SmsRestService implements ISmsRestService {

    @Autowired
    private SmsService smsService;

    @Autowired
    private StockMoveService moveService;

    @Autowired
    private StockSummaryService stockSummaryService;

    @Autowired
    private SoOrderService soOrderService;

    public void setSmsService(SmsService smsService) {
        this.smsService = smsService;
    }


    @GET
    @Path("sendMsg")
    public String sendMsg (@QueryParam("item_id") String itemId,
                          @QueryParam("location_code") String locationCode) throws BizCheckedException  {
        return JsonUtils.SUCCESS();
    }

    @GET
    @Path("inventory")
    public String inventory(@QueryParam("item_id") Long itemId,
                            @QueryParam("from_location_id") Long fromLocationId,
                            @QueryParam("to_location_id") Long toLocationId,
                            @QueryParam("qty")BigDecimal qty) throws BizCheckedException {
        StockMove move = new StockMove();
        move.setItemId(itemId);
        move.setFromLocationId(fromLocationId);
        move.setToLocationId(toLocationId);
        move.setQty(qty);
        moveService.move(move);
        return JsonUtils.SUCCESS();
    }

    @GET
    @Path("so")
    public String alloc(@QueryParam("order_id") String orderId) throws BizCheckedException {
        return JsonUtils.SUCCESS();
    }

    @GET
    @Path("diff")
    public String diff(@QueryParam("order_id") String orderId) throws BizCheckedException {
        return JsonUtils.SUCCESS();
    }
}
