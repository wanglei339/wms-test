package com.lsh.wms.model.up;

import java.util.Date;

public class UpModel {

    /**  */
    private Integer id;
    /**  */
    private String modCode;
    /**  */
    private String modName;
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

    public String getModCode() {
        return this.modCode;
    }

    public void setModCode(String modCode) {
        this.modCode = modCode;
    }

    public String getModName() {
        return this.modName;
    }

    public void setModName(String modName) {
        this.modName = modName;
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
