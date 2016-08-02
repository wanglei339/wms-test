package com.lsh.wms.api.service.inhouse;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.model.transfer.StockTransferPlan;

import java.util.Map;

/**
 * Created by mali on 16/8/1.
 */
public interface IProcurementProveiderRpcService {
    void addProcurementPlan (StockTransferPlan plan) throws  BizCheckedException;
    void createProcurement() throws BizCheckedException;
    void scanFromLocation(Map<String, Object> params) throws BizCheckedException;
    void scanToLocation(Map<String, Object> params) throws  BizCheckedException;
    Long assign(Long staffId) throws BizCheckedException;
}
