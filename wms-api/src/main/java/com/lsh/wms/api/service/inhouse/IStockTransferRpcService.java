package com.lsh.wms.api.service.inhouse;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.model.transfer.StockTransferPlan;

import java.util.List;
import java.util.Map;

/**
 * Created by mali on 16/8/1.
 */
public interface IStockTransferRpcService {
    void addPlan(StockTransferPlan plan) throws BizCheckedException;
    Map<String, Object> scanToLocation(Map<String, Object> params) throws BizCheckedException;
    Map<String, Object> scanFromLocation(Map<String, Object> params) throws BizCheckedException;
    Long assign(Long staffId) throws BizCheckedException;
    void updatePlan(StockTransferPlan plan) throws BizCheckedException;
    void cancelPlan(Long taskId) throws BizCheckedException;
    void createStockTransfer() throws BizCheckedException;
}
