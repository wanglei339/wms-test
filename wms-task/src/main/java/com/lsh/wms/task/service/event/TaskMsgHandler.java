package com.lsh.wms.task.service.event;

import com.lsh.wms.core.service.task.MessageService;
import com.lsh.wms.model.task.TaskMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by mali on 16/8/17.
 */
@Component
public class TaskMsgHandler {
    @Autowired
    private MessageService msgService;
}
