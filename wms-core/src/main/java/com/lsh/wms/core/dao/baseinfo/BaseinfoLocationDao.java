package com.lsh.wms.core.dao.baseinfo;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface BaseinfoLocationDao{

	void insert(BaseinfoLocation baseinfoLocation);
	
	void update(BaseinfoLocation baseinfoLocation);
	
	BaseinfoLocation getBaseinfoLocationById(Long id);

    Integer countBaseinfoLocation(Map<String, Object> params);

    List<BaseinfoLocation> getBaseinfoLocationList(Map<String, Object> params);
//设置dock的服务,处理出入码头的关联查询
	List<BaseinfoLocation> getDockList(Map<String, Object> params);
	
}