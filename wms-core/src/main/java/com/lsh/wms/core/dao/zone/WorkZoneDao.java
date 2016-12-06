package com.lsh.wms.core.dao.zone;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.zone.WorkZone;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface WorkZoneDao {

	void insert(WorkZone workZone);
	
	void update(WorkZone workZone);
	
	WorkZone getWorkZoneById(Long id);

    Integer countWorkZone(Map<String, Object> params);

    List<WorkZone> getWorkZoneList(Map<String, Object> params);
	
}