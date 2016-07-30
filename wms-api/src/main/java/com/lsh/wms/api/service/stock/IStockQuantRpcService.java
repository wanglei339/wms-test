package com.lsh.wms.api.service.stock;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.model.stock.StockQuant;
import com.lsh.wms.model.stock.StockQuantCondition;
import com.lsh.wms.model.task.TaskInfo;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by mali on 16/7/28.
 */
public interface IStockQuantRpcService {
    BigDecimal getQty(StockQuantCondition condition) throws BizCheckedException;
    List<StockQuant> getQuantList(StockQuantCondition condition) throws BizCheckedException;
    List<StockQuant> reserveByTask(TaskInfo taskInfo) throws BizCheckedException;
    List<StockQuant> reserveByContainer(Long containerId, Long taskId) throws BizCheckedException;
}
