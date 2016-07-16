package com.lsh.wms.rpc.service.baseinfo;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.wms.api.service.baseinfo.IDepartmentRpcService;
import com.lsh.wms.core.service.baseinfo.DepartmentService;
import com.lsh.wms.model.baseinfo.BaseinfoDepartment;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by lixin-mac on 16/7/12.
 */

@Service(protocol = "dubbo")
public class DepartmentRpcService implements IDepartmentRpcService{

    private static Logger logger = LoggerFactory.getLogger(DepartmentRpcService.class);
    @Autowired
    private DepartmentService departmentService;

    public BaseinfoDepartment getDepartment(long departmentId) {
        return departmentService.getDepartment(departmentId);
    }

    public void insertDepartment(BaseinfoDepartment department) {
        departmentService.isnertDepartment(department);
    }

    public void updateDepartment(BaseinfoDepartment department) {
        departmentService.updateDepartment(department);
    }
}
