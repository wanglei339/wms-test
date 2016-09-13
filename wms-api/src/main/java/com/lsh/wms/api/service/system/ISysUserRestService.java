package com.lsh.wms.api.service.system;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.model.system.SysUser;

import java.util.Map;

/**
 * Created by wulin on 16/7/30.
 */
public interface ISysUserRestService {

    public String getSysUserList(Map<String, Object> params)throws BizCheckedException;

    public String getSysUserListCount(Map<String, Object> params)throws BizCheckedException;

    public String addSysUser(SysUser sysUser)throws BizCheckedException;

    public String updateSysUser(SysUser sysUser)throws BizCheckedException;

    public String checkLogin (Map<String, Object> params)throws BizCheckedException;

    public String getSysUserByUsername(Map<String, Object> params)throws BizCheckedException;

    String getSysUserById(Long iUid);
}
