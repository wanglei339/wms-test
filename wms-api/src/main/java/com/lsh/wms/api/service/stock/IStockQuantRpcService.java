package com.lsh.wms.api.service.stock;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.model.stock.*;
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
    Integer countQuantList(StockQuantCondition condition) throws BizCheckedException;

    String freeze(Map<String, Object> mapCondition) throws BizCheckedException;
    String unfreeze(Map<String, Object> mapCondition) throws BizCheckedException;
    String toDefect(Map<String, Object> mapCondition) throws BizCheckedException;
    String toRefund(Map<String, Object> mapCondition) throws BizCheckedException;
    void writeOffQuant(Long quantId, BigDecimal realQty,Long operator)throws BizCheckedException;

    int getItemStockCount(Map<String, Object> mapQuery);
    Map<Long, Map<String, BigDecimal>>getItemStockList(Map<String, Object> mapQuery);
    int getLocationStockCount(Map<String, Object> mapQuery);
    List<StockQuant> getLocationStockList(Map<String, Object> mapQuery);
    List<StockMove> traceQuant(Long quantId);
    void updateStockInfoList();
    void updateStockLocationInfoList();
    List<StockQuantInfo> getStockInfo(Map<String, Object> mapQuery);
    Integer countStockInfo(Map<String, Object> mapQuery);
    List<StockQuantLocationInfo> getStockLocationInfo(Map<String, Object> mapQuery);
    Integer countStockLocationInfo(Map<String, Object> mapQuery);
    List<StockQuant> getItemLocationList(Map<String, Object> mapQuery);
    List<StockQuant> getBackItemLocationList(Map<String, Object> mapQuery);
}
