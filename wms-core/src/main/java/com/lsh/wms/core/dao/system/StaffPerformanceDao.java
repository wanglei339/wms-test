package com.lsh.wms.core.dao.system;

import com.lsh.wms.core.dao.system.MyBatisRepository;
import com.lsh.wms.model.system.StaffPerformance;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface StaffPerformanceDao {

	void insert(StaffPerformance staffPerformance);
	
	void update(StaffPerformance staffPerformance);
	
	StaffPerformance getStaffPerformanceById(Long id);

    Integer countStaffPerformance(Map<String, Object> params);

    List<StaffPerformance> getStaffPerformanceList(Map<String, Object> params);
	
}