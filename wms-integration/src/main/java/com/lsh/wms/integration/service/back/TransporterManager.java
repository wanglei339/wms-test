package com.lsh.wms.integration.service.back;

import com.lsh.wms.core.constant.SysLogConstant;
import com.lsh.wms.model.system.SysLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by mali on 16/11/17.
 */
@Component
public class TransporterManager {

    @Autowired
    public void dealOne(SysLog sysLog) {
        ITransporter transporter = new ITransporter() {
            public void process(SysLog sysLog) {

            }
        };

        if (sysLog.getLogType().equals(SysLogConstant.LOG_TYPE_OBD)) {
            transporter = new IbdTransporter();
        }

        transporter.process(sysLog);
    }
}