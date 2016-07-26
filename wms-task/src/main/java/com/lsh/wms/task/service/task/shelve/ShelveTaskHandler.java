package com.lsh.wms.task.service.task.shelve;

import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.model.shelve.ShelveTaskHead;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.task.service.handler.AbsTaskHandler;
import com.lsh.wms.task.service.handler.TaskHandlerFactory;
import com.lsh.wms.core.service.shelve.ShelveTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by fengkun on 16/7/25.
 */
@Component
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
        ShelveTaskHead taskHead = (ShelveTaskHead) taskEntry.getTaskHead();
        taskHead.setTaskId(taskEntry.getTaskInfo().getTaskId());
        taskService.create(taskHead);
    }

    protected void getConcrete(TaskEntry taskEntry) {
        taskEntry.setTaskHead(taskService.getShelveTaskHead(taskEntry.getTaskInfo().getTaskId()));
    }

    protected void getHeadConcrete(TaskEntry taskEntry) {
        taskEntry.setTaskHead(taskService.getShelveTaskHead(taskEntry.getTaskInfo().getTaskId()));
    }
}
