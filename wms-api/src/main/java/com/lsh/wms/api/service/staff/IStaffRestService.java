package com.lsh.wms.api.service.staff;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.wms.model.baseinfo.BaseinfoDepartment;

import java.util.List;
import java.util.Map;

/**
 * Created by wulin on 16/7/9.
 */

public interface IStaffRestService {
    public String getDepartmentList();
}
