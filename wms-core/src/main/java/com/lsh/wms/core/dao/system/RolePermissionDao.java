package com.lsh.wms.core.dao.system;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.system.RolePermission;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface RolePermissionDao {

	void insert(RolePermission rolePermission);
	
	void update(RolePermission rolePermission);
	
	RolePermission getRolePermissionById(Long id);

    Integer countRolePermission(Map<String, Object> params);

    List<RolePermission> getRolePermissionList(Map<String, Object> params);
	
}