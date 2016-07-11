package com.lsh.wms.core.dao.baseinfo;

import com.lsh.admin.core.dao.mysql.MyBatisRepository;
import com.lsh.admin.model.pub.BaseinfoDepartment;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface BaseinfoDepartmentDao {

	void insert(BaseinfoDepartment baseinfoDepartment);
	
	void update(BaseinfoDepartment baseinfoDepartment);
	
	BaseinfoDepartment getBaseinfoDepartmentById(Integer id);

    Integer countBaseinfoDepartment(Map<String, Object> params);

    List<BaseinfoDepartment> getBaseinfoDepartmentList(Map<String, Object> params);
	
}