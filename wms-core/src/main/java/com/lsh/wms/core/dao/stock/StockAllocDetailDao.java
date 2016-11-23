package com.lsh.wms.core.dao.stock;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.stock.StockAllocDetail;

import java.util.List;
import java.util.Map;

/**
 * Created by mali on 16/11/23.
 */
@MyBatisRepository
public interface StockAllocDetailDao {

    void insert(StockAllocDetail stockAllocDetail);

    void update(StockAllocDetail stockAllocDetail);

    List<StockAllocDetail> getStockAllocDetailByItemId(Long itemId);

    List<StockAllocDetail> getStockAllocDetailByObdDetail(Long obdId);

    List<StockAllocDetail> getStockAllocDetailList(Map<String, Object> params);
}
