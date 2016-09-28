package com.lsh.wms.task.service.task.seed;

import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.task.service.handler.AbsTaskHandler;
import com.lsh.wms.task.service.handler.TaskHandlerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by mali on 16/8/2.
 */
@Component
public class SeedTaskHandler extends AbsTaskHandler {
    @Autowired
    private TaskHandlerFactory handlerFactory;

    @PostConstruct
    public void postConstruct() {
        handlerFactory.register(TaskConstant.TYPE_PROCUREMENT, this);
    }

    public void calcPerformance(TaskInfo taskInfo) {

        taskInfo.setTaskPackQty(taskInfo.getQtyDone());
        taskInfo.setTaskEaQty(taskInfo.getQtyDone().multiply(taskInfo.getPackUnit()));


    }
}
