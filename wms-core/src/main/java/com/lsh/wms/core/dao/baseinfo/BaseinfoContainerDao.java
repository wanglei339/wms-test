package com.lsh.wms.core.dao.baseinfo;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.baseinfo.BaseinfoContainer;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface BaseinfoContainerDao {

	void insert(BaseinfoContainer baseinfoContainer);
	
	void update(BaseinfoContainer baseinfoContainer);
	
	BaseinfoContainer getBaseinfoContainerById(Integer id);

    Integer countBaseinfoContainer(Map<String, Object> params);

    List<BaseinfoContainer> getBaseinfoContainerList(Map<String, Object> params);
	
}