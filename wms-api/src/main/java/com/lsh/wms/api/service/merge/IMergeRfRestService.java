package com.lsh.wms.api.service.merge;

import com.lsh.base.common.exception.BizCheckedException;

/**
 * Created by fengkun on 2016/10/11.
 */
public interface IMergeRfRestService {
    String mergeContainers() throws BizCheckedException;
    String checkMergeContainers() throws BizCheckedException;
    String checkMergeStatus() throws BizCheckedException;
}
