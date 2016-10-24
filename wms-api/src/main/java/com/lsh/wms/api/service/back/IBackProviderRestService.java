package com.lsh.wms.api.service.back;

import com.lsh.base.common.exception.BizCheckedException;

/**
 * Created by wuhao on 16/10/21.
 */

public interface IBackProviderRestService {
    String getOrderInfo(Long orderId) throws BizCheckedException;
    String updateSoStatus(Long orderId,int orderStatus) throws BizCheckedException;
    String test() throws BizCheckedException;
}
