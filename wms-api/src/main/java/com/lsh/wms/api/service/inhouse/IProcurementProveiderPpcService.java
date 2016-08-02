package com.lsh.wms.api.service.inhouse;

import com.lsh.base.common.exception.BizCheckedException;

import java.util.Map;

/**
 * Created by mali on 16/8/1.
 */
public interface IProcurementProveiderPpcService {
    void createProcurement() throws BizCheckedException;
    void scanFromLocation(Map<String, Object> params) throws BizCheckedException;
    void scanToLocation(Map<String, Object> params) throws  BizCheckedException;
}
