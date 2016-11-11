package com.lsh.wms.core.dao.baseinfo;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.baseinfo.BassinfoItemTypeRelation;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface BassinfoItemTypeRelationDao {

	void insert(BassinfoItemTypeRelation bassinfoItemTypeRelation);
	
	void update(BassinfoItemTypeRelation bassinfoItemTypeRelation);
	
	BassinfoItemTypeRelation getBassinfoItemTypeRelationById(Long id);

    Integer countBassinfoItemTypeRelation(Map<String, Object> params);

    List<BassinfoItemTypeRelation> getBassinfoItemTypeRelationList(Map<String, Object> params);
	
}