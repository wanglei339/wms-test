package com.lsh.wms.task.service.event;

import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.service.task.TaskHandler;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskMsg;
import com.lsh.wms.task.service.TaskRpcService;
import com.lsh.wms.task.service.handler.TaskHandlerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mali on 16/8/24.
 */
@Component
public class ProcurementEventHandler extends AbsEventHandler implements IEventHandler {
    private static final Logger logger = LoggerFactory.getLogger(TaskFinishEventHandler.class);

    @Autowired
    private TaskRpcService taskRpcService;

    @Autowired
    private TaskHandlerFactory taskHandlerFactory;

    @Autowired
    private EventHandlerFactory eventHandlerFactory;


    @PostConstruct
    public void postConstruct() {
        eventHandlerFactory.register(TaskConstant.EVENT_OUT_OF_STOCK, this);
        eventHandlerFactory.register(TaskConstant.EVENT_SO_ACCEPT, this);
        eventHandlerFactory.register(TaskConstant.EVENT_WAVE_RELEASE, this);
        eventHandlerFactory.register(TaskConstant.EVENT_PROCUREMENT_CANCEL, this);
    }

    public void process(TaskMsg msg) {
        if (msg.getType().equals(TaskConstant.EVENT_PROCUREMENT_CANCEL)) {
            this.cancel(msg);
        } else {
            this.adjustPriority(msg);
        }
    }

    private void adjustPriority(TaskMsg msg) {
        TaskHandler handler = taskHandlerFactory.getTaskHandler(TaskConstant.TYPE_PROCUREMENT);
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("itemId", Long.valueOf(msg.getMsgBody().get("itemId").toString()));
        if (msg.getType().equals(TaskConstant.EVENT_OUT_OF_STOCK)) {
            mapQuery.put("locationId", Long.valueOf(msg.getMsgBody().get("locationId").toString()));
        }
        List<TaskEntry> taskEntryList = taskRpcService.getTaskList(TaskConstant.TYPE_PROCUREMENT, mapQuery);

        for(TaskEntry entry: taskEntryList){
            Long taskId = entry.getTaskInfo().getTaskId();
            Long newPriority = msg.getType() - 100000L + 1L;
            handler.setPriority(taskId, newPriority);
        }
    }

    private void cancel(TaskMsg msg) {
        Long procurementTaskId = Long.valueOf(msg.getMsgBody().get("taskId").toString());
        TaskEntry taskEntry = taskRpcService.getTaskEntryById(procurementTaskId);
        if (taskEntry.getTaskInfo().getStatus().equals(TaskConstant.Draft)) {
            return;
        }
        taskHandlerFactory.getTaskHandler(TaskConstant.TYPE_PROCUREMENT).cancel(procurementTaskId);
    }
}
