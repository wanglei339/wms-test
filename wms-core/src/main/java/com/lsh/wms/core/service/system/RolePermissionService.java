package com.lsh.wms.core.service.system;

import com.lsh.wms.core.dao.system.RolePermissionDao;
import com.lsh.wms.model.system.RolePermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by lixin-mac on 16/8/17.
 */
@Component
@Transactional(readOnly = true)
public class RolePermissionService {

    @Autowired
    private RolePermissionDao rolePermissionDao;

    public List<RolePermission> getRolePermissionList(Map<String, Object> mapQuery){
        return rolePermissionDao.getRolePermissionList(mapQuery);
    }

    public Integer getRolePermissionCount(Map<String, Object> mapQuery){
        return rolePermissionDao.countRolePermission(mapQuery);
    }

    @Transactional(readOnly = false)
    public void insert(RolePermission rolePermission){
        rolePermissionDao.insert(rolePermission);
    }

    @Transactional(readOnly = false)
    public void update(RolePermission rolePermission){
        rolePermissionDao.update(rolePermission);
    }
}
