package com.lsh.wms.api.service.staff;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.wms.model.baseinfo.BaseinfoDepartment;

import java.util.List;
import java.util.Map;

/**
 * Created by wulin on 16/7/9.
 */

public interface IStaffRpcService {
    public List<BaseinfoDepartment> getDepartmentList(Map<String, Object> mapQuery);

    public void addDepartment(BaseinfoDepartment department);

    public void updateDepartment(BaseinfoDepartment department);
}