package com.lsh.wms.core.dao.system;


import com.lsh.wms.model.system.SysRole;
import com.lsh.wms.core.dao.MyBatisRepository;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface SysRoleDao {

    void insert(SysRole sysRole);

    void update(SysRole sysRole);

    SysRole getSysRoleById(Integer id);

    Integer countSysRole(Map<String, Object> params);

    List<SysRole> getSysRoleList(Map<String, Object> params);

}