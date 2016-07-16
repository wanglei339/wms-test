package com.lsh.wms.api.service.stock;

import com.lsh.wms.model.stock.StockLot;

import java.util.Map;

/**
 * Created by Ming on 7/14/16.
 */
public interface IStockLotRestService {
    public String getStockLotByLotId(long iLotId);
    public String insertLot(StockLot lot);
    public String updateLot(StockLot lot);
    public String searchLot(Map<String, Object> mapQuery);
}
