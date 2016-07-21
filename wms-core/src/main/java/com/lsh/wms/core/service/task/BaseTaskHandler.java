package com.lsh.wms.core.service.task;

import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.dao.task.TaskInfoDao;
import com.lsh.wms.model.task.Operation;
import com.lsh.wms.model.task.TaskInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**
 * Created by mali on 16/7/20.
 */

@Component
@Transactional(readOnly = true)
public class BaseTaskHandler {
    @Autowired
    private TaskInfoDao baseDao;

    @Transactional(readOnly = false)
    public void create(TaskInfo task, List<Operation> operationList) {
        task.setCreatedAt(DateUtils.getCurrentSeconds());
        task.setUpdatedAt(DateUtils.getCurrentSeconds());
        baseDao.insert(task);
    }

    public TaskInfo getTaskInfoById(Long taskId) {
        return baseDao.getTaskInfoById(taskId);
    }

    public List<TaskInfo> getTaskInfoList(Map<String, Object> mapQuery) {
        return baseDao.getTaskInfoList(mapQuery);
    }

    @Transactional(readOnly = false)
    public void assinged(Long taskId)
    {
        TaskInfo task = baseDao.getTaskInfoById(taskId);
        task.setStatus(TaskConstant.Assigned);
        task.setUpdatedAt(DateUtils.getCurrentSeconds());
        baseDao.update(task);
    }

    @Transactional(readOnly = false)
    public void allocate(Long taskId, Long staffId) {
        TaskInfo task = baseDao.getTaskInfoById(taskId);
        task.setOperator(staffId);
        task.setStatus(TaskConstant.Allocated);
        task.setUpdatedAt(DateUtils.getCurrentSeconds());
        baseDao.update(task);
    }

    @Transactional(readOnly = false)
    public void done(Long taskId, String data) {
        TaskInfo task = baseDao.getTaskInfoById(taskId);
        task.setStatus(TaskConstant.Done);
        task.setUpdatedAt(DateUtils.getCurrentSeconds());
        baseDao.update(task);
    }

    @Transactional(readOnly = false)
    public void cancel(Long taskId) {
        TaskInfo task = baseDao.getTaskInfoById(taskId);
        task.setStatus(TaskConstant.Cancel);
        task.setUpdatedAt(DateUtils.getCurrentSeconds());
        baseDao.update(task);
    }
}
