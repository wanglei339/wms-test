package com.lsh.wms.task.service.handler;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mali on 16/7/20.
 */

@Component
public class TaskHandlerFactory {
    private Map<Long, TaskHandler> handlerMap = new HashMap<Long, TaskHandler>();

    public void register(Long taskType, TaskHandler handler) {
        handlerMap.put(taskType, handler);
    }

    public TaskHandler getTaskHandler(Long taskType) {
        return (TaskHandler)  handlerMap.get(taskType);
    }

}
