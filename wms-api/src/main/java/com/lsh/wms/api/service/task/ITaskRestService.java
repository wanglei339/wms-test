package com.lsh.wms.api.service.task;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.model.task.TaskEntry;

import java.util.List;
import java.util.Map;

/**
 * Created by zengwenjun on 16/7/24.
 */
public interface ITaskRestService {
    String getTaskList(Map<String, Object> mapQuery);
    String getTaskCount(Map<String, Object> mapQuery);
    String getTaskHeadList(Map<String, Object> mapQuery);
    String getTask(long taskId) throws BizCheckedException;
    String getTaskMove(long taskId) throws BizCheckedException;
}
