package com.lsh.wms.api.service.baseinfo;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.wms.model.baseinfo.BaseinfoDepartment;

/**
 * Created by lixin-mac on 16/7/12.
 */
@Service(protocol = "rest")
public interface IDepartmentRestService {

    String getDepartment(long departmentId);
    String insertDepartment(BaseinfoDepartment department);
    String updateDepartment(BaseinfoDepartment department);
}
