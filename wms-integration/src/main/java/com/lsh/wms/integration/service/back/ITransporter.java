package com.lsh.wms.integration.service.back;

import com.lsh.wms.model.system.SysLog;

/**
 * Created by mali on 16/11/17.
 */
public interface ITransporter {
    public void process(SysLog sysLog);
}
