package com.lsh.wms.task.handler;

import com.alibaba.fastjson.JSON;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.dao.taking.StockTakingDetailDao;
import com.lsh.wms.core.dao.task.StockTakingTaskDao;
import com.lsh.wms.core.service.taking.StockTakingService;
import com.lsh.wms.core.service.task.StockTakingTaskService;
import com.lsh.wms.model.taking.StockTakingDetail;
import com.lsh.wms.model.task.Operation;
import com.lsh.wms.model.task.StockTakingTask;
import com.lsh.wms.model.task.Task;
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
    private StockTakingTaskService stockTakingTaskService;

    @Autowired
    private StockTakingDetailDao detailDao;

    @Autowired
    private TaskHandlerFactory handlerFactory;

    @PostConstruct
    public void postConstruct() {
        handlerFactory.register(TaskConstant.TYPE_STOCK_TAKING, this);
    }


    public void create(Task task, List<Operation > operationList) {
        stockTakingTaskService.create(task, operationList);
    }

    public void assigned(Long taskId, Long staffId) {
        stockTakingTaskService.assigned(taskId, staffId);
    }

    public void allocate(Long taskId) {
        stockTakingTaskService.allocate(taskId);
    }

    public void done(Long taskId){
        stockTakingTaskService.done(taskId);
    }

    public void cancel(Long taskId) {
        stockTakingTaskService.cancel(taskId);
    }
}
