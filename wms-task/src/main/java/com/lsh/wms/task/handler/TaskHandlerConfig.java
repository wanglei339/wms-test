package com.lsh.wms.task.handler;

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
