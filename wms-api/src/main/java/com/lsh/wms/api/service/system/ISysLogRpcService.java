package com.lsh.wms.api.service.system;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.model.system.SysLog;

import java.util.List;
import java.util.Map;

/**
 * Created by lixin-mac on 2016/10/24.
 */
public interface ISysLogRpcService {

    List<SysLog> getSysLogList(Map<String, Object> params);

    Integer countSysLog(Map<String, Object> params);

    void retransmission(Long logId) throws BizCheckedException;

}
