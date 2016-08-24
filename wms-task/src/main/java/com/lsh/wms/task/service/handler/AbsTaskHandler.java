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

import java.math.BigDecimal;
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

    public void create(Long taskId) throws BizCheckedException{
    }

    public void create(TaskEntry taskEntry) throws BizCheckedException{
        // 插入标准任务信息
        TaskInfo taskInfo = taskEntry.getTaskInfo();
        if (taskInfo.getTaskId().equals(0L)) {
            Long taskId = RandomUtils.genId();
            taskInfo.setTaskId(taskId);
        }
        taskEntry.setTaskInfo(taskInfo);
        baseTaskService.create(taskEntry, this);
    }

    public void batchCreate(List<TaskEntry> taskEntries) throws BizCheckedException{
        for(TaskEntry taskEntry:taskEntries){
            Long taskId = RandomUtils.genId();
            TaskInfo taskInfo = taskEntry.getTaskInfo();
            taskInfo.setTaskId(taskId);
            taskEntry.setTaskInfo(taskInfo);
        }
       baseTaskService.batchCreate(taskEntries, this);
    }
    public void batchAssign(List<Long> tasks,Long staffId) throws BizCheckedException {
        baseTaskService.batchAssign(tasks, staffId, this);
    }
    public void batchCancel(List<Long> tasks) throws BizCheckedException {
        baseTaskService.batchCancel(tasks, this);
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
        baseTaskService.assign(taskId, staffId, this);
        // this.assignConcrete(taskId, staffId);
    }

    public void update(TaskEntry taskEntry) throws BizCheckedException {
        baseTaskService.update(taskEntry, this);
        // this.assignConcrete(taskId, staffId);
    }

    public void assignConcrete(Long taskId, Long staffId) throws BizCheckedException {
    }

    public void assignMul(List<Map<String, Long>> params) throws BizCheckedException {
        baseTaskService.assignMul(params, this);
    }

    public void assign(Long taskId, Long staffId, Long containerId) throws BizCheckedException {
        baseTaskService.assign(taskId, staffId, containerId, this);
        // this.assignConcrete(taskId, staffId);
    }

    public void assignConcrete(Long taskId, Long staffId, Long containerId) throws BizCheckedException {
    }


    public void done(Long taskId) {
        baseTaskService.done(taskId, this);
        //this.doneConcrete(taskId);
    }

    public void done(Long taskId, Long locationId) throws BizCheckedException {
        baseTaskService.done(taskId, locationId, this);
    }

    public void done(Long taskId, Long locationId, Long staffId) throws BizCheckedException {
        baseTaskService.done(taskId, locationId, staffId, this);
    }

    public void doneConcrete(Long taskId) {
    }

    public void doneConcrete(Long taskId, Long locationId) throws BizCheckedException{
    }

    public void doneConcrete(Long taskId, Long locationId, Long staffId) throws BizCheckedException{
    }

    public void cancel(Long taskId) {
        baseTaskService.cancel(taskId, this);
        //this.cancelConcrete(taskId);
    }

    public void cancelConcrete(Long taskId) {
    }
    public void updteConcrete(Long taskId) {
    }

    public void allocate(Long taskId) {
        baseTaskService.allocate(taskId, this);
        //this.allocateConcrete(taskId);
    }

    public void allocateConcrete(Long taskId) {
    }

    public void updteConcrete(TaskEntry taskEntry) {

    }

    public void release(Long taskId) {
    }

    public void calcPerformance(TaskInfo taskInfo) {
        taskInfo.setTaskQty(BigDecimal.ONE);
    }

    public void setPriority(Long taskId, Long newPriority) {
        baseTaskService.setPriority(taskId, newPriority);
    }
}
