package com.lsh.wms.core.dao.taking;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.taking.StockTakingHead;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface StockTakingHeadDao {

	void insert(StockTakingHead stockTakingHead);
	
	void update(StockTakingHead stockTakingHead);
	
	StockTakingHead getStockTakingHeadById(Long id);

    Integer countStockTakingHead(Map<String, Object> params);

    List<StockTakingHead> getStockTakingHeadList(Map<String, Object> params);
	
}