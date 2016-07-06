package com.lsh.wms.service.sms;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.wms.api.service.sms.ISmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service(protocol = "dubbo")
public class SmsService implements ISmsService {

    private static Logger logger = LoggerFactory.getLogger(SmsService.class);

    public String sendMsg(String phone, String msg) {
        logger.info("sendMsg phone={},msg={}", phone, msg);
        return "sendMsg success!";
    }

}
