package com.lsh.wms.core.dao.task;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.task.StockTakingTask;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface StockTakingTaskDao {

	void insert(StockTakingTask stockTakingTask);

	void update(StockTakingTask stockTakingTask);
<<<<<<< HEAD

	StockTakingTask getStockTakingTaskById(Long id);
=======
	
	StockTakingTask getStockTakingTaskById(Long taskId);
>>>>>>> 0551e56db4e61b5300856a6a6ff127e6c3796cf0

    Integer countStockTakingTask(Map<String, Object> params);

    List<StockTakingTask> getStockTakingTaskList(Map<String, Object> params);

}