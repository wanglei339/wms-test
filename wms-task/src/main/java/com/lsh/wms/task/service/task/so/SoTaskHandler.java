package com.lsh.wms.task.service.task.so;

import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.service.po.PoOrderService;
import com.lsh.wms.core.service.so.SoOrderService;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.task.service.handler.AbsTaskHandler;
import com.lsh.wms.task.service.handler.TaskHandlerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Project Name: lsh-wms
 * Created by fuhao
 * Date: 16/7/23
 * Time: 16/7/23.
 * 北京链商电子商务有限公司
 * Package name:com.lsh.wms.task.service.task.po.
 * desc:类功能描述
 */
@Component
public class SoTaskHandler extends AbsTaskHandler {
    @Autowired
    private TaskHandlerFactory handlerFactory;
    @Autowired
    private SoOrderService soOrderService;

    @PostConstruct
    public void postConstruct() {
        handlerFactory.register(TaskConstant.TYPE_PICK, this);
    }

    protected void getConcrete(TaskEntry taskEntry) {
        taskEntry.setTaskHead(soOrderService.getOutbSoHeaderByOrderId(taskEntry.getTaskInfo().getOrderId()));
        taskEntry.setTaskDetailList((List<Object>)(List<?>)soOrderService.getOutbSoDetailListByOrderId(taskEntry.getTaskInfo().getOrderId()));
    }

    protected void getHeadConcrete(TaskEntry taskEntry) {
        taskEntry.setTaskHead(soOrderService.getOutbSoHeaderByOrderId(taskEntry.getTaskInfo().getOrderId()));
    }
}
