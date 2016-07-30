package com.lsh.wms.api.service.stock;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.model.stock.StockQuant;
import com.lsh.wms.model.stock.StockQuantCondition;
import com.lsh.wms.model.task.TaskInfo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by mali on 16/7/28.
 */
public interface IStockQuantRpcService {
    BigDecimal getQty(StockQuantCondition condition) throws BizCheckedException;
    List<StockQuant> getQuantList(StockQuantCondition condition) throws BizCheckedException;


    List<StockQuant> reserveByTask(TaskInfo taskInfo) throws BizCheckedException;
    List<StockQuant> reserveByContainer(Long containerId, Long taskId) throws BizCheckedException;

    String freeze(Map<String, Object> mapCondition) throws BizCheckedException;
    String unfreeze(Map<String, Object> mapCondition) throws BizCheckedException;
    String toDefect(Map<String, Object> mapCondition) throws BizCheckedException;
    String toRefund(Map<String, Object> mapCondition) throws BizCheckedException;

    int getItemStockCount(Map<String, Object> mapQuery);
    Map<Long, Map<String, BigDecimal>>getItemStockList(Map<String, Object> mapQuery);
    int getLocationStockCount(Map<String, Object> mapQuery);
    List<StockQuant> getLocationStockList(Map<String, Object> mapQuery);

}
