package com.lsh.wms.task.service.event;

import com.lsh.wms.model.task.TaskMsg;

/**
 * Created by mali on 16/8/13.
 */
public interface IEventHandler {
    void process(Long taskId);
    void process(TaskMsg msg);
}
