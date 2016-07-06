package com.lsh.wms.core.dao.up;


import com.lsh.wms.api.model.up.UpApp;
import com.lsh.wms.core.dao.MyBatisRepository;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface UpAppDao {

	void insert(UpApp app);
	
	void update(UpApp app);
	
	UpApp getAppById(Integer id);

    Integer countApp(Map<String, Object> params);

    List<UpApp> getAppList(Map<String, Object> params);
	
}