package com.lsh.wms.api.service.stock;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.model.stock.StockQuant;
import com.lsh.wms.model.stock.StockQuantCondition;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by mali on 16/6/29.
 */
public interface IStockQuantRestService {

    String getOnhandQty(StockQuantCondition condition) throws BizCheckedException;
    String getList(StockQuantCondition condition) throws BizCheckedException;
    String create(Map<String, Object> mapInput) throws BizCheckedException;
    String freeze(Map<String, Object> mapCondition) throws BizCheckedException;
    String unFreeze(Map<String, Object> mapCondition) throws BizCheckedException;
    String toDefect(Map<String, Object> mapCondition) throws BizCheckedException;
    String toRefund(Map<String, Object> mapCondition) throws BizCheckedException;
    String getHistory(Long quant_id);
    String writeOffQuant(Long quantId, BigDecimal realQty);

    String getItemStockCount(Map<String, Object> mapQuery);
    String getItemStockList(Map<String, Object> mapQuery);

    String getLocationStockCount(Map<String, Object> mapQuery);
    String getLocationStockList(Map<String, Object> mapQuery);
    String traceQuant(Long quantId);
    String getStockQuantList(Map<String, Object> mapQuery);
}
