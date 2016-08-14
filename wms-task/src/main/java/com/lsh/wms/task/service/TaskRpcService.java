package com.lsh.wms.task.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.core.service.task.TaskTriggerService;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.core.service.task.TaskHandler;
import com.lsh.wms.model.task.TaskTrigger;
import com.lsh.wms.task.service.handler.TaskHandlerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by mali on 16/7/20.
 */


@Service(protocol = "dubbo")
public class TaskRpcService implements ITaskRpcService {

    private static final Logger logger = LoggerFactory.getLogger(TaskRpcService.class);

    @Autowired
    private TaskHandlerFactory handlerFactory;

    @Autowired
    private BaseTaskService baseTaskService;

    @Autowired
    private TaskTriggerService triggerService;

    private Map<String, List<TaskTrigger>> triggerMap;

    public TaskRpcService() {
        triggerMap = triggerService.getAll();
    }

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
    public void batchAssign(Long taskType,List<Long> tasks,Long staffId) throws BizCheckedException {
        TaskHandler handler = handlerFactory.getTaskHandler(taskType);
        handler.batchAssign(tasks, staffId);
    }
    public void update(Long taskType,TaskEntry entry) throws BizCheckedException {
        TaskHandler handler = handlerFactory.getTaskHandler(taskType);
        handler.update(entry);
    }
    public void batchCancel(Long taskType,List<Long> tasks) throws BizCheckedException {
        TaskHandler handler = handlerFactory.getTaskHandler(taskType);
        handler.batchCancel(tasks);
    }

    public Long getTaskTypeById(Long taskId) throws BizCheckedException{
        Long taskType = baseTaskService.getTaskTypeById(taskId);
        if(taskType.equals(-1L)){
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

    public void assign(Long taskId, Long staffId, Long containerId) throws BizCheckedException{
        Long taskType = this.getTaskTypeById(taskId);
        TaskHandler taskHandler = handlerFactory.getTaskHandler(taskType);
        taskHandler.assign(taskId, staffId, containerId);
    }

    public void cancel(Long taskId) throws BizCheckedException{
        Long taskType = this.getTaskTypeById(taskId);
        TaskHandler taskHandler = handlerFactory.getTaskHandler(taskType);
        taskHandler.cancel(taskId);
    }

    public List<TaskEntry> getTaskList(Long taskType, Map<String, Object> mapQuery){
        TaskHandler taskHandler = handlerFactory.getTaskHandler(taskType);
        mapQuery.put("type", taskType);
        return taskHandler.getTaskList(mapQuery);
    }

    public int getTaskCount(Long taskType, Map<String, Object> mapQuery){
        TaskHandler taskHandler = handlerFactory.getTaskHandler(taskType);
        return taskHandler.getTaskCount(mapQuery);
    }

    public List<TaskEntry> getTaskHeadList(Long taskType, Map<String, Object> mapQuery){
        TaskHandler taskHandler = handlerFactory.getTaskHandler(taskType);
        mapQuery.put("type", taskType);
        return taskHandler.getTaskHeadList(mapQuery);
    }

    public void done(Long taskId) throws BizCheckedException {
        Long taskType = this.getTaskTypeById(taskId);
        TaskHandler taskHandler = handlerFactory.getTaskHandler(taskType);
        taskHandler.done(taskId);

        String key = taskType + taskHandler.getClass().getName() + 1L;
        List<TaskTrigger> triggerList = triggerMap.get(key);
        for(TaskTrigger trigger : triggerList) {
            TaskHandler handler = handlerFactory.getTaskHandler(taskType);
            try {
                Method method = handler.getClass().getDeclaredMethod(trigger.getDestMethod(), TaskEntry.class);
                method.invoke(handler, this.getTaskEntryById(taskId));
            } catch (Exception e) {
                logger.warn(e.getMessage());
            }
        }
    }

    public void done(Long taskId, Long locationId) throws BizCheckedException {
        Long taskType = this.getTaskTypeById(taskId);
        TaskHandler taskHandler = handlerFactory.getTaskHandler(taskType);
        taskHandler.done(taskId, locationId);
    }

    public void done(Long taskId, Long locationId, Long staffId) throws BizCheckedException {
        Long taskType = this.getTaskTypeById(taskId);
        TaskHandler taskHandler = handlerFactory.getTaskHandler(taskType);
        taskHandler.done(taskId, locationId, staffId);
    }

}
