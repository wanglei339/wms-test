package com.lsh.wms.api.service.staff;

import com.lsh.wms.model.baseinfo.BaseinfoStaffDepartment;

import java.util.List;
import java.util.Map;

/**
 * Created by wulin on 16/7/9.
 */

public interface IStaffRpcService {
    public List<BaseinfoStaffDepartment> getDepartmentList(Map<String, Object> mapQuery);

    public void addDepartment(BaseinfoStaffDepartment department);

    public void updateDepartment(BaseinfoStaffDepartment department);
}
