package com.lsh.wms.core.dao.stock;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.stock.StockLot;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface StockLotDao {

	void insert(StockLot stockLot);
	
	void update(StockLot stockLot);

	StockLot getStockLotByLotId(Long lotId);

	StockLot getStockLotById(Long id);

    Integer countStockLot(Map<String, Object> params);

    List<StockLot> getStockLotList(Map<String, Object> params);
	
}