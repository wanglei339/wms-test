package com.lsh.wms.model.up;

import java.util.Date;

public class UpOpsystem {

    /**  */
    private Integer id;
    /**  */
    private String osCode;
    /**  */
    private String osName;
    /**
     * 1-PC，2-移动，3-TV
     */
    private Integer osType;
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

    public String getOsCode() {
        return this.osCode;
    }

    public void setOsCode(String osCode) {
        this.osCode = osCode;
    }

    public String getOsName() {
        return this.osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public Integer getOsType() {
        return this.osType;
    }

    public void setOsType(Integer osType) {
        this.osType = osType;
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
