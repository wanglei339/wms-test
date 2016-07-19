package com.lsh.wms.core.dao.baseinfo;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.baseinfo.BaseinfoStaffJob;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface BaseinfoStaffJobDao {

	void insert(BaseinfoStaffJob baseinfoStaffJob);
	
	void update(BaseinfoStaffJob baseinfoStaffJob);
	
	BaseinfoStaffJob getBaseinfoStaffJobById(Long id);

    Integer countBaseinfoStaffJob(Map<String, Object> params);

    List<BaseinfoStaffJob> getBaseinfoStaffJobList(Map<String, Object> params);
	
}