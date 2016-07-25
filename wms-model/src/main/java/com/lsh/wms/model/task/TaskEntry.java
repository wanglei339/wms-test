package com.lsh.wms.model.task;

import com.lsh.wms.model.stock.StockMove;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mali on 16/7/23.
 */
public class TaskEntry implements Serializable {

    private TaskInfo taskInfo;
    private Object taskHead;
    private List<Object> taskDetailList;
    private List<StockMove> stockMoveList;


    public TaskInfo getTaskInfo() {
        return taskInfo;
    }

    public void setTaskInfo(TaskInfo taskInfo) {
        this.taskInfo = taskInfo;
    }

    public Object getTaskHead() {
        return taskHead;
    }

    public void setTaskHead(Object taskHead) {
        this.taskHead = taskHead;
    }

    public List<Object> getTaskDetailList() {
        return taskDetailList;
    }

    public void setTaskDetailList(List<Object> taskDetailList) {
        this.taskDetailList = taskDetailList;
    }

    public List<StockMove> getStockMoveList() {
        return stockMoveList;
    }

    public void setStockMoveList(List<StockMove> stockMoveList) {
        this.stockMoveList = stockMoveList;
    }

}
