package com.lsh.wms.core.service.task;

import com.lsh.base.common.utils.DateUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.core.dao.task.StockTakingTaskDao;
import com.lsh.wms.model.task.StockTakingTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


/**
 * Created by mali on 16/7/22.
 */
public class StockTakingTaskService {
    @Autowired
    private StockTakingTaskDao stockTakingTaskDao;

    @Transactional(readOnly = false)
    public void create(StockTakingTask task) {
        task.setTaskId(RandomUtils.genId());
        task.setCreatedAt(DateUtils.getCurrentSeconds());
        task.setUpdatedAt(DateUtils.getCurrentSeconds());
        stockTakingTaskDao.insert(task);
    }


}
