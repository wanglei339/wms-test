package com.lsh.wms.core.dao.up;


import com.lsh.wms.model.up.UpOpsystem;
import com.lsh.wms.core.dao.MyBatisRepository;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface UpOpsystemDao {

	void insert(UpOpsystem opsystem);
	
	void update(UpOpsystem opsystem);
	
	UpOpsystem getOpsystemById(Integer id);

    Integer countOpsystem(Map<String, Object> params);

	List<UpOpsystem> getOpsystemList(Map<String, Object> params);

}