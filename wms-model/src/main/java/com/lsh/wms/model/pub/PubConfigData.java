package com.lsh.wms.model.pub;

import java.util.Date;
import java.util.List;

public class PubConfigData {

    /**  */
    private Integer id;
    /**  */
    private Integer pageId;
    /**  */
    private Integer conType;
    /**  */
    private String conTitle;
    /**  */
    private String conDesc;
    /**  */
    private String conUrl;
    /**  */
    private Long conId;
    /**  */
    private String conP1;
    /**  */
    private String conP2;
    /**  */
    private String conP3;
    /**  */
    private String conP4;
    /**  */
    private String conP5;
    /**  */
    private Integer conOrder;
    /**  */
    private Integer status;
    /**  */
    private Date createdTime;
    /**  */
    private Date updatedTime;

    /**
     * 附加属性-内容类型名称
     */
    private String conTypeName;
    /**
     * 附加属性-文件列表
     */
    private List<PubConfigDataFile> fileList;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPageId() {
        return this.pageId;
    }

    public void setPageId(Integer pageId) {
        this.pageId = pageId;
    }

    public Integer getConType() {
        return this.conType;
    }

    public void setConType(Integer conType) {
        this.conType = conType;
    }

    public String getConTitle() {
        return this.conTitle;
    }

    public void setConTitle(String conTitle) {
        this.conTitle = conTitle;
    }

    public String getConDesc() {
        return this.conDesc;
    }

    public void setConDesc(String conDesc) {
        this.conDesc = conDesc;
    }

    public String getConUrl() {
        return this.conUrl;
    }

    public void setConUrl(String conUrl) {
        this.conUrl = conUrl;
    }

    public Long getConId() {
        return this.conId;
    }

    public void setConId(Long conId) {
        this.conId = conId;
    }

    public String getConP1() {
        return this.conP1;
    }

    public void setConP1(String conP1) {
        this.conP1 = conP1;
    }

    public String getConP2() {
        return this.conP2;
    }

    public void setConP2(String conP2) {
        this.conP2 = conP2;
    }

    public String getConP3() {
        return this.conP3;
    }

    public void setConP3(String conP3) {
        this.conP3 = conP3;
    }

    public String getConP4() {
        return this.conP4;
    }

    public void setConP4(String conP4) {
        this.conP4 = conP4;
    }

    public String getConP5() {
        return this.conP5;
    }

    public void setConP5(String conP5) {
        this.conP5 = conP5;
    }

    public Integer getConOrder() {
        return this.conOrder;
    }

    public void setConOrder(Integer conOrder) {
        this.conOrder = conOrder;
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

    public String getConTypeName() {
        return conTypeName;
    }

    public void setConTypeName(String conTypeName) {
        this.conTypeName = conTypeName;
    }

    public List<PubConfigDataFile> getFileList() {
        return fileList;
    }

    public void setFileList(List<PubConfigDataFile> fileList) {
        this.fileList = fileList;
    }

}
