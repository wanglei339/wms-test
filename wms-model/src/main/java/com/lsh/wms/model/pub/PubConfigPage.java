package com.lsh.wms.model.pub;

import java.util.Date;

public class PubConfigPage {

    /**  */
    private Integer id;
    /**  */
    private Integer pageType;
    /**  */
    private String pageCode;
    /**  */
    private String pageName;
    /**  */
    private String pageDesc;
    /**  */
    private String conTypeCode;
    /**  */
    private Integer cacheType;
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

    public Integer getPageType() {
        return this.pageType;
    }

    public void setPageType(Integer pageType) {
        this.pageType = pageType;
    }

    public String getPageCode() {
        return this.pageCode;
    }

    public void setPageCode(String pageCode) {
        this.pageCode = pageCode;
    }

    public String getPageName() {
        return this.pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public String getPageDesc() {
        return this.pageDesc;
    }

    public void setPageDesc(String pageDesc) {
        this.pageDesc = pageDesc;
    }

    public String getConTypeCode() {
        return this.conTypeCode;
    }

    public void setConTypeCode(String conTypeCode) {
        this.conTypeCode = conTypeCode;
    }

    public Integer getCacheType() {
        return this.cacheType;
    }

    public void setCacheType(Integer cacheType) {
        this.cacheType = cacheType;
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
