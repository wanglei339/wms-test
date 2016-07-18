package com.lsh.wms.core.dao.baseinfo;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.baseinfo.BaseinfoStaffJobRelation;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface BaseinfoStaffJobRelationDao {

	void insert(BaseinfoStaffJobRelation baseinfoStaffJobRelation);
	
	void update(BaseinfoStaffJobRelation baseinfoStaffJobRelation);
	
	BaseinfoStaffJobRelation getBaseinfoStaffJobRelationById(Long id);

    Integer countBaseinfoStaffJobRelation(Map<String, Object> params);

    List<BaseinfoStaffJobRelation> getBaseinfoStaffJobRelationList(Map<String, Object> params);
	
}