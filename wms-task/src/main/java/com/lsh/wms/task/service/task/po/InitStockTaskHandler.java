package com.lsh.wms.task.service.task.po;

import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.task.service.handler.AbsTaskHandler;
import com.lsh.wms.task.service.handler.TaskHandlerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by mali on 16/12/28.
 */
@Component
public class InitStockTaskHandler extends AbsTaskHandler {

    @Autowired
    private static final Logger logger = LoggerFactory.getLogger(InitStockTaskHandler.class);
    @Autowired
    private TaskHandlerFactory handlerFactory;

    @PostConstruct
    public void postConstruct() {
        handlerFactory.register(TaskConstant.TYPE_INIT_STOCK, this);
    }
}
