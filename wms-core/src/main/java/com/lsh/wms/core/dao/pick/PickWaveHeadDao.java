package com.lsh.wms.core.dao.pick;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.pick.PickWaveHead;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface PickWaveHeadDao {

	void insert(PickWaveHead pickWaveHead);
	
	void update(PickWaveHead pickWaveHead);
	
	PickWaveHead getPickWaveHeadById(Long id);

    Integer countPickWaveHead(Map<String, Object> params);

    List<PickWaveHead> getPickWaveHeadList(Map<String, Object> params);
	
}