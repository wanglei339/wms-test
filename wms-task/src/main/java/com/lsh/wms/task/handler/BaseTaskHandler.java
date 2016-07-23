package com.lsh.wms.task.handler;

import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.model.task.Operation;
import com.lsh.wms.model.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by mali on 16/7/20.
 */

@Component
@Transactional(readOnly = true)
public class BaseTaskHandler {
    @Autowired
    private BaseTaskService baseTaskService;

    @Transactional(readOnly = false)
    public void create(Task task, List<Operation> operationList) {
        baseTaskService.create(task, operationList);
    }

    public Task getTaskById(Long taskId) {
        return baseTaskService.getTaskById(taskId);
    }

    public List<Task> getTaskList(Map<String, Object> mapQuery) {
        return baseTaskService.getTaskList(mapQuery);
    }

    /* 分配资源 */
    public void allocate(Long taskId) {
        baseTaskService.allocate(taskId);
    }

    /* 分配到人 */
    public void assigned(Long taskId, Long staffId) {
        baseTaskService.assigned(taskId, staffId);
    }


    public void done(Long taskId) {
        baseTaskService.done(taskId);
    }

    public void cancel(Long taskId) {
        baseTaskService.cancel(taskId);
    }
}
