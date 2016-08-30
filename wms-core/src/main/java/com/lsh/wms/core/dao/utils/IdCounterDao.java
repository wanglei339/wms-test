package com.lsh.wms.core.dao.utils;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.utils.IdCounter;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface IdCounterDao {

	void insert(IdCounter idCounter);
	
	void update(IdCounter idCounter);
	
	IdCounter getIdCounterById(Long id);

    Integer countIdCounter(Map<String, Object> params);

    List<IdCounter> getIdCounterList(Map<String, Object> params);

	IdCounter getIdCounterByIdKey(String idKey);
	
}