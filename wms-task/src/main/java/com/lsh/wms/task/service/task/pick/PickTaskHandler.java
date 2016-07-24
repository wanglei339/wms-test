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
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by zengwenjun on 16/7/23.
 */

@Component
public class PickTaskHandler extends AbsTaskHandler {
    @Autowired
    private TaskHandlerFactory handlerFactory;
    @Autowired
    private PickTaskService pickTaskService;

    @PostConstruct
    public void postConstruct() {
        handlerFactory.register(TaskConstant.TYPE_PICK, this);
    }

    protected void createConcrete(TaskEntry taskEntry) {
        PickTaskHead head = (PickTaskHead)taskEntry.getTaskHead();
        head.setPickTaskId(taskEntry.getTaskInfo().getTaskId());
        List<PickTaskDetail> details = (List<PickTaskDetail>)(List<?>)taskEntry.getTaskDetailList();
        for(PickTaskDetail detail : details){
            detail.setPickTaskId(taskEntry.getTaskInfo().getTaskId());
        }
        pickTaskService.createPickTask(head, details);
    }

    protected void getConcrete(TaskEntry taskEntry) {
        taskEntry.setTaskHead(pickTaskService.getPickTaskHead(taskEntry.getTaskInfo().getTaskId()));
        taskEntry.setTaskDetailList((List<Object>)(List<?>)pickTaskService.getPickTaskDetails(taskEntry.getTaskInfo().getTaskId()));
    }

    protected void getHeadConcrete(TaskEntry taskEntry) {
        taskEntry.setTaskHead(pickTaskService.getPickTaskHead(taskEntry.getTaskInfo().getTaskId()));
    }
}
