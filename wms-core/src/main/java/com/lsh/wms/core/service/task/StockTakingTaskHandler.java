package com.lsh.wms.core.service.task;

import com.alibaba.fastjson.JSON;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.dao.taking.StockTakingDetailDao;
import com.lsh.wms.core.dao.task.StockTakingTaskDao;
import com.lsh.wms.model.taking.StockTakingDetail;
import com.lsh.wms.model.task.Operation;
import com.lsh.wms.model.task.StockTakingTask;
import com.lsh.wms.model.task.TaskInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by mali on 16/7/20.
 */

@Component
@Transactional(readOnly = true)
public class StockTakingTaskHandler extends BaseTaskHandler {
    @Autowired
    private StockTakingTaskDao stockTakingTaskDao;

    @Autowired
    private StockTakingDetailDao detailDao;

    @Autowired
    private TaskHandlerFactory handlerFactory;

    @PostConstruct
    public void postConstruct() {
        handlerFactory.register(TaskConstant.TYPE_STOCK_TAKING, this);
    }

    @Transactional(readOnly =  false)
    private void update(StockTakingTask task) {
        task.setUpdatedAt(DateUtils.getCurrentSeconds());
        stockTakingTaskDao.update(task);
    }

    @Transactional(readOnly = false)
    public void create(TaskInfo task, List<Operation > operationList) {
        task.setTaskId(RandomUtils.genId());
        task.setCreatedAt(DateUtils.getCurrentSeconds());
        task.setUpdatedAt(DateUtils.getCurrentSeconds());
        stockTakingTaskDao.insert((StockTakingTask) task);
        task.setPlanId(((StockTakingTask) task).getTakingId());
        task.setType(TaskConstant.TYPE_STOCK_TAKING);
        super.create(task, operationList);
    }

    @Transactional(readOnly = false)
    public void allocate(Long taskId, Long staffId) {
        StockTakingTask task = stockTakingTaskDao.getStockTakingTaskById(taskId);
        task.setOperator(staffId);
        this.update(task);
        super.allocate(taskId, staffId);
    }

    @Transactional(readOnly = false)
    public void done(Long taskId, String data){
        List<StockTakingDetail> detailList = JSON.parseArray(data, StockTakingDetail.class);
        for (StockTakingDetail detail : detailList) {
            detail.setUpdatedAt(DateUtils.getCurrentSeconds());
            detailDao.update(detail);
        }
        StockTakingTask task = stockTakingTaskDao.getStockTakingTaskById(taskId);
        task.setStatus(TaskConstant.Done);
        this.update(task);
        super.done(taskId, data);
    }

    @Transactional(readOnly = false)
    public void cancel(Long taskId) {
        StockTakingTask task = stockTakingTaskDao.getStockTakingTaskById(taskId);
        task.setStatus(TaskConstant.Cancel);
        this.update(task);
        super.cancel(taskId);
    }
}
