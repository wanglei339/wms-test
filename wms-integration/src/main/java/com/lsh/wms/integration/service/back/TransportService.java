package com.lsh.wms.integration.service.back;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.utils.StrUtils;
import com.lsh.wms.api.service.back.ITransportService;
import com.lsh.wms.core.service.system.SysLogService;
import com.lsh.wms.model.system.SysLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by mali on 16/11/17.
 */
@Service(protocol = "dubbo")
public class TransportService implements ITransportService{
    private static final Logger logger = LoggerFactory.getLogger(TransportService.class);

    @Autowired
    private TransporterManager transporterManager;

    @Autowired
    private SysLogService sysLogService;

    public void dealOne(Long sysLogId) {

        logger.info(StrUtils.formatString("begin deal with sysLogId[{0}]]", sysLogId));

        SysLog sysLog = sysLogService.getSysLogById(sysLogId);

        if(sysLog == null) {
            logger.warn(StrUtils.formatString("cannot find syslog for log_id[{0}]", sysLogId));
            return;
        }

        logger.info(StrUtils.formatString("begin send data back [logId:{0},businessId:{1}]", sysLogId, sysLog.getBusinessId()));
        transporterManager.dealOne(sysLog);


        logger.info(StrUtils.formatString("begin update sysLogInfo[logId:{0},businessId:{1}] ", sysLogId, sysLog.getBusinessId()));
        sysLogService.updateSysLog(sysLog);
    }
}
