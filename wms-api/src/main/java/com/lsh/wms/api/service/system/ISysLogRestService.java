package com.lsh.wms.api.service.system;

import com.lsh.base.common.exception.BizCheckedException;

import java.util.Map;

/**
 * Created by lixin-mac on 2016/10/24.
 */
public interface ISysLogRestService {


    String getSysLogList(Map<String, Object> params);

    String countSysLog(Map<String, Object> params);

    String retransmission(Long logId) throws BizCheckedException;
}
