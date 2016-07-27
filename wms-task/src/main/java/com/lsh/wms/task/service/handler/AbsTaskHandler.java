package com.lsh.wms.task.service.handler;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by mali on 16/7/23.
 */
@Component
public class AbsTaskHandler implements TaskHandler {
    @Autowired
    private BaseTaskService baseTaskService;

    @Transactional (readOnly = false)
    public void create(TaskEntry taskEntry) throws BizCheckedException{
        // 插入标准任务信息
        Long taskId = RandomUtils.genId();
        TaskInfo taskInfo = taskEntry.getTaskInfo();
        taskInfo.setTaskId(taskId);
        baseTaskService.create(taskInfo);
        this.createConcrete(taskEntry);
    }

    @Transactional (readOnly = false)
    public void batchCreate(List<TaskEntry> taskEntries) throws BizCheckedException{
        for(TaskEntry entry : taskEntries){
            this.create(entry);
        }
    }

    protected void createConcrete(TaskEntry taskEntry) throws BizCheckedException{
    }


    public TaskEntry getTask(Long taskId) {
        TaskEntry taskEntry = new TaskEntry();
        taskEntry.setTaskInfo(baseTaskService.getTaskInfoById(taskId));
        this.getConcrete(taskEntry);
        return taskEntry;
    }

    public List<TaskEntry> getTaskList(Map<String, Object> condition) {
        List<TaskInfo> taskInfoList = baseTaskService.getTaskInfoList(condition);
        List<TaskEntry> taskEntryList = new ArrayList<TaskEntry>();
        for (TaskInfo taskInfo : taskInfoList) {
            TaskEntry taskEntry = new TaskEntry();
            taskEntry.setTaskInfo(taskInfo);
            this.getConcrete(taskEntry);
            taskEntryList.add(taskEntry);
        }
        return taskEntryList;
    }

    public List<TaskEntry> getTaskHeadList(Map<String, Object> condition) {
        List<TaskInfo> taskInfoList = baseTaskService.getTaskInfoList(condition);
        List<TaskEntry> taskEntryList = new ArrayList<TaskEntry>();
        for (TaskInfo taskInfo : taskInfoList) {
            TaskEntry taskEntry = new TaskEntry();
            taskEntry.setTaskInfo(taskInfo);
            this.getHeadConcrete(taskEntry);
            taskEntryList.add(taskEntry);
        }
        return taskEntryList;
    }


    public int getTaskCount(Map<String, Object> condition) {
        return baseTaskService.getTaskInfoCount(condition);
    }

    protected void getConcrete(TaskEntry taskEntry) {
    }

    protected void getHeadConcrete(TaskEntry taskEntry) {

    }


    public void assign(Long taskId, Long staffId) {
        baseTaskService.assign(taskId, staffId);
        this.assignConcrete(taskId, staffId);
    }

    protected void assignConcrete(Long taskId, Long staffId) {
    }


    public void done(Long taskId) {
        baseTaskService.done(taskId);
        this.doneConcrete(taskId);
    }

    public void done(Long taskId, Long locationId) throws BizCheckedException {
        baseTaskService.done(taskId);
        this.doneConcrete(taskId, locationId);
    }

    protected void doneConcrete(Long taskId) {
    }

    protected void doneConcrete(Long taskId, Long locationId) throws BizCheckedException{
    }

    public void cancel(Long taskId) {
        baseTaskService.cancel(taskId);
        this.cancelConcrete(taskId);
    }

    protected void cancelConcrete(Long taskId) {
    }

    public void allocate(Long taskId) {
        baseTaskService.allocate(taskId);
        this.allocateConcrete(taskId);
    }

    protected void allocateConcrete(Long taskId) {
    }

    public void release(Long taskId) {
    }
}
