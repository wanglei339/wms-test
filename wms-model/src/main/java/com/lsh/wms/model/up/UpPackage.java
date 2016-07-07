package com.lsh.wms.model.up;

import java.util.Date;

public class UpPackage {

    /**  */
    private Long id;
    /**  */
    private String appKey;
    /**  */
    private String pkgName;
    /**  */
    private String pkgPath;
    /**  */
    private Integer pkgType;
    /**  */
    private Integer appId;
    /**  */
    private Integer osId;
    /**  */
    private Integer chnId;
    /**  */
    private Integer modId;
    /**  */
    private Integer status;
    /**  */
    private Date createdTime;
    /**  */
    private Date updatedTime;

    private String appName;
    private String osName;
    private String modName;
    private String chnName;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAppKey() {
        return this.appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getPkgName() {
        return this.pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public String getPkgPath() {
        return this.pkgPath;
    }

    public void setPkgPath(String pkgPath) {
        this.pkgPath = pkgPath;
    }

    public Integer getPkgType() {
        return this.pkgType;
    }

    public void setPkgType(Integer pkgType) {
        this.pkgType = pkgType;
    }

    public Integer getAppId() {
        return this.appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public Integer getOsId() {
        return this.osId;
    }

    public void setOsId(Integer osId) {
        this.osId = osId;
    }

    public Integer getChnId() {
        return this.chnId;
    }

    public void setChnId(Integer chnId) {
        this.chnId = chnId;
    }

    public Integer getModId() {
        return this.modId;
    }

    public void setModId(Integer modId) {
        this.modId = modId;
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

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public String getModName() {
        return modName;
    }

    public void setModName(String modName) {
        this.modName = modName;
    }

    public String getChnName() {
        return chnName;
    }

    public void setChnName(String chnName) {
        this.chnName = chnName;
    }
}
