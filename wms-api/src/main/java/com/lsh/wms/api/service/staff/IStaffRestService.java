package com.lsh.wms.api.service.staff;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.model.baseinfo.BaseinfoDepartment;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * Created by wulin on 16/7/9.
 */

public interface IStaffRestService {
    public String getDepartmentList();

    public String addDepartment();

    public String updateDepartment(Map<String, Object> params) throws BizCheckedException;

    public String deleteDepartment(Map<String, Object> params);
}
