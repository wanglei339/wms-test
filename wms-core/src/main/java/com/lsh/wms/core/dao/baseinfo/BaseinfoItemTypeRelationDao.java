package com.lsh.wms.core.dao.baseinfo;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.baseinfo.BaseinfoItemTypeRelation;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface BaseinfoItemTypeRelationDao {

	void insert(BaseinfoItemTypeRelation baseinfoItemTypeRelation);
	
	void update(BaseinfoItemTypeRelation baseinfoItemTypeRelation);
	
	BaseinfoItemTypeRelation getBaseinfoItemTypeRelationById(Long id);

    Integer countBaseinfoItemTypeRelation(Map<String, Object> params);

    List<BaseinfoItemTypeRelation> getBaseinfoItemTypeRelationList(Map<String, Object> params);

	List<Map<String, Object>> getBaseinfoItemTypeAllRelationList(Map<String, Object> params);

	Integer countBaseinfoItemTypeAllRelationList(Map<String, Object> params);
	
}