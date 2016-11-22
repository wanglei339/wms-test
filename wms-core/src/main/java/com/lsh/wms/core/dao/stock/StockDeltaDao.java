package com.lsh.wms.core.dao.stock;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.stock.StockDelta;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface StockDeltaDao {

	void insert(StockDelta stockDelta);
	
	void update(StockDelta stockDelta);
	
	StockDelta getStockDeltaById(Long id);

    Integer countStockDelta(Map<String, Object> params);

    List<StockDelta> getStockDeltaList(Map<String, Object> params);
	
}