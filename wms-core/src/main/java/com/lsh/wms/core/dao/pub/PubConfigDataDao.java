package com.lsh.wms.core.dao.pub;


import com.lsh.wms.model.pub.PubConfigData;
import com.lsh.wms.core.dao.MyBatisRepository;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface PubConfigDataDao {

	void insert(PubConfigData pubConfigData);
	
	void update(PubConfigData pubConfigData);
	
	PubConfigData getPubConfigDataById(Integer id);

    Integer countPubConfigData(Map<String, Object> params);

    List<PubConfigData> getPubConfigDataList(Map<String, Object> params);
	
}