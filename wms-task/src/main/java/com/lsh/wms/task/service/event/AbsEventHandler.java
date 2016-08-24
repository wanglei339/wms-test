package com.lsh.wms.task.service.event;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.service.task.TaskHandler;
import com.lsh.wms.core.service.task.TaskTriggerService;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskMsg;
import com.lsh.wms.model.task.TaskTrigger;
import com.lsh.wms.task.service.TaskRpcService;
import com.lsh.wms.task.service.handler.TaskHandlerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * Created by mali on 16/8/19.
 */
@Component
public class AbsEventHandler implements IEventHandler{

    private static final Logger logger = LoggerFactory.getLogger(AbsEventHandler.class);

    public void process(TaskMsg msg) {
    }
}
