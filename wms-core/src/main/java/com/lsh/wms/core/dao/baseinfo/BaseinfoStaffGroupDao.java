package com.lsh.wms.core.dao.baseinfo;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.baseinfo.BaseinfoStaffGroup;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface BaseinfoStaffGroupDao {

	void insert(BaseinfoStaffGroup baseinfoStaffGroup);
	
	void update(BaseinfoStaffGroup baseinfoStaffGroup);
	
	BaseinfoStaffGroup getBaseinfoStaffGroupById(Long id);

    Integer countBaseinfoStaffGroup(Map<String, Object> params);

    List<BaseinfoStaffGroup> getBaseinfoStaffGroupList(Map<String, Object> params);
	
}