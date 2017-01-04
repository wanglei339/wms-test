package com.lsh.wms.model.taking;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wuhao on 16/7/26.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DetailRequest implements Serializable {
    /** 任务id*/
    private Long taskId;
    /** 物美码*/
    private Long skuCode;
    /** 库位码*/
    private String locationCode;
    /** 箱数*/
    private BigDecimal umoQty;
    /** 剩余ea数*/
    private BigDecimal scatterQty;

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(Long skuCode) {
        this.skuCode = skuCode;
    }

    public BigDecimal getUmoQty() {
        return umoQty;
    }

    public void setUmoQty(BigDecimal umoQty) {
        this.umoQty = umoQty;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public BigDecimal getScatterQty() {
        return scatterQty;
    }

    public void setScatterQty(BigDecimal scatterQty) {
        this.scatterQty = scatterQty;
    }

    @Override
    public String toString() {
        return "DetailRequest{" +
                "taskId=" + taskId +
                ", skuCode=" + skuCode +
                ", locationCode='" + locationCode + '\'' +
                ", umoQty=" + umoQty +
                ", scatterQty=" + scatterQty +
                '}';
    }
}
