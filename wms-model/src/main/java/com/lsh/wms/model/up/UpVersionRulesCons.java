package com.lsh.wms.model.up;

import java.util.Date;

public class UpVersionRulesCons {

    /**  */
    private Long id;
    /**  */
    private Long verRuleId;
    /**  */
    private String eqValue;
    /**  */
    private String eqValueName;
    /**  */
    private Long minValue;
    /**  */
    private String minValueName;
    /**  */
    private Long maxValue;
    /**  */
    private String maxValueName;
    /**  */
    private String memo;
    /**  */
    private Integer status;
    /**  */
    private Date createdTime;
    /**  */
    private Date updatedTime;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVerRuleId() {
        return this.verRuleId;
    }

    public void setVerRuleId(Long verRuleId) {
        this.verRuleId = verRuleId;
    }

    public String getEqValue() {
        return this.eqValue;
    }

    public void setEqValue(String eqValue) {
        this.eqValue = eqValue;
    }

    public String getEqValueName() {
        return this.eqValueName;
    }

    public void setEqValueName(String eqValueName) {
        this.eqValueName = eqValueName;
    }

    public Long getMinValue() {
        return this.minValue;
    }

    public void setMinValue(Long minValue) {
        this.minValue = minValue;
    }

    public String getMinValueName() {
        return this.minValueName;
    }

    public void setMinValueName(String minValueName) {
        this.minValueName = minValueName;
    }

    public Long getMaxValue() {
        return this.maxValue;
    }

    public void setMaxValue(Long maxValue) {
        this.maxValue = maxValue;
    }

    public String getMaxValueName() {
        return this.maxValueName;
    }

    public void setMaxValueName(String maxValueName) {
        this.maxValueName = maxValueName;
    }

    public String getMemo() {
        return this.memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreatedTime() {
        return this.createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdatedTime() {
        return this.updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }


}
