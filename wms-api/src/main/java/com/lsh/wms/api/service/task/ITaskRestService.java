package com.lsh.wms.api.service.task;

import com.lsh.base.common.exception.BizCheckedException;

import java.util.Map;

/**
 * Created by zengwenjun on 16/7/24.
 */
public interface ITaskRestService {
    String getTaskList(Map<String, Object> mapQuery);
    String getTaskCount(Map<String, Object> mapQuery);
    String getTask(long taskId) throws BizCheckedException;
    String getTaskMove(long taskId) throws BizCheckedException;
}
