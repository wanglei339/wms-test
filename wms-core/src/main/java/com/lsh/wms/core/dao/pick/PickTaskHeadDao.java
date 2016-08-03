package com.lsh.wms.core.dao.pick;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.pick.PickTaskHead;

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