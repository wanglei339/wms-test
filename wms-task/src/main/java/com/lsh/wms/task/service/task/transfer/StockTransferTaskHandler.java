package com.lsh.wms.task.service.task.transfer;

import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.service.transfer.StockTransferTaskService;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.transfer.StockTransferPlan;
import com.lsh.wms.model.transfer.StockTransferTaskDetail;
import com.lsh.wms.task.service.handler.AbsTaskHandler;
import com.lsh.wms.task.service.handler.TaskHandlerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by mali on 16/7/25.
 */
@Component
public class StockTransferTaskHandler extends AbsTaskHandler {
    @Autowired
    private StockTransferTaskService stockTransferTaskService;

    @Autowired
    private TaskHandlerFactory handlerFactory;

    @PostConstruct
    public void postConstruct() {
        handlerFactory.register(TaskConstant.TYPE_STOCK_TRANSFER, this);
    }

    protected void createConcrete(TaskEntry taskEntry) {
    }

    protected void getConcrete(TaskEntry taskEntry) {
    }

}
