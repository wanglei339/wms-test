package com.lsh.wms.model.up;

import java.util.Date;

public class UpVersionRules {

    /**  */
    private Long id;
    /**  */
    private Long verId;
    /**  */
    private Integer ruleId;
    /**
     * 1-黑名单,2-白名单
     */
    private Integer ruleType;
    /**
     * 1-在范围内,2-不在范围内
     */
    private Integer judgeWay;
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

    public Long getVerId() {
        return this.verId;
    }

    public void setVerId(Long verId) {
        this.verId = verId;
    }

    public Integer getRuleId() {
        return this.ruleId;
    }

    public void setRuleId(Integer ruleId) {
        this.ruleId = ruleId;
    }

    public Integer getRuleType() {
        return this.ruleType;
    }

    public void setRuleType(Integer ruleType) {
        this.ruleType = ruleType;
    }

    public Integer getJudgeWay() {
        return this.judgeWay;
    }

    public void setJudgeWay(Integer judgeWay) {
        this.judgeWay = judgeWay;
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
