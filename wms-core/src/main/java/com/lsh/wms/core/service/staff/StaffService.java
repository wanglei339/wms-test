package com.lsh.wms.core.service.staff;

import com.lsh.wms.core.dao.baseinfo.BaseinfoDepartmentDao;
import com.lsh.wms.model.baseinfo.BaseinfoDepartment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
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
    private BaseinfoDepartmentDao departmentDao;

    public List<BaseinfoDepartment> getDepartmentList(Map<String, Object> mapQuery) {
        return departmentDao.getBaseinfoDepartmentList(mapQuery);
    }

    public Integer countBaseinfoDepartment(Map<String, Object> params) {
        return departmentDao.countBaseinfoDepartment(params);
    }

    public void addDepartment(BaseinfoDepartment department) {
        departmentDao.insert(department);
    }

    public void updateDepartment(BaseinfoDepartment department) {
        departmentDao.update(department);
    }
}