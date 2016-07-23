package com.lsh.wms.model.task;

import com.lsh.wms.model.stock.StockMove;

import java.util.List;

/**
 * Created by mali on 16/7/23.
 */
public class TaskEntry<E, T> {

    private Long taskId;
    private Long taskType;
    private TaskInfo taskInfo;
    private E taskHead;
    private List<T> taskDetailList;
    private List<StockMove> stockMoveList;

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getTaskType() {
        return taskType;
    }

    public void setTaskType(Long taskType) {
        this.taskType = taskType;
    }

    public TaskInfo getTaskInfo() {
        return taskInfo;
    }

    public void setTaskInfo(TaskInfo taskInfo) {
        this.taskInfo = taskInfo;
    }

    public E getTaskHead() {
        return taskHead;
    }

    public void setTaskHead(E taskHead) {
        this.taskHead = taskHead;
    }

    public List<T> getTaskDetailList() {
        return taskDetailList;
    }

    public void setTaskDetailList(List<T> taskDetailList) {
        this.taskDetailList = taskDetailList;
    }

    public List<StockMove> getStockMoveList() {
        return stockMoveList;
    }

    public void setStockMoveList(List<StockMove> stockMoveList) {
        this.stockMoveList = stockMoveList;
    }

}
