package com.lsh.wms.api.service.task;

import com.lsh.wms.model.task.Operation;
import com.lsh.wms.model.task.TaskInfo;

import java.util.List;

/**
 * Created by mali on 16/7/20.
 */
public interface ITaskRpcService {
    void create(Long taskType, TaskInfo task, List<Operation> operationList);
}
