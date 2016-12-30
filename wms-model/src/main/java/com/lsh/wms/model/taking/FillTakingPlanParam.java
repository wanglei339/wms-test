package com.lsh.wms.model.taking;

/**
 * Created by xiaoma on 16/3/18.
 */
public class FillTakingPlanParam {
    private Long taskId;
    private Long operator;

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getOperator() {
        return operator;
    }

    public void setOperator(Long operator) {
        this.operator = operator;
    }

    public FillTakingPlanParam(Long taskId, Long operator) {
        this.taskId = taskId;
        this.operator = operator;
    }

    @Override
    public String toString() {
        return "FillTakingPlanParam{" +
                "taskId=" + taskId +
                ", operator=" + operator +
                '}';
    }
}
