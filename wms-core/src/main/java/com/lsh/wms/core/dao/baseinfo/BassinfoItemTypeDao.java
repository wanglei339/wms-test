package com.lsh.wms.core.dao.baseinfo;

import com.lsh.wms.core.dao.MyBatisRepository;

import com.lsh.wms.model.baseinfo.BassinfoItemType;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface BassinfoItemTypeDao {

	void insert(BassinfoItemType bassinfoItemType);
	
	void update(BassinfoItemType bassinfoItemType);
	
	BassinfoItemType getBassinfoItemTypeById(Integer id);

    Integer countBassinfoItemType(Map<String, Object> params);

    List<BassinfoItemType> getBassinfoItemTypeList(Map<String, Object> params);
	
}