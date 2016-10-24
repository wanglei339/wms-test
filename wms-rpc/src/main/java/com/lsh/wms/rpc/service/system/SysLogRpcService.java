package com.lsh.wms.rpc.service.system;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.wms.api.service.system.ISysLogRpcService;
import com.lsh.wms.core.service.system.SysLogService;
import com.lsh.wms.model.system.SysLog;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * Created by lixin-mac on 2016/10/24.
 */
@Service(protocol = "dubbo")
public class SysLogRpcService implements ISysLogRpcService{

    @Autowired
    private SysLogService sysLogService;

    public List<SysLog> getSysLogList(Map<String, Object> params) {
        return sysLogService.getSysLogList(params);
    }

    public Integer countSysLog(Map<String, Object> params) {
        return sysLogService.countSysLog(params);
    }
}
