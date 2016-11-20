package com.lsh.wms.service.sms;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.wms.api.model.so.ObdDetail;
import com.lsh.wms.api.service.sms.ISmsRestService;
import com.lsh.wms.core.service.so.SoOrderRedisService;
import com.lsh.wms.core.service.so.SoOrderService;
import com.lsh.wms.core.service.stock.SynStockService;
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

    public void setSmsService(SmsService smsService) {
        this.smsService = smsService;
    }

    @GET
    @Path("sendMsg")
    public String sendMsg(@QueryParam("phone") String phone,
                          @QueryParam("msg") String msg) {
        //synStockService.synStock(123L,2.3);
        WaveDetail detail = new WaveDetail();
        detail.setItemId(1L);
        detail.setDeliveryQty(BigDecimal.TEN);
        detail.setOrderId(100L);
        soOrderRedisService.delSoRedis(detail.getOrderId(), detail.getItemId(), detail.getDeliveryQty());
        //return smsService.sendMsg(phone, msg);
        return "OK";
    }

}
