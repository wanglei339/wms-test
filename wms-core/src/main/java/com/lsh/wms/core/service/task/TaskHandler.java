package com.lsh.wms.core.service.task;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by mali on 16/7/23.
 */
public interface TaskHandler {
    void create(TaskEntry taskEntry) throws BizCheckedException;
    public void createConcrete(TaskEntry taskEntry) throws BizCheckedException;
    void batchCreate(List<TaskEntry> taskEntries) throws BizCheckedException;
    void batchAssign(List<Long> tasks,Long staffId) throws BizCheckedException;
    TaskEntry getTask(Long taskId);
    List<TaskEntry> getTaskList(Map<String, Object> condition);
    int getTaskCount(Map<String, Object> condition);
    List<TaskEntry> getTaskHeadList(Map<String, Object> condition);
    void assign(Long taskId, Long staffId) throws BizCheckedException;
    public void assignConcrete(Long taskId, Long staffId) throws BizCheckedException;
    void allocate(Long taskId);
    void release(Long taskId);
    void done(Long taskId);
    void done(Long taskId, Long locationId) throws BizCheckedException;
    void cancel(Long taskId);
}
