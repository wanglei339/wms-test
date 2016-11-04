package com.lsh.wms.api.service.tu;

import com.lsh.base.common.exception.BizCheckedException;

/**
 * Created by zhanghongling on 16/11/4.
 */
public interface ITuOrdersRestService {
    public String getTuOrdersList(String tuId) throws BizCheckedException;
}
