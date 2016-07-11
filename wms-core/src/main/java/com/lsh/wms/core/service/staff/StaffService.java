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

    public BaseinfoDepartment getDepartment(long iDepartmentId) {
        Map<String, Object> mapQuery = new HashMap();
        mapQuery.put("department_id", iDepartmentId);
        List<BaseinfoDepartment> ret = this.departmentDao.getBaseinfoDepartmentList(mapQuery);
        if (!ret.isEmpty()) {
            return ret.get(0);
        } else {
            return null;
        }
    }

    public List<BaseinfoDepartment> getDepartmentList(Map<String, Object> mapQuery) {
        logger.debug(mapQuery.toString());
        return this.departmentDao.getBaseinfoDepartmentList(mapQuery);
    }

    public Integer countBaseinfoDepartment(Map<String, Object> params) {
        return departmentDao.countBaseinfoDepartment(params);
    }
}