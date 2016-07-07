package com.lsh.wms.model.system;

import java.util.Date;
import java.util.List;

public class SysUser {

    /**
     * 主键
     */
    private Integer id;
    /**
     * 登录名
     */
    private String loginName;
    /**
     * 密码
     */
    private String pazzword;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 状态
     */
    private Integer isEffective;
    /**
     * 创建时间
     */
    private Date createdTime;
    /**
     * 更新时间
     */
    private Date updatedTime;

    /**
     * 所属角色
     */
    private List<SysUserRoleRelation> sysUserRoleRelationList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPazzword() {
        return pazzword;
    }

    public void setPazzword(String pazzword) {
        this.pazzword = pazzword;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public List<SysUserRoleRelation> getSysUserRoleRelationList() {
        return sysUserRoleRelationList;
    }

    public void setSysUserRoleRelationList(List<SysUserRoleRelation> sysUserRoleRelationList) {
        this.sysUserRoleRelationList = sysUserRoleRelationList;
    }

}
