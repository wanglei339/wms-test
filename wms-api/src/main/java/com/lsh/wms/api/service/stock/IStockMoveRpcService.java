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
    void move(List<StockMove> moveList) throws BizCheckedException;
    void moveWholeContainer(Long containerId, Long taskId, Long staffId, Long fromLocationId, Long toLocationId) throws BizCheckedException;
    void moveWholeContainer(Long fromContainerId, Long toContainerId, Long taskId, Long staffId, Long fromLocationId, Long toLocationId) throws BizCheckedException;
}
