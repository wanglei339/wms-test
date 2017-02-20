package com.lsh.wms.core.dao.stock;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.stock.StockQuantInfo;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface StockQuantInfoDao {

	void insert(StockQuantInfo stockQuantInfo);
	
	void updateByItemId(StockQuantInfo stockQuantInfo);
	
	StockQuantInfo getStockQuantInfoById(Long id);

    Integer countStockQuantInfo(Map<String, Object> params);

    List<StockQuantInfo> getStockQuantInfoList(Map<String, Object> params);
	
}