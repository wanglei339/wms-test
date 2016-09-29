package com.lsh.wms.core.dao.po;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.po.IbdObdRelation;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface IbdObdRelationDao {

	void insert(IbdObdRelation ibdObdRelation);
	
	void update(IbdObdRelation ibdObdRelation);
	
	IbdObdRelation getIbdObdRelationById(Long id);

    Integer countIbdObdRelation(Map<String, Object> params);

    List<IbdObdRelation> getIbdObdRelationList(Map<String, Object> params);
	
}