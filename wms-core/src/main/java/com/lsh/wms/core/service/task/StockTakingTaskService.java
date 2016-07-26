package com.lsh.wms.core.service.task;

import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.core.dao.task.StockTakingTaskDao;
import com.lsh.wms.model.task.StockTakingTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mali on 16/7/22.
 */

@Component
@Transactional(readOnly = true)
public class StockTakingTaskService {
    @Autowired
    private StockTakingTaskDao stockTakingTaskDao;

    @Transactional(readOnly = false)
    public void create(StockTakingTask task) {
        task.setCreatedAt(DateUtils.getCurrentSeconds());
        task.setUpdatedAt(DateUtils.getCurrentSeconds());
        stockTakingTaskDao.insert(task);
    }

    public List<StockTakingTask> getTakingTask(Map queryMap) {
        return stockTakingTaskDao.getStockTakingTaskList(queryMap);
    }

    public Integer count(Map queryMap) {
        return stockTakingTaskDao.countStockTakingTask(queryMap);
    }
    public StockTakingTask getTakingTaskByTaskId(Long taskId) {
        return stockTakingTaskDao.getStockTakingTaskById(taskId);
    }

}
