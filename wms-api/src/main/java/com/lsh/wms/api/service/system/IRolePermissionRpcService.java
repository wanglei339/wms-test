package com.lsh.wms.api.service.system;

import com.lsh.wms.model.system.RolePermission;

import java.util.List;
import java.util.Map;

/**
 * Created by lixin-mac on 16/8/17.
 */
public interface IRolePermissionRpcService {
    List<RolePermission> getRolePermissionList(Map<String, Object> mapQuery);
    Integer getRolePermissionCount(Map<String, Object> mapQuery);
    void insert(RolePermission rolePermission);
    void update(RolePermission rolePermission);
    RolePermission getRolePermissionByName(String role);
}
