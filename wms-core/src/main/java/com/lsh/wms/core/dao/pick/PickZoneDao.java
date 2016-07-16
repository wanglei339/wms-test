package com.lsh.wms.core.dao.pick;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.pick.PickZone;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface PickZoneDao {

	void insert(PickZone pickZone);
	
	void update(PickZone pickZone);
	
	PickZone getPickZoneById(Long id);

    Integer countPickZone(Map<String, Object> params);

    List<PickZone> getPickZoneList(Map<String, Object> params);
	
}