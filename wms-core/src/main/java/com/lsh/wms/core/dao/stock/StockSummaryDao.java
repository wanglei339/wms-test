package com.lsh.wms.core.dao.stock;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.stock.StockSummary;

import java.util.List;
import java.util.Map;

@MyBatisRepository

public interface StockSummaryDao {

    void changeStock(StockSummary stockSummary);

    StockSummary getStockSummaryByItemId(Long itemId);

    Integer countStockSummary(Map<String, Object> params);

    List<StockSummary> getStockSummaryList(Map<String, Object> params);
}
