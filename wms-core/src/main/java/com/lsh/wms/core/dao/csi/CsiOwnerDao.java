package com.lsh.wms.core.dao.csi;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.csi.CsiOwner;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface CsiOwnerDao {

	void insert(CsiOwner csiOwner);
	
	void update(CsiOwner csiOwner);
	
	CsiOwner getCsiOwnerById(Integer id);

    Integer countCsiOwner(Map<String, Object> params);

    List<CsiOwner> getCsiOwnerList(Map<String, Object> params);
	
}