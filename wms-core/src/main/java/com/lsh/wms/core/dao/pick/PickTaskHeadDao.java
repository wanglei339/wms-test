package com.lsh.wms.core.dao.shelve;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.shelve.PickTaskHead;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface PickTaskHeadDao {

	void insert(PickTaskHead pickTaskHead);
	
	void update(PickTaskHead pickTaskHead);
	
	PickTaskHead getPickTaskHeadById(Long id);

    Integer countPickTaskHead(Map<String, Object> params);

    List<PickTaskHead> getPickTaskHeadList(Map<String, Object> params);
	
}