package com.lsh.wms.api.service.merge;

import com.lsh.base.common.exception.BizCheckedException;

/**
 * Created by fengkun on 2016/10/14.
 */
public interface IMergeRestService {
    String getMergeList() throws BizCheckedException;
    String getMergeCount() throws BizCheckedException;
}
