package com.lsh.wms.core.service.task;

import com.lsh.base.common.utils.DateUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.dao.task.StockTakingTaskDao;
import com.lsh.wms.model.task.Operation;
import com.lsh.wms.model.task.StockTakingTask;
import com.lsh.wms.model.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by mali on 16/7/22.
 */
public class StockTakingTaskService extends BaseTaskService {
    @Autowired
    private StockTakingTaskDao stockTakingTaskDao;

    @Transactional(readOnly =  false)
    private void update(StockTakingTask task) {
        task.setUpdatedAt(DateUtils.getCurrentSeconds());
        stockTakingTaskDao.update(task);
    }

    @Transactional(readOnly = false)
    public void create(Task task, List<Operation > operationList) {
        task.setTaskId(RandomUtils.genId());
        task.setStatus(TaskConstant.Draft);
        task.setCreatedAt(DateUtils.getCurrentSeconds());
        task.setUpdatedAt(DateUtils.getCurrentSeconds());
        stockTakingTaskDao.insert((StockTakingTask) task);
        task.setPlanId(((StockTakingTask) task).getTakingId());
        task.setType(TaskConstant.TYPE_STOCK_TAKING);
        super.create(task, operationList);
    }

    @Transactional(readOnly = false)
    public void assigned(Long taskId, Long staffId) {
        StockTakingTask task = stockTakingTaskDao.getStockTakingTaskById(taskId);
        task.setOperator(staffId);
        task.setStatus(TaskConstant.Assigned);
        this.update(task);
        super.assigned(taskId, staffId);
    }

    @Transactional(readOnly = false)
    public void allocate(Long taskId) {
        StockTakingTask task = stockTakingTaskDao.getStockTakingTaskById(taskId);
        task.setStatus(TaskConstant.Allocated);
        this.update(task);
        super.allocate(taskId);
    }

    @Transactional(readOnly = false)
    public void done(Long taskId){
        StockTakingTask task = stockTakingTaskDao.getStockTakingTaskById(taskId);
        task.setStatus(TaskConstant.Done);
        this.update(task);
        super.done(taskId);
    }

    @Transactional(readOnly = false)
    public void cancel(Long taskId) {
        StockTakingTask task = stockTakingTaskDao.getStockTakingTaskById(taskId);
        task.setStatus(TaskConstant.Cancel);
        this.update(task);
        super.cancel(taskId);
    }
}
