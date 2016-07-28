package com.lsh.wms.api.service.task;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.model.task.StockTakingTask;
import com.lsh.wms.model.task.TaskInfo;

/**
 * Created by mali on 16/7/23.
 */
public interface ITestRestService {
    String init(TaskInfo stockTakingInfo) throws BizCheckedException;
}
