package com.lsh.wms.api.service.system;

import com.lsh.wms.model.system.RolePermission;

import java.util.List;
import java.util.Map;

/**
 * Created by lixin-mac on 16/8/17.
 */
public interface IRolePermissionRestService {

    String getRolePermissionList(Map<String, Object> mapQuery);
    String getRolePermissionCount(Map<String, Object> mapQuery);
    String insert(RolePermission rolePermission);
    String update(RolePermission rolePermission);
    String getRolePermissionByName(String role);

    String getRolePermissionById(Long id);
}
