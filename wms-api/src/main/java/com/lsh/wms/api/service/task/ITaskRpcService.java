package com.lsh.wms.api.service.task;

import com.lsh.wms.model.task.TaskEntry;

import java.util.List;

/**
 * Created by mali on 16/7/20.
 */
public interface ITaskRpcService {
    Long create(Long taskType, TaskEntry taskEntry);
    List<Long> batchCreate(Long taskType, List<TaskEntry> taskEntries);
}
