package com.lsh.wms.rpc.service.task;

import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.model.task.Operation;
import com.lsh.wms.model.task.TaskInfo;

import java.util.List;

/**
 * Created by mali on 16/7/20.
 */
public class TaskRpcService implements ITaskRpcService {
    public void create(Long taskType, TaskInfo task, List<Operation> operationList) {return;}
}
