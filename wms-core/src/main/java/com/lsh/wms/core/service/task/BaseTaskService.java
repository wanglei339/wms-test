package com.lsh.wms.core.service.task;

import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.dao.task.TaskInfoDao;
import com.lsh.wms.model.task.TaskInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by mali on 16/7/22.
 */
@Component
@Transactional(readOnly = true)
public class BaseTaskService {

    @Autowired
    private TaskInfoDao taskInfoDao;

    @Transactional(readOnly = false)
    public void create(TaskInfo taskInfo) {
        taskInfo.setCreatedAt(DateUtils.getCurrentSeconds());
        taskInfo.setUpdatedAt(DateUtils.getCurrentSeconds());
        taskInfoDao.insert(taskInfo);
    }

    public TaskInfo getTaskInfoById(Long taskId) {
        return taskInfoDao.getTaskInfoById(taskId);
    }

    public List<TaskInfo> getTaskInfoList(Map<String, Object> mapQuery) {
        return taskInfoDao.getTaskInfoList(mapQuery);
    }

    public int getTaskInfoCount(Map<String, Object> mapQuery) {
        return taskInfoDao.countTaskInfo(mapQuery);
    }

    public Long getTaskTypeById(Long taskId) {
        TaskInfo info = taskInfoDao.getTaskInfoById(taskId);
        if(info == null){
            return -1L;
        }else{
            return info.getType();
        }
    }

    @Transactional(readOnly = false)
    public void allocate(Long taskId)
    {
        TaskInfo taskInfo = getTaskInfoById(taskId);
        taskInfo.setStatus(TaskConstant.Allocated);
        taskInfo.setUpdatedAt(DateUtils.getCurrentSeconds());
        taskInfoDao.update(taskInfo);
    }

    @Transactional(readOnly = false)
    public void assign(Long taskId,  Long staffId) {
        TaskInfo taskInfo = taskInfoDao.getTaskInfoById(taskId);
        taskInfo.setOperator(staffId);
        taskInfo.setStatus(TaskConstant.Assigned);
        taskInfo.setUpdatedAt(DateUtils.getCurrentSeconds());
        taskInfoDao.update(taskInfo);
    }

    @Transactional(readOnly = false)
    public void done(Long taskId) {
        TaskInfo taskInfo = taskInfoDao.getTaskInfoById(taskId);
        taskInfo.setStatus(TaskConstant.Done);
        taskInfo.setUpdatedAt(DateUtils.getCurrentSeconds());
        taskInfoDao.update(taskInfo);
    }

    @Transactional(readOnly = false)
    public void cancel(Long taskId) {
        TaskInfo taskInfo = taskInfoDao.getTaskInfoById(taskId);
        taskInfo.setStatus(TaskConstant.Cancel);
        taskInfo.setUpdatedAt(DateUtils.getCurrentSeconds());
        taskInfoDao.update(taskInfo);
    }
}
