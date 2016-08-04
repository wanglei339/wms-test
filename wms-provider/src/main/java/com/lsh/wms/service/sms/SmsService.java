package com.lsh.wms.service.sms;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.wms.api.service.sms.ISmsService;
import com.lsh.wms.core.service.demo.DemoService;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.model.task.TaskInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@Service(protocol = "dubbo")
public class SmsService implements ISmsService {

    private static Logger logger = LoggerFactory.getLogger(SmsService.class);

    @Autowired
    private DemoService demoService;

    public String sendMsg(String phone, String msg) {
        logger.info("sendMsg phone={},msg={}", phone, msg);
        TaskInfo taskInfo = new TaskInfo();

        try {
            demoService.create(taskInfo);
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return "sendMsg success!";
    }

}
