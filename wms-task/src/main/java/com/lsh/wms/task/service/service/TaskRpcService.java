package com.lsh.wms.task.service.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.task.service.handler.TaskHandler;
import com.lsh.wms.task.service.handler.TaskHandlerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * Created by mali on 16/7/20.
 */


@Service(protocol = "dubbo")
public class TaskRpcService implements ITaskRpcService {
    @Autowired
    private TaskHandlerFactory handlerFactory;

    @Autowired
    private BaseTaskService baseTaskService;


    public Long create(Long taskType, TaskEntry taskEntry) {
        TaskHandler handler = handlerFactory.getTaskHandler(taskType);
        handler.create(taskEntry);
        return taskEntry.getTaskInfo().getTaskId();
    }

    public TaskEntry getTaskEntryById(Long taskId) {
        Long taskType = baseTaskService.getTaskTypeById(taskId);
        TaskHandler taskHandler = handlerFactory.getTaskHandler(taskType);
        return taskHandler.getTask(taskId);
    }

    public void assign(Long taskId, Long staffId) {
        Long taskType = baseTaskService.getTaskTypeById(taskId);
        TaskHandler taskHandler = handlerFactory.getTaskHandler(taskType);
        taskHandler.assign(taskId, staffId);
    }

    public void cancel(Long taskId) {
        Long taskType = baseTaskService.getTaskTypeById(taskId);
        TaskHandler taskHandler = handlerFactory.getTaskHandler(taskType);
        taskHandler.cancel(taskId);
    }

    public List<TaskEntry> getTaskList(Long taskType, Map<String, Object> mapQuery){
        TaskHandler taskHandler = handlerFactory.getTaskHandler(taskType);
        return taskHandler.getTaskList(mapQuery);
    }

    public void done(Long taskId) {
        Long taskType = baseTaskService.getTaskTypeById(taskId);
        TaskHandler taskHandler = handlerFactory.getTaskHandler(taskType);
        taskHandler.done(taskId);
    }
}
