package com.lsh.wms.model.up;

import java.util.Date;
import java.util.List;

public class UpApp {

    /**  */
    private Integer id;
    /**  */
    private String appCode;
    /**  */
    private String appName;
    /**  */
    private Integer status;
    /**  */
    private Date createdTime;
    /**  */
    private Date updatedTime;

    /**
     * 在产品包中，应用所拥有的系统
     */
    private List<UpOpsystem> osList;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAppCode() {
        return this.appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getAppName() {
        return this.appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
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

    public List<UpOpsystem> getOsList() {
        return osList;
    }

    public void setOsList(List<UpOpsystem> osList) {
        this.osList = osList;
    }

    @Override
    public String toString() {
        return "UpApp{" +
                "id=" + id +
                ", appCode='" + appCode + '\'' +
                ", appName='" + appName + '\'' +
                ", status=" + status +
                ", createdTime=" + createdTime +
                ", updatedTime=" + updatedTime +
                ", osList=" + osList +
                '}';
    }
}
