package com.lsh.wms.core.dao.baseinfo;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.baseinfo.BaseinfoDepartment;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface BaseinfoDepartmentDao {

	void insert(BaseinfoDepartment baseinfoDepartment);
	
	void update(BaseinfoDepartment baseinfoDepartment);
	
	BaseinfoDepartment getBaseinfoDepartmentById(Long id);

    Integer countBaseinfoDepartment(Map<String, Object> params);

    List<BaseinfoDepartment> getBaseinfoDepartmentList(Map<String, Object> params);
	
}