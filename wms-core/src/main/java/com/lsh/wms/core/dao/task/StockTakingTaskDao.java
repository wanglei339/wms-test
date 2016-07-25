package com.lsh.wms.core.dao.task;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.task.StockTakingTask;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface StockTakingTaskDao {

	void insert(StockTakingTask stockTakingTask);

	void update(StockTakingTask stockTakingTask);

	StockTakingTask getStockTakingTaskById(Long id);

    Integer countStockTakingTask(Map<String, Object> params);

    List<StockTakingTask> getStockTakingTaskList(Map<String, Object> params);

}