package com.lsh.wms.core.service.task;

import org.codehaus.jackson.map.deser.ValueInstantiators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mali on 16/7/20.
 */
@Component
public class TaskHandlerFactory {
    private Map<Long, BaseTaskHandler> handlerMap = new HashMap<Long, BaseTaskHandler>();

    public void register(Long taskType, BaseTaskHandler handler) {
        handlerMap.put(taskType, handler);
    }

    public BaseTaskHandler getTaskHandler(Long taskType) {
        return (BaseTaskHandler)  handlerMap.get(taskType);
    }

}
