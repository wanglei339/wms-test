package com.lsh.wms.model.up;

import java.util.Date;

public class UpRule {

    /**  */
    private Integer id;
    /**  */
    private String ruleName;
    /**
     * 1-等于value，2-小于max，3-小于等于max，4-大于min，5-大于等于min，6-大于min小于max，7-大于min小于等于max，8-大于等于min小于max，9-大于等于min小于等于max
     */
    private Integer compareType;
    /**  */
    private String compareCol;
    /**
     * 影响结果：1-是否可升级，2-是否强制升级
     */
    private Integer returnType;
    /**  */
    private Integer status;
    /**  */
    private Date createdTime;
    /**  */
    private Date updatedTime;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRuleName() {
        return this.ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public Integer getCompareType() {
        return this.compareType;
    }

    public void setCompareType(Integer compareType) {
        this.compareType = compareType;
    }

    public String getCompareCol() {
        return this.compareCol;
    }

    public void setCompareCol(String compareCol) {
        this.compareCol = compareCol;
    }

    public Integer getReturnType() {
        return this.returnType;
    }

    public void setReturnType(Integer returnType) {
        this.returnType = returnType;
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
