package com.lsh.wms.model.up;

import java.util.Date;

public class UpVersion {

    /**  */
    private Long id;
    /**  */
    private Long pkgId;
    /**  */
    private String verStr;
    /**  */
    private Integer verInt;
    /**  */
    private String verName;
    /**  */
    private String verTitle;
    /**  */
    private String verTitleForce;
    /**  */
    private String verDesc;
    /**  */
    private String verDescForce;
    /**  */
    private Integer upType;
    /**  */
    private Integer silentDownload;
    /**  */
    private Integer silentInstall;
    /**  */
    private Integer promptUp;
    /**  */
    private Integer promptAlways;
    /**  */
    private Integer promptInterval;
    /**  */
    private String fileMd5;
    /**  */
    private String fileUrl;
    /**  */
    private String filePath;
    /**  */
    private Integer fileStatus;
    /**  */
    private Integer status;
    /**  */
    private Date createdTime;
    /**  */
    private Date updatedTime;

    /**
     * 显示属性
     */
    private Integer appId;
    private Integer osId;
    private Integer chnId;
    private Integer modId;
    private String appKey;
    private String pkgName;
    private String appName;
    private String osName;
    private String chnName;
    private String modName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPkgId() {
        return pkgId;
    }

    public void setPkgId(Long pkgId) {
        this.pkgId = pkgId;
    }

    public String getVerStr() {
        return verStr;
    }

    public void setVerStr(String verStr) {
        this.verStr = verStr;
    }

    public Integer getVerInt() {
        return verInt;
    }

    public void setVerInt(Integer verInt) {
        this.verInt = verInt;
    }

    public String getVerName() {
        return verName;
    }

    public void setVerName(String verName) {
        this.verName = verName;
    }

    public String getVerTitle() {
        return verTitle;
    }

    public void setVerTitle(String verTitle) {
        this.verTitle = verTitle;
    }

    public String getVerTitleForce() {
        return verTitleForce;
    }

    public void setVerTitleForce(String verTitleForce) {
        this.verTitleForce = verTitleForce;
    }

    public String getVerDesc() {
        return verDesc;
    }

    public void setVerDesc(String verDesc) {
        this.verDesc = verDesc;
    }

    public String getVerDescForce() {
        return verDescForce;
    }

    public void setVerDescForce(String verDescForce) {
        this.verDescForce = verDescForce;
    }

    public Integer getUpType() {
        return upType;
    }

    public void setUpType(Integer upType) {
        this.upType = upType;
    }

    public Integer getSilentDownload() {
        return silentDownload;
    }

    public void setSilentDownload(Integer silentDownload) {
        this.silentDownload = silentDownload;
    }

    public Integer getSilentInstall() {
        return silentInstall;
    }

    public void setSilentInstall(Integer silentInstall) {
        this.silentInstall = silentInstall;
    }

    public Integer getPromptUp() {
        return promptUp;
    }

    public void setPromptUp(Integer promptUp) {
        this.promptUp = promptUp;
    }

    public Integer getPromptAlways() {
        return promptAlways;
    }

    public void setPromptAlways(Integer promptAlways) {
        this.promptAlways = promptAlways;
    }

    public Integer getPromptInterval() {
        return promptInterval;
    }

    public void setPromptInterval(Integer promptInterval) {
        this.promptInterval = promptInterval;
    }

    public String getFileMd5() {
        return fileMd5;
    }

    public void setFileMd5(String fileMd5) {
        this.fileMd5 = fileMd5;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Integer getFileStatus() {
        return fileStatus;
    }

    public void setFileStatus(Integer fileStatus) {
        this.fileStatus = fileStatus;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public Integer getOsId() {
        return osId;
    }

    public void setOsId(Integer osId) {
        this.osId = osId;
    }

    public Integer getChnId() {
        return chnId;
    }

    public void setChnId(Integer chnId) {
        this.chnId = chnId;
    }

    public Integer getModId() {
        return modId;
    }

    public void setModId(Integer modId) {
        this.modId = modId;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
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

    public String getChnName() {
        return chnName;
    }

    public void setChnName(String chnName) {
        this.chnName = chnName;
    }

    public String getModName() {
        return modName;
    }

    public void setModName(String modName) {
        this.modName = modName;
    }

}
