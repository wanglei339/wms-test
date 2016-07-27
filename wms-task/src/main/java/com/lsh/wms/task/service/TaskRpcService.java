package com.lsh.wms.task.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.task.service.handler.TaskHandler;
import com.lsh.wms.task.service.handler.TaskHandlerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedList;
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


    public Long create(Long taskType, TaskEntry taskEntry) throws BizCheckedException{
        TaskHandler handler = handlerFactory.getTaskHandler(taskType);
        handler.create(taskEntry);
        return taskEntry.getTaskInfo().getTaskId();
    }

    public List<Long> batchCreate(Long taskType, List<TaskEntry> taskEntries) throws BizCheckedException{
        TaskHandler handler = handlerFactory.getTaskHandler(taskType);
        handler.batchCreate(taskEntries);
        List<Long> idList = new LinkedList<Long>();
        for(TaskEntry entry : taskEntries) {
            idList.add(entry.getTaskInfo().getTaskId());
        }
        return idList;
    }

    public Long getTaskTypeById(Long taskId) throws BizCheckedException{
        Long taskType = baseTaskService.getTaskTypeById(taskId);
        if(taskType == -1){
            throw new BizCheckedException("2000001");
        }else{
            return taskType;
        }
    }


    public TaskEntry getTaskEntryById(Long taskId) throws BizCheckedException{
        Long taskType = this.getTaskTypeById(taskId);
        TaskHandler taskHandler = handlerFactory.getTaskHandler(taskType);
        return taskHandler.getTask(taskId);
    }


    public void assign(Long taskId, Long staffId) throws BizCheckedException{
        Long taskType = this.getTaskTypeById(taskId);
        TaskHandler taskHandler = handlerFactory.getTaskHandler(taskType);
        taskHandler.assign(taskId, staffId);
    }

    public void cancel(Long taskId) throws BizCheckedException{
        Long taskType = this.getTaskTypeById(taskId);
        TaskHandler taskHandler = handlerFactory.getTaskHandler(taskType);
        taskHandler.cancel(taskId);
    }

    public List<TaskEntry> getTaskList(Long taskType, Map<String, Object> mapQuery){
        TaskHandler taskHandler = handlerFactory.getTaskHandler(taskType);
        return taskHandler.getTaskList(mapQuery);
    }

    public int getTaskCount(Long taskType, Map<String, Object> mapQuery){
        TaskHandler taskHandler = handlerFactory.getTaskHandler(taskType);
        return taskHandler.getTaskCount(mapQuery);
    }

    public List<TaskEntry> getTaskHeadList(Long taskType, Map<String, Object> mapQuery){
        TaskHandler taskHandler = handlerFactory.getTaskHandler(taskType);
        return taskHandler.getTaskHeadList(mapQuery);
    }

    public void done(Long taskId) throws BizCheckedException {
        Long taskType = this.getTaskTypeById(taskId);
        TaskHandler taskHandler = handlerFactory.getTaskHandler(taskType);
        taskHandler.done(taskId);
    }

    public void done(Long taskId, Long locationId) throws BizCheckedException {
        Long taskType = this.getTaskTypeById(taskId);
        TaskHandler taskHandler = handlerFactory.getTaskHandler(taskType);
        taskHandler.done(taskId, locationId);
    }
}
