package com.lsh.wms.task.service.task.pick;

import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.service.pick.PickTaskService;
import com.lsh.wms.model.pick.PickTaskDetail;
import com.lsh.wms.model.pick.PickTaskHead;
import com.lsh.wms.model.task.StockTakingTask;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.task.service.handler.AbsTaskHandler;
import com.lsh.wms.task.service.handler.TaskHandlerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by zengwenjun on 16/7/23.
 */

public class PickTaskHandler extends AbsTaskHandler {
    @Autowired
    private TaskHandlerFactory handlerFactory;
    @Autowired
    private PickTaskService taskService;

    @PostConstruct
    public void postConstruct() {
        handlerFactory.register(TaskConstant.TYPE_PICK, this);
    }

    protected void createConcrete(TaskEntry taskEntry) {
        taskService.createPickTask((PickTaskHead)taskEntry.getTaskHead(),
                (List<PickTaskDetail>)(List<?>)taskEntry.getTaskDetailList());
    }
}
