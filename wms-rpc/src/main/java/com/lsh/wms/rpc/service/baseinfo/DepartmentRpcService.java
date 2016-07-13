package com.lsh.wms.rpc.service.baseinfo;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.wms.api.service.baseinfo.IDepartmentRpcService;
import com.lsh.wms.core.service.baseinfo.DepartmentService;
import com.lsh.wms.model.baseinfo.BaseinfoDepartment;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by lixin-mac on 16/7/12.
 */

@Service(protocol = "dubbo")
public class DepartmentRpcService implements IDepartmentRpcService{
    @Autowired
    private DepartmentService departmentService;

    public BaseinfoDepartment getDepartment(long departmentId) {
        return departmentService.getDepartment(departmentId);
    }

    public BaseinfoDepartment insertDepartment(BaseinfoDepartment department) {
        return departmentService.isnertDepartment(department);
    }

    public int updateDepartment(BaseinfoDepartment department) {
        return departmentService.updateDepartment(department);
    }
}
