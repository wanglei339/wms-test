package com.lsh.wms.rpc.service.staff;


import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.q.Module.Base;
import com.lsh.wms.api.service.staff.IStaffRpcService;
import com.lsh.wms.model.baseinfo.BaseinfoDepartment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.lsh.wms.core.service.staff.StaffService;
import org.springframework.beans.factory.annotation.Autowired;

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

    public List<BaseinfoDepartment> getDepartmentList(Map<String, Object> mapQuery) {
        return this.staffService.getDepartmentList(mapQuery);
    }

    public void addDepartment(BaseinfoDepartment department) {
        staffService.addDepartment(department);
    }

    public void updateDepartment(BaseinfoDepartment department) {
        staffService.updateDepartment(department);
    }
}
