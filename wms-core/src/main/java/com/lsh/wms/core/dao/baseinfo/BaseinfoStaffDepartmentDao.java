package com.lsh.wms.core.dao.baseinfo;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.baseinfo.BaseinfoStaffDepartment;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface BaseinfoStaffDepartmentDao {

	void insert(BaseinfoStaffDepartment baseinfoStaffDepartment);
	
	void update(BaseinfoStaffDepartment baseinfoStaffDepartment);
	
	BaseinfoStaffDepartment getBaseinfoStaffDepartmentById(Long id);

    Integer countBaseinfoStaffDepartment(Map<String, Object> params);

    List<BaseinfoStaffDepartment> getBaseinfoStaffDepartmentList(Map<String, Object> params);
	
}