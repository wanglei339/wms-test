package com.lsh.wms.core.service.staff;

import com.lsh.wms.core.dao.baseinfo.BaseinfoStaffDepartmentDao;
import com.lsh.wms.model.baseinfo.BaseinfoStaffDepartment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;

/**
 * Created by wulin on 16/7/9.
 */

@Component
@Transactional(readOnly = true)
public class StaffService {
    private static final Logger logger = LoggerFactory.getLogger(StaffService.class);

    @Autowired
    private BaseinfoStaffDepartmentDao departmentDao;

    public List<BaseinfoStaffDepartment> getDepartmentList(Map<String, Object> mapQuery) {
        return departmentDao.getBaseinfoStaffDepartmentList(mapQuery);
    }

    public Integer countBaseinfoStaffDepartment(Map<String, Object> params) {
        return departmentDao.countBaseinfoStaffDepartment(params);
    }

    @Transactional(readOnly = false)
    public void addDepartment(BaseinfoStaffDepartment department) {
        long now = (System.currentTimeMillis() / 1000);
        department.setCreatedAt(now);
        department.setUpdatedAt(now);
        departmentDao.insert(department);
    }

    @Transactional(readOnly = false)
    public void updateDepartment(BaseinfoStaffDepartment department) {
        long now = (System.currentTimeMillis() / 1000);
        department.setUpdatedAt(now);
        departmentDao.update(department);
    }
}