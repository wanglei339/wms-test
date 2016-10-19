package com.lsh.wms.api.service.merge;

import com.lsh.base.common.exception.BizCheckedException;

import java.util.Map;

/**
 * Created by fengkun on 2016/10/20.
 */
public interface IMergeRpcService {
    String getMergeList(Map<String, Object> mapQuery) throws BizCheckedException;
    String getMergeCount(Map<String, Object> mapQuery) throws BizCheckedException;
}
