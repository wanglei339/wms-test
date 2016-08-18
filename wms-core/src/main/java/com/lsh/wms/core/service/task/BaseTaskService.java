package com.lsh.wms.core.service.task;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.dao.task.TaskInfoDao;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by mali on 16/7/22.
 */
@Component
@Transactional(readOnly = true)
public class BaseTaskService {

    @Autowired
    private TaskInfoDao taskInfoDao;

    @Transactional(readOnly = false)
    public void create(TaskEntry taskEntry, TaskHandler taskHandler) throws BizCheckedException {
        TaskInfo taskInfo = taskEntry.getTaskInfo();
        taskInfo.setDraftTime(DateUtils.getCurrentSeconds());
        taskInfo.setStatus(TaskConstant.Draft);
        taskInfo.setCreatedAt(DateUtils.getCurrentSeconds());
        taskInfo.setUpdatedAt(DateUtils.getCurrentSeconds());
        taskInfoDao.insert(taskInfo);
        taskHandler.createConcrete(taskEntry);
    }

    @Transactional(readOnly = false)
    public void batchCreate(List<TaskEntry> taskEntries, TaskHandler taskHandler) throws BizCheckedException {
        for(TaskEntry taskEntry : taskEntries) {
            TaskInfo taskInfo = taskEntry.getTaskInfo();
            taskInfo.setDraftTime(DateUtils.getCurrentSeconds());
            taskInfo.setStatus(TaskConstant.Draft);
            taskInfo.setCreatedAt(DateUtils.getCurrentSeconds());
            taskInfo.setUpdatedAt(DateUtils.getCurrentSeconds());
            taskInfoDao.insert(taskInfo);
            taskHandler.createConcrete(taskEntry);
        }
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
    public void allocate(Long taskId, TaskHandler taskHandler)
    {
        TaskInfo taskInfo = getTaskInfoById(taskId);
        taskInfo.setStatus(TaskConstant.Allocated);
        taskInfo.setUpdatedAt(DateUtils.getCurrentSeconds());
        taskInfoDao.update(taskInfo);
        taskHandler.allocateConcrete(taskId);
    }

    @Transactional(readOnly = false)
    public void assign(Long taskId, Long staffId, TaskHandler taskHandler) throws BizCheckedException {
        TaskInfo taskInfo = taskInfoDao.getTaskInfoById(taskId);
        taskInfo.setOperator(staffId);
        taskInfo.setStatus(TaskConstant.Assigned);
        taskInfo.setAssignTime(DateUtils.getCurrentSeconds());
        taskInfo.setUpdatedAt(DateUtils.getCurrentSeconds());
        taskInfoDao.update(taskInfo);
        taskHandler.assignConcrete(taskId, staffId);
    }
    @Transactional(readOnly = false)
    public void update(TaskEntry taskEntry, TaskHandler taskHandler) throws BizCheckedException {
        TaskInfo taskInfo = taskInfoDao.getTaskInfoById(taskEntry.getTaskInfo().getTaskId());;
        taskInfoDao.update(taskInfo);
        taskHandler.updteConcrete(taskEntry);
    }
    @Transactional(readOnly = false)
    public void batchAssign(List<Long> taskList,  Long staffId,TaskHandler taskHandler) {
        for(Long taskId:taskList) {
            TaskInfo taskInfo = taskInfoDao.getTaskInfoById(taskId);
            taskInfo.setOperator(staffId);
            taskInfo.setStatus(TaskConstant.Assigned);
            taskInfo.setAssignTime(DateUtils.getCurrentSeconds());
            taskInfo.setUpdatedAt(DateUtils.getCurrentSeconds());
            taskInfoDao.update(taskInfo);
            taskHandler.assignConcrete(taskId, staffId);
        }
    }

    @Transactional(readOnly = false)
    public void assign(Long taskId, Long staffId, Long containerId, TaskHandler taskHandler) {
        TaskInfo taskInfo = taskInfoDao.getTaskInfoById(taskId);
        taskInfo.setOperator(staffId);
        taskInfo.setStatus(TaskConstant.Assigned);
        taskInfo.setAssignTime(DateUtils.getCurrentSeconds());
        taskInfo.setUpdatedAt(DateUtils.getCurrentSeconds());
        taskInfo.setContainerId(containerId);
        taskInfoDao.update(taskInfo);
        taskHandler.assignConcrete(taskId, staffId, containerId);
    }

    @Transactional(readOnly = false)
    public void done(Long taskId, TaskHandler taskHandler) {
        TaskInfo taskInfo = taskInfoDao.getTaskInfoById(taskId);
        taskInfo.setStatus(TaskConstant.Done);
        taskInfo.setFinishTime(DateUtils.getCurrentSeconds());
        taskInfo.setUpdatedAt(DateUtils.getCurrentSeconds());
        taskInfoDao.update(taskInfo);
        taskHandler.doneConcrete(taskId);
    }

    @Transactional(readOnly = false)
    public void batchDone(List<Long> taskList ,TaskHandler taskHandler) {
        for(Long taskId:taskList) {
            TaskInfo taskInfo = taskInfoDao.getTaskInfoById(taskId);
            taskInfo.setStatus(TaskConstant.Done);
            taskInfo.setFinishTime(DateUtils.getCurrentSeconds());
            taskInfo.setUpdatedAt(DateUtils.getCurrentSeconds());
            taskInfoDao.update(taskInfo);
            taskHandler.doneConcrete(taskId);
        }
    }

    @Transactional(readOnly = false)
    public void done(Long taskId, Long locationId, TaskHandler taskHandler) {
        TaskInfo taskInfo = taskInfoDao.getTaskInfoById(taskId);
        taskInfo.setStatus(TaskConstant.Done);
        taskInfo.setFinishTime(DateUtils.getCurrentSeconds());
        taskInfo.setUpdatedAt(DateUtils.getCurrentSeconds());
        taskInfoDao.update(taskInfo);
        taskHandler.doneConcrete(taskId, locationId);
    }

    @Transactional(readOnly = false)
    public void done(Long taskId, Long locationId, Long staffId, TaskHandler taskHandler) {
        TaskInfo taskInfo = taskInfoDao.getTaskInfoById(taskId);
        taskInfo.setStatus(TaskConstant.Done);
        taskInfo.setFinishTime(DateUtils.getCurrentSeconds());
        taskInfo.setUpdatedAt(DateUtils.getCurrentSeconds());
        taskInfoDao.update(taskInfo);
        taskHandler.doneConcrete(taskId, locationId, staffId);
    }

    @Transactional(readOnly = false)
    public void cancel(Long taskId, TaskHandler taskHandler) {
        TaskInfo taskInfo = taskInfoDao.getTaskInfoById(taskId);
        taskInfo.setStatus(TaskConstant.Cancel);
        taskInfo.setCancelTime(DateUtils.getCurrentSeconds());
        taskInfo.setUpdatedAt(DateUtils.getCurrentSeconds());
        taskInfoDao.update(taskInfo);
        taskHandler.cancelConcrete(taskId);
    }

    @Transactional(readOnly = false)
    public void batchCancel(List<Long> taskList,TaskHandler taskHandler) {
        for(Long taskId:taskList) {
            TaskInfo taskInfo = taskInfoDao.getTaskInfoById(taskId);
            taskInfo.setStatus(TaskConstant.Cancel);
            taskInfo.setCancelTime(DateUtils.getCurrentSeconds());
            taskInfo.setUpdatedAt(DateUtils.getCurrentSeconds());
            taskInfoDao.update(taskInfo);
            taskHandler.cancelConcrete(taskId);
        }
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

    public boolean checkTaskByToLocation(Long toLocationId, Long taskType) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("toLocationId", toLocationId);
        params.put("taskType", taskType);
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

<<<<<<< HEAD
=======
    /**
     * 根据locationId获取指定类型的未完成任务
     * @param locationId
     * @param type
     * @return
     */
    public List<TaskInfo> getIncompleteTaskByLocation (Long locationId, Long type) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("locaitonId", locationId);
        params.put("type", type);
        List<TaskInfo> taskInfos = taskInfoDao.getTaskInfoList(params);
        List<TaskInfo> retTaskInfos = new ArrayList<TaskInfo>();
        for (TaskInfo taskInfo : taskInfos) {
            if (!taskInfo.getStatus().equals(TaskConstant.Done) && !taskInfo.getStatus().equals(TaskConstant.Cancel)) {
                retTaskInfos.add(taskInfo);
            }
        }
        return retTaskInfos;
    }
>>>>>>> 3ae5cf4ce726ac6292c2fe6da1fa30ff2194c9b7
}
