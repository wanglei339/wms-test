package com.lsh.wms.api.service.stock;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.model.stock.StockMove;
import com.lsh.wms.model.stock.StockQuant;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by mali on 16/7/27.
 */
public interface IStockMoveRpcService {
    void create(StockMove move);
    void create(List<StockMove> moveList);
    void done(Long moveId);
}
