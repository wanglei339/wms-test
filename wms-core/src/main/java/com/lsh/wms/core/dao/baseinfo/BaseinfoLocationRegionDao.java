package com.lsh.wms.core.dao.baseinfo;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.baseinfo.BaseinfoLocationRegion;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface BaseinfoLocationRegionDao {

	void insert(BaseinfoLocationRegion baseinfoLocationRegion);
	
	void update(BaseinfoLocationRegion baseinfoLocationRegion);
	
	BaseinfoLocationRegion getBaseinfoLocationRegionById(Long id);

    Integer countBaseinfoLocationRegion(Map<String, Object> params);

    List<BaseinfoLocationRegion> getBaseinfoLocationRegionList(Map<String, Object> params);
	
}