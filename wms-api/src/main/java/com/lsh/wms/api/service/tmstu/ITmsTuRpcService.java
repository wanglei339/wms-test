package com.lsh.wms.api.service.tmstu;

import com.lsh.base.common.exception.BizCheckedException;

/**
 * Created by fengkun on 2016/11/16.
 */
public interface ITmsTuRpcService {
    Boolean postTuDetails(String tuId) throws BizCheckedException;
}
