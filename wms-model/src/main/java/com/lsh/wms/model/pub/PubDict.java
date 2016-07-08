package com.lsh.wms.model.pub;

import java.util.Date;

public class PubDict {

    /**  */
    private Integer id;
    /**  */
    private Integer dictType;
    /**  */
    private String dictCode;
    /**  */
    private String dictName;
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

    public Integer getDictType() {
        return this.dictType;
    }

    public void setDictType(Integer dictType) {
        this.dictType = dictType;
    }

    public String getDictCode() {
        return this.dictCode;
    }

    public void setDictCode(String dictCode) {
        this.dictCode = dictCode;
    }

    public String getDictName() {
        return this.dictName;
    }

    public void setDictName(String dictName) {
        this.dictName = dictName;
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
