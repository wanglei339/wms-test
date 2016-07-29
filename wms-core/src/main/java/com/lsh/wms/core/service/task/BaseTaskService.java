package com.lsh.wms.core.service.task;

import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.dao.task.TaskInfoDao;
import com.lsh.wms.model.task.TaskInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
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
        taskInfo.setDraftTime(DateUtils.getCurrentSeconds());
        taskInfo.setStatus(TaskConstant.Draft);
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
        taskInfo.setAssignTime(DateUtils.getCurrentSeconds());
        taskInfo.setAssignTime(DateUtils.getCurrentSeconds());
        taskInfo.setUpdatedAt(DateUtils.getCurrentSeconds());
        taskInfoDao.update(taskInfo);
    }

    @Transactional(readOnly = false)
    public void done(Long taskId) {
        TaskInfo taskInfo = taskInfoDao.getTaskInfoById(taskId);
        taskInfo.setStatus(TaskConstant.Done);
        taskInfo.setFinishTime(DateUtils.getCurrentSeconds());
        taskInfo.setUpdatedAt(DateUtils.getCurrentSeconds());
        taskInfoDao.update(taskInfo);
    }

    @Transactional(readOnly = false)
    public void cancel(Long taskId) {
        TaskInfo taskInfo = taskInfoDao.getTaskInfoById(taskId);
        taskInfo.setStatus(TaskConstant.Cancel);
        taskInfo.setCancelTime(DateUtils.getCurrentSeconds());
        taskInfo.setUpdatedAt(DateUtils.getCurrentSeconds());
        taskInfoDao.update(taskInfo);
    }

    /**
     * 根据container_id判断是否有运行中的任务
     * @param containerId
     * @return
     */
    public Boolean checkTaskByContainerId (Long containerId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("containerId", containerId);
        List<TaskInfo> taskInfos = taskInfoDao.getTaskInfoList(params);
        for (TaskInfo taskInfo : taskInfos) {
            if (!taskInfo.getStatus().equals(TaskConstant.Done) && !taskInfo.getStatus().equals(TaskConstant.Cancel)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据container_id获取未分配的任务id
     * @param containerId
     * @return
     */
    public Long getDraftTaskIdByContainerId (Long containerId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("containerId", containerId);
        params.put("status", TaskConstant.Draft);
        List<TaskInfo> taskInfos = taskInfoDao.getTaskInfoList(params);
        if (taskInfos.size() == 0) {
            return null;
        }
        return taskInfos.get(0).getTaskId();
    }
}
