package com.lsh.wms.core.dao.transfer;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.transfer.StockTransferTaskDetail;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface StockTransferTaskDetailDao {

	void insert(StockTransferTaskDetail stockTransferTaskDetail);

	void batchInsert(List<StockTransferTaskDetail> list);

	void update(StockTransferTaskDetail stockTransferTaskDetail);
	
	StockTransferTaskDetail getStockTransferTaskDetailById(Long id);

    Integer countStockTransferTaskDetail(Map<String, Object> params);

    List<StockTransferTaskDetail> getStockTransferTaskDetailList(Map<String, Object> params);
	
}