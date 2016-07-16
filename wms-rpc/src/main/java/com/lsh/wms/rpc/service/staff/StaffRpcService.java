package com.lsh.wms.rpc.service.staff;


import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.wms.api.service.staff.IStaffRpcService;
import com.lsh.wms.model.baseinfo.BaseinfoStaffDepartment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.lsh.wms.core.service.staff.StaffService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wulin on 16/7/9.
 */

@Service(protocol = "dubbo")
public class StaffRpcService implements IStaffRpcService {

    private static final Logger logger = LoggerFactory.getLogger(StaffRpcService.class);

    @Autowired
    private StaffService staffService;

    public List<BaseinfoStaffDepartment> getDepartmentList(Map<String, Object> mapQuery) {
        return staffService.getDepartmentList(mapQuery);
    }

    public void addDepartment(BaseinfoStaffDepartment department) {
        staffService.addDepartment(department);
    }

    public void updateDepartment(BaseinfoStaffDepartment department) {
        staffService.updateDepartment(department);
    }

    public BaseinfoStaffDepartment getDepartmentById(long iDepartmentId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("department_id", iDepartmentId);
        List<BaseinfoStaffDepartment> dList = getDepartmentList(params);
        BaseinfoStaffDepartment department = null;
        if (!dList.isEmpty()) {
            department = dList.get(0);
        }
        return department;
    }
}
