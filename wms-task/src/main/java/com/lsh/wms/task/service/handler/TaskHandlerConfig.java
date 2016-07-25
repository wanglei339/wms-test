package com.lsh.wms.task.service.handler;

import com.lsh.wms.task.service.task.taking.StockTakingTaskHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by mali on 16/7/21.
 */
@Component
public class TaskHandlerConfig {
    @Autowired
    private StockTakingTaskHandler stockTakingTaskHandler;
}
