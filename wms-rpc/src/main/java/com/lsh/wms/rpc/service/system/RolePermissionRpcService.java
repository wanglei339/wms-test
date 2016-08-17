package com.lsh.wms.rpc.service.system;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.wms.api.service.system.IRolePermissionRpcService;
import com.lsh.wms.core.service.system.RolePermissionService;
import com.lsh.wms.model.system.RolePermission;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lixin-mac on 16/8/17.
 */
@Service(protocol = "dubbo")
public class RolePermissionRpcService implements IRolePermissionRpcService {
    @Autowired
    private RolePermissionService rolePermissionService;

    public List<RolePermission> getRolePermissionList(Map<String, Object> mapQuery) {
        return rolePermissionService.getRolePermissionList(mapQuery);
    }

    public Integer getRolePermissionCount(Map<String, Object> mapQuery) {
        return rolePermissionService.getRolePermissionCount(mapQuery);
    }

    public void insert(RolePermission rolePermission) {
        rolePermissionService.insert(rolePermission);
    }

    public void update(RolePermission rolePermission) {
        rolePermissionService.update(rolePermission);
    }

    public RolePermission getRolePermissionByName(String role) {
        Map<String,Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("role",role);
        List<RolePermission> lists = rolePermissionService.getRolePermissionList(mapQuery);
        RolePermission rolePermission = new RolePermission();
        if(lists.size() == 1){
            rolePermission = lists.get(0);
        }
        return rolePermission;
    }
}
