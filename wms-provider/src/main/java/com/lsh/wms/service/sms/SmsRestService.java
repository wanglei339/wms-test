package com.lsh.wms.service.sms;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.api.model.so.ObdDetail;
import com.lsh.wms.api.service.sms.ISmsRestService;
import com.lsh.wms.core.constant.StockConstant;
import com.lsh.wms.core.dao.redis.RedisSortedSetDao;
import com.lsh.wms.core.dao.stock.StockSummaryDao;
import com.lsh.wms.core.service.so.SoOrderRedisService;
import com.lsh.wms.core.service.so.SoOrderService;
import com.lsh.wms.core.service.stock.StockAllocService;
import com.lsh.wms.core.service.stock.StockSummaryService;
import com.lsh.wms.core.service.stock.SynStockService;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.core.service.task.MessageService;
import com.lsh.wms.model.so.ObdHeader;
import com.lsh.wms.model.stock.StockDelta;
import com.lsh.wms.model.stock.StockSummary;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.model.task.TaskMsg;
import com.lsh.wms.model.wave.WaveDetail;
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

    @Autowired
    private SoOrderService soOrderService;

    @Autowired
    private StockAllocService stockAllocService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private BaseTaskService baseTaskService;

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
//  stockSummaryService.changeStock(delta);
        //ObdHeader header = soOrderService.getOutbSoHeaderByOrderOtherId("19682122499");
        //List<com.lsh.wms.model.so.ObdDetail> detailList = soOrderService.getOutbSoDetailListByOrderId(header.getOrderId());
//
//        WaveDetail waveDetail = new WaveDetail();
//        waveDetail.setItemId(152578713218622L);
//        waveDetail.setOrderId(2016111800000022L);
//        waveDetail.setDeliveryId(1001L);
//        waveDetail.setQcQty(BigDecimal.ONE);
//        stockAllocService.realease(waveDetail);
//        //stockAllocService.alloc(header, detailList);

//        TaskMsg message = new TaskMsg();
//        message.setSourceTaskId(1001L);
//        message.setBusinessId(201011L);
//        Map<String, Object> msgBody = new HashMap<String, Object>();
//        msgBody.put("xx", 1);
//        msgBody.put("zz", 2);
//        message.setMsgBody(msgBody);
//        message.setType(1L);
//        message.setErrorMsg("fuck you");
//        message.setCreatedAt(DateUtils.getCurrentSeconds());
//        messageService.saveMessage(message);
//
//        Long businessId = 201011L;
//        message = messageService.getMessage(businessId);

        Map<String,Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("containerId", 201011);
        List<TaskInfo> list = baseTaskService.getTaskInfoList(mapQuery);
        return JsonUtils.SUCCESS(list);
    }

}
