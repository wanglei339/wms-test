package com.lsh.wms.task.service.task.procurement;

import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.task.service.handler.AbsTaskHandler;
import com.lsh.wms.task.service.handler.TaskHandlerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by mali on 16/8/2.
 */
@Component
public class ProcurementService extends AbsTaskHandler {
    @Autowired
    private TaskHandlerFactory handlerFactory;

    @PostConstruct
    public void postConstruct() {
        handlerFactory.register(TaskConstant.TYPE_PROCUREMENT, this);
    }
}
