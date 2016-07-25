package com.lsh.wms.core.dao.shelve;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.shelve.ShelveTaskHead;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface ShelveTaskHeadDao {

	void insert(ShelveTaskHead shelveTaskHead);
	
	void update(ShelveTaskHead shelveTaskHead);
	
	ShelveTaskHead getShelveTaskHeadById(Long id);

    Integer countShelveTaskHead(Map<String, Object> params);

    List<ShelveTaskHead> getShelveTaskHeadList(Map<String, Object> params);
	
}