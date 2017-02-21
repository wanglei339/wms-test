package com.lsh.wms.model;

/**
 * Created by wuhao on 17/1/11.
 */
public class ProcurementInfo {

    private Long locationType;

    private boolean canMax;

    private int taskType;

    private Long locationId = 0L;

    private Long itemId = 0L;

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

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public ProcurementInfo(Long locationId, Long itemId) {
        this.locationId = locationId;
        this.itemId = itemId;
    }

    public ProcurementInfo() {
    }

    @Override
    public String toString() {
        return "ProcurementInfo{" +
                "locationType=" + locationType +
                ", canMax=" + canMax +
                ", taskType=" + taskType +
                ", locationId=" + locationId +
                ", itemId=" + itemId +
                '}';
    }
}
