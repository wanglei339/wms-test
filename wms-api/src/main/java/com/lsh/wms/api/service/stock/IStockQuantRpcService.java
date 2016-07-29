package com.lsh.wms.api.service.stock;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.model.stock.StockQuant;
import com.lsh.wms.model.stock.StockQuantCondition;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by mali on 16/7/28.
 */
public interface IStockQuantRpcService {
    BigDecimal getQty(StockQuantCondition condition) throws BizCheckedException;
    List<StockQuant> getQuantList(StockQuantCondition condition) throws BizCheckedException;
    List<StockQuant> reserve(StockQuantCondition condition, Long taskId, BigDecimal requiredQty) throws BizCheckedException;
    void move(Long moveId) throws BizCheckedException;
}
