package com.lsh.wms.task.handler;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by mali on 16/7/21.
 */
public class TaskHandlerConfig {
    @Autowired
    private StockTakingTaskHandler stockTakingTaskHandler;
    @Autowired
    private ShelveTaskHandler shelveTaskHandler;
}
