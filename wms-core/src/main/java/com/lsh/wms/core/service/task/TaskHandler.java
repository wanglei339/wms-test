package com.lsh.wms.core.service.task;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.model.stock.StockMove;
import com.lsh.wms.model.taking.StockTakingHead;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by mali on 16/7/23.
 */
public interface TaskHandler {
    void create(Long taskId) throws BizCheckedException;
    void createTask(StockTakingHead head,TaskEntry entry);
    void create(TaskEntry taskEntry) throws BizCheckedException;
    void createConcrete(TaskEntry taskEntry) throws BizCheckedException;
    void batchCreate(List<TaskEntry> taskEntries) throws BizCheckedException;
    void batchCreate(StockTakingHead head,List<TaskEntry> taskEntries) throws BizCheckedException;
    void batchAssign(List<Long> tasks,Long staffId) throws BizCheckedException;
    void batchCancel(List<Long> tasks) throws BizCheckedException;
    TaskEntry getTask(Long taskId);
    List<TaskEntry> getTaskList(Map<String, Object> condition);
    int getTaskCount(Map<String, Object> condition);
    List<TaskEntry> getTaskHeadList(Map<String, Object> condition);
    void assign(Long taskId, Long staffId) throws BizCheckedException;
    void assign(Long taskId, Long staffId, Long containerId) throws BizCheckedException;
    void assignMul(List<Map<String, Long>> params) throws BizCheckedException;
    void assignConcrete(Long taskId, Long staffId) throws BizCheckedException;
    void assignConcrete(Long taskId, Long staffId, Long containerId) throws BizCheckedException;
    void allocate(Long taskId);
    void release(Long taskId);
    void done(Long taskId);
    void done(TaskEntry taskEntry);
    void done(Long taskId, Long locationId) throws BizCheckedException;
    void done(Long taskId, Long locationId, Long staffId) throws BizCheckedException;
    void done(Long taskId, List<StockMove> moveList) throws BizCheckedException;
    void cancel(Long taskId);
    void update(TaskEntry taskEntry);

    void doneConcrete(Long taskId);
    void doneConcrete(Long taskId, Long locationId);
    void doneConcrete(Long taskId, Long locationId, Long staffId);
    void doneConcrete(Long taskId, List<StockMove> moveList);
    void cancelConcrete(Long taskId);
    TaskEntry getOldTask(Long taskId);
    void allocateConcrete(Long taskId);
    void updteConcrete(TaskEntry taskEntry);
    void calcPerformance(TaskInfo taskInfo);
    void setPriority(Long taskId, Long newPriority);
}
