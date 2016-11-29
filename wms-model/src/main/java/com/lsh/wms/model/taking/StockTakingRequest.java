package com.lsh.wms.model.taking;

import com.lsh.base.common.utils.DateUtils;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import java.io.Serializable;

/**
 * Created by wuhao on 16/7/26.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StockTakingRequest implements Serializable {
    /** 库区Id*/
    private Long storageId = 0L;
    /** 库区Id*/
    private Long areaId = 0L;
    /** 商品Id */
    private Long itemId = 0L;
    /** 库位信息 */
    private String locationList = "";
    /** 供应商id */
    private Long supplierId = 0L;
    /** 盘点类型 */
    private Long viewType = 1L;
    /** 盘点性质 */
    private Long planType = 1L;
    /** 要求结束时间 */
    private Long dueTime = DateUtils.getCurrentSeconds();
    /** 盘点计划Id*/
    private Long takingId = 0L;
    /** 盘点任务发起人*/
    private Long planner  = 0L;

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getLocationList() {
        return locationList;
    }

    public void setLocationList(String locationList) {
        this.locationList = locationList;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public Long getViewType() {
        return viewType;
    }

    public void setViewType(Long viewType) {
        this.viewType = viewType;
    }

    public Long getDueTime() {
        return dueTime;
    }

    public void setDueTime(Long dueTime) {
        this.dueTime = dueTime;
    }

    public Long getPlanType() {
        return planType;
    }

    public void setPlanType(Long planType) {
        this.planType = planType;
    }

    public Long getTakingId() {
        return takingId;
    }

    public void setTakingId(Long takingId) {
        this.takingId = takingId;
    }

    public StockTakingRequest() {
    }

    public Long getPlanner() {
        return planner;
    }

    public void setPlanner(Long planner) {
        this.planner = planner;
    }

    public Long getStorageId() {
        return storageId;
    }

    public void setStorageId(Long storageId) {
        this.storageId = storageId;
    }

    @Override
    public String toString() {
        return "StockTakingRequest{" +
                "storageId=" + storageId +
                ", areaId=" + areaId +
                ", itemId=" + itemId +
                ", locationList='" + locationList + '\'' +
                ", supplierId=" + supplierId +
                ", viewType=" + viewType +
                ", planType=" + planType +
                ", dueTime=" + dueTime +
                ", takingId=" + takingId +
                ", planner=" + planner +
                '}';
    }
}
