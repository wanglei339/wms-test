package com.lsh.wms.api.service.task;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.model.task.TaskEntry;

import java.util.List;
import java.util.Map;

/**
 * Created by mali on 16/7/20.
 */
public interface ITaskRpcService {
    Long create(Long taskType, TaskEntry taskEntry) throws BizCheckedException;
    List<Long> batchCreate(Long taskType, List<TaskEntry> taskEntries) throws BizCheckedException;
    Long getTaskTypeById(Long taskId) throws BizCheckedException;
    TaskEntry getTaskEntryById(Long taskId) throws BizCheckedException;
    void assign(Long taskId, Long staffId) throws BizCheckedException;
    void cancel(Long taskId) throws BizCheckedException;
    List<TaskEntry> getTaskList(Long taskType, Map<String, Object> mapQuery);
    int getTaskCount(Long taskType, Map<String, Object> mapQuery);
    List<TaskEntry> getTaskHeadList(Long taskType, Map<String, Object> mapQuery);
    void done(Long taskId) throws BizCheckedException;
    void done(Long taskId, Long locationId) throws BizCheckedException;
}
