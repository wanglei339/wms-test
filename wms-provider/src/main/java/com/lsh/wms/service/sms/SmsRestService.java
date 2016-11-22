package com.lsh.wms.service.sms;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.model.so.ObdDetail;
import com.lsh.wms.api.service.sms.ISmsRestService;
import com.lsh.wms.core.constant.StockConstant;
import com.lsh.wms.core.dao.redis.RedisSortedSetDao;
import com.lsh.wms.core.dao.stock.StockSummaryDao;
import com.lsh.wms.core.service.so.SoOrderRedisService;
import com.lsh.wms.core.service.so.SoOrderService;
import com.lsh.wms.core.service.stock.StockSummaryService;
import com.lsh.wms.core.service.stock.SynStockService;
import com.lsh.wms.model.stock.StockDelta;
import com.lsh.wms.model.stock.StockSummary;
import com.lsh.wms.model.wave.WaveDetail;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;

@Service(protocol = "rest")
@Path("sms")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class SmsRestService implements ISmsRestService {

    @Autowired
    private SmsService smsService;

    @Autowired
    private SynStockService synStockService;

    @Autowired
    private SoOrderRedisService soOrderRedisService;

    @Autowired
    private StockSummaryService stockSummaryService;

    public void setSmsService(SmsService smsService) {
        this.smsService = smsService;
    }


    @Autowired
    private RedisSortedSetDao redisSortedSetDao;

    @GET
    @Path("sendMsg")
    public String sendMsg(@QueryParam("phone") String phone,
                          @QueryParam("msg") String msg) {
//        //synStockService.synStock(123L,2.3);
//        WaveDetail detail = new WaveDetail();
//        detail.setItemId(1L);
//        detail.setDeliveryQty(BigDecimal.TEN);
//        detail.setOrderId(100L);
//        soOrderRedisService.delSoRedis(detail.getOrderId(), detail.getItemId(), detail.getDeliveryQty());
//        //return smsService.sendMsg(phone, msg);
//        return "OK";
        StockDelta delta = new StockDelta();
        delta.setItemId(4716171821968L);
        delta.setInhouseQty(new BigDecimal(10.0));
        delta.setAllocQty(BigDecimal.ONE);
        delta.setBusinessId(20161122L);
        delta.setType(StockConstant.TYPE_SO_DELIVERY);

        stockSummaryService.changeStock(delta);
        return JsonUtils.SUCCESS();
    }

}
