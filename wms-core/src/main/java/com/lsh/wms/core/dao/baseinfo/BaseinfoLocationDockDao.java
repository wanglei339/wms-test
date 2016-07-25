package com.lsh.wms.core.dao.baseinfo;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.baseinfo.BaseinfoLocationDock;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface BaseinfoLocationDockDao{

	void insert(BaseinfoLocationDock baseinfoLocationDock);
	
	void update(BaseinfoLocationDock baseinfoLocationDock);
	
	BaseinfoLocationDock getBaseinfoLocationDockById(Long id);

    Integer countBaseinfoLocationDock(Map<String, Object> params);

    List<BaseinfoLocationDock> getBaseinfoLocationDockList(Map<String, Object> params);
	
}