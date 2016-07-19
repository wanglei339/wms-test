package com.lsh.wms.core.dao.taking;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.taking.StockTakingDetail;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface StockTakingDetailDao {

	void insert(StockTakingDetail stockTakingDetail);

	void batchInsert(List<StockTakingDetail> list);
	
	void update(StockTakingDetail stockTakingDetail);
	
	StockTakingDetail getStockTakingDetailById(Integer id);

    Integer countStockTakingDetail(Map<String, Object> params);

    List<StockTakingDetail> getStockTakingDetailList(Map<String, Object> params);
	
}