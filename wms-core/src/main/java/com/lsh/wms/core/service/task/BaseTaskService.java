package com.lsh.wms.core.service.task;

import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.dao.task.TaskDao;
import com.lsh.wms.model.task.Operation;
import com.lsh.wms.model.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by mali on 16/7/22.
 */
public class BaseTaskService {

    @Autowired
    private TaskDao taskDao;

    @Transactional(readOnly = false)
    public void create(Task task, List<Operation> operationList) {
        task.setCreatedAt(DateUtils.getCurrentSeconds());
        task.setUpdatedAt(DateUtils.getCurrentSeconds());
        taskDao.insert(task);
    }

    public Task getTaskById(Long taskId) {
        return taskDao.getTaskById(taskId);
    }

    public List<Task> getTaskList(Map<String, Object> mapQuery) {
        return taskDao.getTaskList(mapQuery);
    }

    @Transactional(readOnly = false)
    public void allocate(Long taskId)
    {
        Task task = taskDao.getTaskById(taskId);
        task.setStatus(TaskConstant.Allocated);
        task.setUpdatedAt(DateUtils.getCurrentSeconds());
        taskDao.update(task);
    }

    @Transactional(readOnly = false)
    public void assigned(Long taskId,  Long staffId) {
        Task task = taskDao.getTaskById(taskId);
        task.setOperator(staffId);
        task.setStatus(TaskConstant.Assigned);
        task.setUpdatedAt(DateUtils.getCurrentSeconds());
        taskDao.update(task);
    }

    @Transactional(readOnly = false)
    public void done(Long taskId) {
        Task task = taskDao.getTaskById(taskId);
        task.setStatus(TaskConstant.Done);
        task.setUpdatedAt(DateUtils.getCurrentSeconds());
        taskDao.update(task);
    }

    @Transactional(readOnly = false)
    public void cancel(Long taskId) {
        Task task = taskDao.getTaskById(taskId);
        task.setStatus(TaskConstant.Cancel);
        task.setUpdatedAt(DateUtils.getCurrentSeconds());
        taskDao.update(task);
    }
}
