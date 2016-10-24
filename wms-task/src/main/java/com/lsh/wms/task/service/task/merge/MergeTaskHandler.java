package com.lsh.wms.task.service.task.merge;

import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.task.service.handler.AbsTaskHandler;
import com.lsh.wms.task.service.handler.TaskHandlerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by fengkun on 2016/10/24.
 */
@Component
public class MergeTaskHandler extends AbsTaskHandler {
    @Autowired
    private TaskHandlerFactory handlerFactory;
    @PostConstruct
    public void postConstruct() {
        handlerFactory.register(TaskConstant.TYPE_MERGE, this);
    }
}
