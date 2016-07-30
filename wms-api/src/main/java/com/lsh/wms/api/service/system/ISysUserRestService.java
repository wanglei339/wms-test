package com.lsh.wms.api.service.system;

import com.lsh.wms.model.system.SysUser;

import java.util.Map;

/**
 * Created by wulin on 16/7/30.
 */
public interface ISysUserRestService {

    public String getSysUserList(Map<String, Object> params);

    public String getSysUserListCount(Map<String, Object> params);

    public String addSysUser(SysUser sysUser);

    public String updateSysUser(SysUser sysUser);

    public String checkLogin(Map<String, Object> params);

    public String getSysUserByUsername(Map<String, Object> params);
}
