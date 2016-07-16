package com.lsh.wms.core.dao.baseinfo;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.baseinfo.BaseinfoStaffLevel;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface BaseinfoStaffLevelDao {

	void insert(BaseinfoStaffLevel baseinfoStaffLevel);
	
	void update(BaseinfoStaffLevel baseinfoStaffLevel);
	
	BaseinfoStaffLevel getBaseinfoStaffLevelById(Long id);

    Integer countBaseinfoStaffLevel(Map<String, Object> params);

    List<BaseinfoStaffLevel> getBaseinfoStaffLevelList(Map<String, Object> params);
	
}