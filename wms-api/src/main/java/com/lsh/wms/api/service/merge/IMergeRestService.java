package com.lsh.wms.api.service.merge;

import com.lsh.base.common.exception.BizCheckedException;

/**
 * Created by fengkun on 2016/10/14.
 */
public interface IMergeRestService {
    String getMergeList() throws BizCheckedException;
    String countMergeList() throws BizCheckedException;
    String getMergeDetail() throws BizCheckedException;
    String getWaveDetailByMergeConatinerId(Long mergeContainerId)throws BizCheckedException;
}
