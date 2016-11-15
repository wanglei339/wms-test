package com.lsh.wms.api.service.csi;

import com.lsh.base.common.exception.BizCheckedException;

import java.util.Map;

/**
 * @Author 马启迪 maqidi@lsh123.com
 * @Date 2016/11/15 上午12:48
 */
public interface ICsiCustomerRestService {
    public String getCustomerList(Map<String, Object> mapQuery) throws BizCheckedException;

    public String getCustomerCount(Map<String, Object> mapQuery)throws BizCheckedException;

}
