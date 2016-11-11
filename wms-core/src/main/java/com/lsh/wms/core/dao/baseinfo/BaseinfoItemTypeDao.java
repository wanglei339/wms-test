package com.lsh.wms.core.dao.baseinfo;

import com.lsh.wms.core.dao.MyBatisRepository;

import com.lsh.wms.model.baseinfo.BaseinfoItemType;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface BaseinfoItemTypeDao {

	void insert(BaseinfoItemType baseinfoItemType);
	
	void update(BaseinfoItemType baseinfoItemType);
	
	BaseinfoItemType getBaseinfoItemTypeById(Integer id);

    Integer countBaseinfoItemType(Map<String, Object> params);

    List<BaseinfoItemType> getBaseinfoItemTypeList(Map<String, Object> params);
	
}