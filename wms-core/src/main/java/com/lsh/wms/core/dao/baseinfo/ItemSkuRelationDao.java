package com.lsh.wms.core.dao.baseinfo;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.baseinfo.ItemSkuRelation;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface ItemSkuRelationDao {

	void insert(ItemSkuRelation itemSkuRelation);
	
	void update(ItemSkuRelation itemSkuRelation);
	
	ItemSkuRelation getItemSkuRelationById(Long id);

    Integer countItemSkuRelation(Map<String, Object> params);

    List<ItemSkuRelation> getItemSkuRelationList(Map<String, Object> params);
	
}