package com.lsh.wms.service.sms;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.wms.api.service.sms.ISmsRestService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Service(protocol = "rest")
@Path("sms")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class SmsRestService implements ISmsRestService {

    @Autowired
    private SmsService smsService;

    public void setSmsService(SmsService smsService) {
        this.smsService = smsService;
    }

    @GET
    @Path("sendMsg")
    public String sendMsg(@QueryParam("phone") String phone,
                          @QueryParam("msg") String msg) {
        return smsService.sendMsg(phone, msg);
    }

}
