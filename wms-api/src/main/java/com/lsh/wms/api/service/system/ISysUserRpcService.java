package com.lsh.wms.api.service.system;


import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.model.system.SysUser;

import java.util.List;
import java.util.Map;

/**
 * Created by wulin on 16/7/30.
 */
public interface ISysUserRpcService {

    public List<SysUser> getSysUserList(Map<String, Object> params);

    public Integer getSysUserListCount(Map<String, Object> params);

    public void addSysUser(SysUser sysUser) throws BizCheckedException;

    public void updateSysUser(SysUser sysUser) throws BizCheckedException;

    public SysUser getSysUserById(Long iUid) throws BizCheckedException;

    Boolean checkLogin(String username, String password) throws BizCheckedException;
}
