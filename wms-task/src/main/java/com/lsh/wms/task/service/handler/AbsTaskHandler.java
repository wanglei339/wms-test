package com.lsh.wms.task.service.handler;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.core.service.task.TaskHandler;
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


    public void create(TaskEntry taskEntry) throws BizCheckedException{
        // 插入标准任务信息
        Long taskId = RandomUtils.genId();
        TaskInfo taskInfo = taskEntry.getTaskInfo();
        taskInfo.setTaskId(taskId);
        taskEntry.setTaskInfo(taskInfo);
        baseTaskService.create(taskEntry, this);
        // this.createConcrete(taskEntry);
    }

    public void batchCreate(List<TaskEntry> taskEntries) throws BizCheckedException{
        for(TaskEntry entry : taskEntries){
            this.create(entry);
        }
    }
    public void batchAssign(List<Long> tasks,Long staffId) throws BizCheckedException {
        for( Long taskId:tasks){
            baseTaskService.assign(taskId, staffId, this);
        }
    }
    public void batchCancel(List<Long> tasks) throws BizCheckedException {
        for( Long taskId:tasks){
            baseTaskService.cancel(taskId, this);
        }
    }

    public void createConcrete(TaskEntry taskEntry) throws BizCheckedException {
        // throw new BizCheckedException("1234567890");
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


    public void assign(Long taskId, Long staffId) throws BizCheckedException {
        baseTaskService.assign(taskId, staffId,this);
        // this.assignConcrete(taskId, staffId);
    }

    public void assignConcrete(Long taskId, Long staffId) throws BizCheckedException {
    }


    public void done(Long taskId) {
        baseTaskService.done(taskId, this);
        //this.doneConcrete(taskId);
    }

    public void done(Long taskId, Long locationId) throws BizCheckedException {
        baseTaskService.done(taskId, this, locationId);
        //this.doneConcrete(taskId, locationId);
    }

    public void doneConcrete(Long taskId) {
    }

    public void doneConcrete(Long taskId, Long locationId) throws BizCheckedException{
    }

    public void cancel(Long taskId) {
        baseTaskService.cancel(taskId, this);
        //this.cancelConcrete(taskId);
    }

    public void cancelConcrete(Long taskId) {
    }

    public void allocate(Long taskId) {
        baseTaskService.allocate(taskId, this);
        //this.allocateConcrete(taskId);
    }

    public void allocateConcrete(Long taskId) {
    }

    public void release(Long taskId) {
    }
}
