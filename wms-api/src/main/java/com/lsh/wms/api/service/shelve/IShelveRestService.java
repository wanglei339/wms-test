package com.lsh.wms.api.service.shelve;

import com.lsh.base.common.exception.BizCheckedException;

/**
 * Created by fengkun on 16/7/15.
 */
public interface IShelveRestService {
    String scanContainer() throws BizCheckedException;
    String scanTargetLocation(Long taskId, Long locationId) throws BizCheckedException;
}
