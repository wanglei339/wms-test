package com.lsh.wms.model.up;

import java.util.Date;

public class UpChannel {

    /**  */
    private Integer id;
    /**  */
    private String chnCode;
    /**  */
    private String chnName;
    /**
     * 1-渠道，2-厂商
     */
    private Integer chnType;
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

    public String getChnCode() {
        return this.chnCode;
    }

    public void setChnCode(String chnCode) {
        this.chnCode = chnCode;
    }

    public String getChnName() {
        return this.chnName;
    }

    public void setChnName(String chnName) {
        this.chnName = chnName;
    }

    public Integer getChnType() {
        return this.chnType;
    }

    public void setChnType(Integer chnType) {
        this.chnType = chnType;
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
