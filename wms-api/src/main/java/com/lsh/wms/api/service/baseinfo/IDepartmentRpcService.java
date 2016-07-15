package com.lsh.wms.api.service.baseinfo;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.wms.model.baseinfo.BaseinfoDepartment;

/**
 * Created by lixin-mac on 16/7/12.
 */

@Service(protocol = "dubbo")
public interface IDepartmentRpcService {
    BaseinfoDepartment getDepartment(long departmentId);
    void insertDepartment(BaseinfoDepartment department);
    void updateDepartment(BaseinfoDepartment department);

}
