package com.lsh.wms.core.dao.system;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.system.ModifyLog;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface ModifyLogDao {

	void insert(ModifyLog modifyLog);
	
	void update(ModifyLog modifyLog);
	
	ModifyLog getModifyLogById(Long id);

    Integer countModifyLog(Map<String, Object> params);

    List<ModifyLog> getModifyLogList(Map<String, Object> params);
	
}