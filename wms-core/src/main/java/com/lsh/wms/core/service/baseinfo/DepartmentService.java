package com.lsh.wms.core.service.baseinfo;

import com.lsh.wms.core.dao.baseinfo.BaseinfoDepartmentDao;
import com.lsh.wms.model.baseinfo.BaseinfoDepartment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by lixin-mac on 16/7/12.
 */

@Component
@Transactional(readOnly = true)
public class DepartmentService {
    private static final Logger logger = LoggerFactory.getLogger(DepartmentService.class);
    private static final ConcurrentMap<Long, BaseinfoDepartment> m_DepartmentCache = new ConcurrentHashMap<Long, BaseinfoDepartment>();


    @Autowired
    private BaseinfoDepartmentDao departmentDao;

    public BaseinfoDepartment getDepartment(long iDepartmentId){
        BaseinfoDepartment department = m_DepartmentCache.get(iDepartmentId);
        if(department == null){
            Map<String,Object> mapQuery = new HashMap<String, Object>();
            mapQuery.put("departmentId",iDepartmentId);
            List<BaseinfoDepartment> list = departmentDao.getBaseinfoDepartmentList(mapQuery);
            if(list.size() == 1){
                department = list.get(0);
                m_DepartmentCache.put(iDepartmentId,department);
            }else{
                return null;
            }
        }

        return department;
    }

    @Transactional(readOnly = false)
    public BaseinfoDepartment isnertDepartment(BaseinfoDepartment department){
        if(department.getDepartmentId() == 0){
            //gen departmentId
            int iDepartmentId = 0;
            int count = departmentDao.countBaseinfoDepartment(null);
            if(count == 0){
                iDepartmentId = 1;
            }else{
                iDepartmentId = count + 1;
            }
            department.setDepartmentId((long)iDepartmentId);
        }
        //增加新增时间
        long createdAt = new Date().getTime()/1000;
        department.setCreatedAt(createdAt);

        departmentDao.insert(department);
        //增加缓存
        m_DepartmentCache.put(department.getDepartmentId(),department);

        return department;
    }

    @Transactional(readOnly = false)
    public int updateDepartment(BaseinfoDepartment department){
        if(this.getDepartment(department.getDepartmentId()) == null){
            return -1;
        }
        //增加更新时间
        long updatedAt = new Date().getTime()/1000;
        department.setUpdatedAt(updatedAt);
        departmentDao.update(department);

        //更新缓存
        m_DepartmentCache.put(department.getDepartmentId(),department);
        return 0;

    }



}
