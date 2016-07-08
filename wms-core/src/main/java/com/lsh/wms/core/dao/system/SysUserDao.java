package com.lsh.wms.core.dao.system;



import com.lsh.wms.model.system.SysUser;
import com.lsh.wms.core.dao.MyBatisRepository;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface SysUserDao {

	void insert(SysUser sysUser);
	
	void update(SysUser sysUser);

    SysUser getSysUserById(Integer id);
	
    SysUser getSysUser(Map<String, Object> params);

    Integer countSysUser(Map<String, Object> params);

    List<SysUser> getSysUserList(Map<String, Object> params);

}