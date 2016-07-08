package com.lsh.wms.model.system;

import java.util.Date;

public class SysFunction {

    /**
     * 主键
     */
    private Integer id;
    /**
     * 权限名
     */
    private String funcName;
    /**
     * 父功能ID
     */
    private Integer parentFuncId;
    /**
     * 是否叶子节点
     */
    private Integer isLeaf;
    /**
     * 功能链接
     */
    private String actionUrl;
    /**
     * 功能图链接
     */
    private String iconUrl;
    /**
     * 功能排序
     */
    private Integer funcOrder;
    /**
     * 是否有效
     */
    private Integer isEffective;
    /**
     * 创建时间
     */
    private Date createdTime;
    /**
     * 最后更新时间
     */
    private Date updatedTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFuncName() {
        return funcName;
    }

    public void setFuncName(String funcName) {
        this.funcName = funcName;
    }

    public Integer getParentFuncId() {
        return parentFuncId;
    }

    public void setParentFuncId(Integer parentFuncId) {
        this.parentFuncId = parentFuncId;
    }

    public Integer getIsLeaf() {
        return isLeaf;
    }

    public void setIsLeaf(Integer isLeaf) {
        this.isLeaf = isLeaf;
    }

    public String getActionUrl() {
        return actionUrl;
    }

    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public Integer getFuncOrder() {
        return funcOrder;
    }

    public void setFuncOrder(Integer funcOrder) {
        this.funcOrder = funcOrder;
    }

    public Integer getIsEffective() {
        return isEffective;
    }

    public void setIsEffective(Integer isEffective) {
        this.isEffective = isEffective;
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

}
