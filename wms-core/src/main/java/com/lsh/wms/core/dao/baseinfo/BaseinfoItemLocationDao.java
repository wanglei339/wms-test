package com.lsh.wms.core.dao.baseinfo;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.baseinfo.BaseinfoItemLocation;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface BaseinfoItemLocationDao {

	void insert(BaseinfoItemLocation baseinfoItemLocation);
	
	void update(BaseinfoItemLocation baseinfoItemLocation);
	
	BaseinfoItemLocation getBaseinfoItemLocationById(Long id);

    Integer countBaseinfoItemLocation(Map<String, Object> params);

    List<BaseinfoItemLocation> getBaseinfoItemLocationList(Map<String, Object> params);
	
}