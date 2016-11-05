package com.lsh.wms.api.service.tu;

import com.lsh.base.common.exception.BizCheckedException;

import java.util.Map;

/**
 * Created by zhanghongling on 16/11/4.
 */
public interface ITuOrdersRpcService {
    public Map<String,Object> getTuOrdersList(String tuId) throws BizCheckedException;
    public Map<String,Object> getDeliveryOrdersList(String tuId) throws BizCheckedException;


}
