package com.lsh.wms.task.service.event;

import org.springframework.stereotype.Component;
import java.beans.EventHandler;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mali on 16/8/19.
 */
@Component
public class EventHandlerFactory {
    private Map<Long, IEventHandler> handlerMap = new HashMap<Long, IEventHandler>();

    public void register(Long type, IEventHandler handler) {
        handlerMap.put(type, handler);
    }

    public IEventHandler getEventHandler(Long type) {
        if (handlerMap.get(type) == null) {
            type = 0L;
        }
        return (IEventHandler) handlerMap.get(type);
    }
}
