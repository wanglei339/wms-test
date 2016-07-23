package com.lsh.wms.task.service;

import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.model.task.Operation;
import com.lsh.wms.model.task.Task;
import com.lsh.wms.task.handler.BaseTaskHandler;
import com.lsh.wms.task.handler.TaskHandlerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by mali on 16/7/20.
 */
@Component
public class DispatcherRpcService implements ITaskRpcService {
    @Autowired
    private TaskHandlerFactory handlerFactory;

    public void create(Long taskType, Task task, List<Operation> operationList) {
        BaseTaskHandler taskHandler = handlerFactory.getTaskHandler(taskType);
        taskHandler.create(task, operationList);
    }

    public void assign(Long taskType, Long taskId, Long staffId) {
        BaseTaskHandler taskHandler = handlerFactory.getTaskHandler(taskType);
        taskHandler.assigned(taskId, staffId);
    }

    public void cancel(Long taskType, Long taskId) {
        BaseTaskHandler taskHandler = handlerFactory.getTaskHandler(taskType);
        taskHandler.cancel(taskId);
    }

    public List<Task> getTaskList(Long taskType, Map<String, Object> mapQuery){
        BaseTaskHandler taskHandler = handlerFactory.getTaskHandler(taskType);
        return taskHandler.getTaskList(mapQuery);
    }
}
