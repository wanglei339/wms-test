package com.lsh.wms.model.system;

public class SysRoleFunctionRelation {

    /**  */
    private Integer id;
    /**
     * 角色ID
     */
    private Integer roleId;
    /**
     * 权限ID
     */
    private Integer funcId;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRoleId() {
        return this.roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getFuncId() {
        return this.funcId;
    }

    public void setFuncId(Integer funcId) {
        this.funcId = funcId;
    }


}
