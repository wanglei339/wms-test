package com.lsh.wms.task.service.handler;

import com.lsh.wms.model.task.TaskEntry;

import java.util.List;
import java.util.Map;

/**
 * Created by mali on 16/7/23.
 */
public interface TaskHandler {
    void create(TaskEntry taskEntry);
    TaskEntry getTask(Long taskId);
    List<TaskEntry> getTaskList(Map<String, Object> condition);
    void assign(Long taskId, Long staffId);
    void allocate(Long taskId);
    void release(Long taskId);
    void done(Long taskId);
    void cancel(Long taskId);
}
