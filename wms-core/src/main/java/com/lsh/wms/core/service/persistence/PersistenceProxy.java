package com.lsh.wms.core.service.persistence;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.utils.StrUtils;
import com.lsh.wms.core.constant.SysLogConstant;
import com.lsh.wms.core.service.system.SysLogService;
import com.lsh.wms.model.system.SysLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by mali on 16/11/17.
 */
@Component
@Transactional(readOnly = true)
public class PersistenceProxy {
    private static final Logger logger = LoggerFactory.getLogger(PersistenceProxy.class);

    @Autowired
    private SysLogService sysLogService;

    @Autowired
    private PersistenceManager persistenceManager;

    @Transactional(readOnly = false)
    public void doOne(int msgType, Object data,Integer targetSystem) throws BizCheckedException {
        SysLog sysLog = persistenceManager.getSysLog(msgType, data,targetSystem);
        sysLogService.insertSysLog(sysLog);
    }
}
