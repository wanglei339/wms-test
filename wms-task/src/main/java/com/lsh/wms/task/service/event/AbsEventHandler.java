package com.lsh.wms.task.service.event;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.service.task.TaskHandler;
import com.lsh.wms.core.service.task.TaskTriggerService;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskMsg;
import com.lsh.wms.model.task.TaskTrigger;
import com.lsh.wms.task.service.TaskRpcService;
import com.lsh.wms.task.service.handler.TaskHandlerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * Created by mali on 16/8/19.
 */
@Component
public class AbsEventHandler implements IEventHandler{

    private static final Logger logger = LoggerFactory.getLogger(AbsEventHandler.class);

    @Autowired
    private TaskRpcService taskRpcService;

    @Autowired
    private TaskHandlerFactory handlerFactory;

    @Autowired
    private TaskTriggerService triggerService;

    @Autowired
    private EventHandlerFactory eventHandlerFactory;

    @PostConstruct
    public void postConstruct() {
        eventHandlerFactory.register(0L, this);
    }

    public void process(Long taskId) {
        //StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        //StackTraceElement element = stacktrace[2];
        //String methodName = element.getMethodName();

        Map<String, List<TaskTrigger>> triggerMap = triggerService.getAll();
        Long taskType = taskRpcService.getTaskTypeById(taskId);
        TaskHandler taskHandler = handlerFactory.getTaskHandler(taskType);

        String key = "" + taskType + taskRpcService.getTaskEntryById(taskId).getTaskInfo().getSubType() + "done" + 1L;
        List<TaskTrigger> triggerList = triggerMap.get(key);
        if (null == triggerList) {
            String key2 = "" + taskType + "0" + "done" + 1L;
            triggerList = triggerMap.get(key2);
            if (null == triggerList) {
                return;
            }
        }
        for(TaskTrigger trigger : triggerList) {
            TaskHandler handler = handlerFactory.getTaskHandler(trigger.getDestType());
            try {
                Method method = handler.getClass().getDeclaredMethod(trigger.getDestMethod(), Long.class);
                method.invoke(handler, taskId);
            } catch (BizCheckedException e) {
                logger.error("Exception",e);
                logger.warn(e.getMessage());
            }catch (Exception e) {
                logger.error("Exception",e);
                logger.warn(e.getCause().getMessage());
            }
        }
    }

    public void process(TaskMsg msg) {

    }
}
