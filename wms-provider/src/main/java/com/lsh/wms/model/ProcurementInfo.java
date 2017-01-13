package com.lsh.wms.model;

/**
 * Created by wuhao on 17/1/11.
 */
public class ProcurementInfo {

    private Long locationType;

    private boolean canMax;

    private int taskType;

    public Long getLocationType() {
        return locationType;
    }

    public void setLocationType(Long locationType) {
        this.locationType = locationType;
    }

    public int getTaskType() {
        return taskType;
    }

    public void setTaskType(int taskType) {
        this.taskType = taskType;
    }

    public boolean isCanMax() {
        return canMax;
    }

    public void setCanMax(boolean canMax) {
        this.canMax = canMax;
    }

    @Override
    public String toString() {
        return "ProcurementInfo{" +
                "locationType=" + locationType +
                ", canMax=" + canMax +
                ", taskType=" + taskType +
                '}';
    }
}
