package com.lsh.wms.core.service.persistence;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.core.constant.SysLogConstant;
import com.lsh.wms.model.system.SysLog;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by mali on 16/11/17.
 */
@Component
@Transactional(readOnly = false)
public class PersistenceManager {
    private static final Logger logger = LoggerFactory.getLogger(PersistenceManager.class);

    public SysLog getSysLog(int type, Object data,Integer targetSystem) throws BizCheckedException {
        SysLog sysLog = new SysLog();
        try {
            sysLog.setLogType(type);
            sysLog.setTargetSystem(targetSystem);
            switch (type) {
//                case SysLogConstant.LOG_TYPE_LOSS:
//                    getStockChangeLog(data, sysLog);
//                    break;
//                case SysLogConstant.LOG_TYPE_WIN:
//                    getStockChangeLog(data, sysLog);
//                    break;
                default:
                    getDefaultLog(data, sysLog);
            }
        } catch (Exception e) {
            throw new BizCheckedException("3900001");
        }
        return sysLog;
    }

    public void getDefaultLog(Object data, SysLog sysLog) {
        Long businessId = (Long) data;
        sysLog.setBusinessId(businessId);
    }
    public void getStockChangeLog(Object data, SysLog sysLog) {

    }
}
