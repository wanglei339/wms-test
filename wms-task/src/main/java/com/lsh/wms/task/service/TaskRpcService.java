package com.lsh.wms.task.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.constant.LocationConstant;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.service.item.ItemLocationService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.core.service.task.MessageService;
import com.lsh.wms.core.service.task.TaskTriggerService;
import com.lsh.wms.model.baseinfo.BaseinfoItemLocation;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.core.service.task.TaskHandler;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.model.task.TaskMsg;
import com.lsh.wms.model.task.TaskTrigger;
import com.lsh.wms.task.service.event.EventHandlerFactory;
import com.lsh.wms.task.service.event.IEventHandler;
import com.lsh.wms.task.service.handler.TaskHandlerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.beans.EventHandler;
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

    @Autowired
    private ItemLocationService itemLocationService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private EventHandlerFactory eventHandlerFactory;

    @Autowired
    private MessageService messageService;

    private String getCurrentMethodName() {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement e = stacktrace[2];
        String methodName = e.getMethodName();
        return methodName;
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

    public void assignMul(List<Map<String, Long>> params) throws BizCheckedException {
        for (Map<String, Long> param: params) {
            Long taskType = this.getTaskTypeById(param.get("taskId"));
            TaskHandler taskHandler = handlerFactory.getTaskHandler(taskType);
            taskHandler.assign(param.get("taskId"), param.get("staffId"), param.get("containerId"));
        }
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
        this.afterDone(taskId);
    }

    public void done(Long taskId, Long locationId) throws BizCheckedException {
        Long taskType = this.getTaskTypeById(taskId);
        TaskHandler taskHandler = handlerFactory.getTaskHandler(taskType);
        taskHandler.done(taskId, locationId);
        this.afterDone(taskId);
    }

    public void done(Long taskId, Long locationId, Long staffId) throws BizCheckedException {
        Long taskType = this.getTaskTypeById(taskId);
        TaskHandler taskHandler = handlerFactory.getTaskHandler(taskType);
        taskHandler.done(taskId, locationId, staffId);
        this.afterDone(taskId);
    }

    public void afterDone(Long taskId) throws BizCheckedException {
        try {
            TaskMsg msg = new TaskMsg();
            msg.setSourceTaskId(taskId);
            msg.setType(TaskConstant.EVENT_TASK_FINISH);
            messageService.sendMessage(msg);
        } catch (Exception e){
            logger.error("AfterDone Exception", e);
        }
    }

    public List<Map<String,Object>> getPerformance(Map<String, Object> condition) {
        return baseTaskService.getPerformance(condition);
    }

    public TaskInfo getTaskInfo(Long taskId) throws BizCheckedException {
        return baseTaskService.getTaskInfoById(taskId);
    }
}
