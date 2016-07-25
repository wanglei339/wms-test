package com.lsh.wms.task.service.task.shelve;

import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.model.shelve.ShelveTaskHead;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.task.service.handler.AbsTaskHandler;
import com.lsh.wms.task.service.handler.TaskHandlerFactory;
import com.lsh.wms.core.service.shelve.ShelveTaskService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * Created by fengkun on 16/7/25.
 */
public class ShelveTaskHandler extends AbsTaskHandler {
    @Autowired
    private TaskHandlerFactory handlerFactory;
    @Autowired
    private ShelveTaskService taskService;

    @PostConstruct
    public void postConstruct() {
        handlerFactory.register(TaskConstant.TYPE_SHELVE, this);
    }

    protected void createConcrete(TaskEntry taskEntry) {
        //ShelveTaskHead taskHead = (ShelveTaskDe)taskEntry.getTaskDetailList()
    }
}
