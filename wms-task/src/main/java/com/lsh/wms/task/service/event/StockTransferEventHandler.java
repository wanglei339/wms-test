package com.lsh.wms.task.service.event;

import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskMsg;
import com.lsh.wms.task.service.TaskRpcService;
import com.lsh.wms.task.service.handler.TaskHandlerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * Created by Ming on 9/7/16.
 */
public class StockTransferEventHandler extends AbsEventHandler implements IEventHandler {
    private static final Logger logger = LoggerFactory.getLogger(TaskFinishEventHandler.class);

    @Autowired
    private TaskRpcService taskRpcService;

    @Autowired
    private TaskHandlerFactory taskHandlerFactory;

    @Autowired
    private EventHandlerFactory eventHandlerFactory;

    @PostConstruct
    public void postConstruct() {
        eventHandlerFactory.register(TaskConstant.EVENT_STOCK_TRANSFER_CANCEL, this);
    }

    public void process(TaskMsg msg) {
        if (msg.getType().equals(TaskConstant.EVENT_STOCK_TRANSFER_CANCEL)) {
            this.cancel(msg);
        }
    }

    private void cancel(TaskMsg msg) {
        Long transferTaskId = Long.valueOf(msg.getMsgBody().get("taskId").toString());
        TaskEntry taskEntry = taskRpcService.getTaskEntryById(transferTaskId);
        if (!taskEntry.getTaskInfo().getStatus().equals(TaskConstant.Draft)) {
            return;
        }
        taskHandlerFactory.getTaskHandler(TaskConstant.TYPE_STOCK_TRANSFER).cancel(transferTaskId);
    }
}
