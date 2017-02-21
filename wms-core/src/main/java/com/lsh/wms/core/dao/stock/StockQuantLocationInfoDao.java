package com.lsh.wms.core.dao.stock;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.stock.StockQuantLocationInfo;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface StockQuantLocationInfoDao {

	void insert(StockQuantLocationInfo stockQuantLocationInfo);
	
	void update(StockQuantLocationInfo stockQuantLocationInfo);

	void updateByLocationAndItemAndSupplier(StockQuantLocationInfo stockQuantLocationInfo);

	void del(StockQuantLocationInfo stockQuantLocationInfo);
	
	StockQuantLocationInfo getStockQuantLocationInfoById(Long id);

    Integer countStockQuantLocationInfo(Map<String, Object> params);

    List<StockQuantLocationInfo> getStockQuantLocationInfoList(Map<String, Object> params);
	
}