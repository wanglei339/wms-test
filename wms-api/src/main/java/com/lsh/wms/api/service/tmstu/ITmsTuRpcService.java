package com.lsh.wms.api.service.tmstu;

import com.lsh.base.common.exception.BizCheckedException;

import java.util.List;
import java.util.Map;

/**
 * Created by fengkun on 2016/11/16.
 */
public interface ITmsTuRpcService {
    Boolean postTuDetails(String tuId) throws BizCheckedException;
    public List<Map<String, Object>> getTravelOrderList(String tuId) throws BizCheckedException;


}
