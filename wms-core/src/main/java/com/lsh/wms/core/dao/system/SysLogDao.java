package com.lsh.wms.core.dao.system;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.system.SysLog;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface SysLogDao {

	void insert(SysLog sysLog);
	
	void update(SysLog sysLog);
	
	SysLog getSysLogById(Long id);

    Integer countSysLog(Map<String, Object> params);

    List<SysLog> getSysLogList(Map<String, Object> params);
	
}