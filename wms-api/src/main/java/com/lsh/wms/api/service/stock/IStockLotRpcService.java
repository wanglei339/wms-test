package com.lsh.wms.api.service.stock;

import com.lsh.wms.model.stock.StockLot;

import java.util.List;
import java.util.Map;

/**
 * Created by wuhao on 16/7/29.
 */
public interface IStockLotRpcService {
    StockLot getLotByLotId(long iLotId);
    void insert(StockLot lot);
    void update(StockLot lot);
    List<StockLot> search(Map<String, Object> mapQuery);
}
