package com.lsh.wms.core.dao.stock;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.api.model.stock.StockQuant;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface StockQuantDao {

	void insert(StockQuant stockQuant);
	
	void update(StockQuant stockQuant);
	
	StockQuant getStockQuantById(Integer id);

    Integer countStockQuant(Map<String, Object> params);

    List<StockQuant> getStockQuantList(Map<String, Object> params);

    List<StockQuant> getQuants(Map<String, Object> params);
	
}