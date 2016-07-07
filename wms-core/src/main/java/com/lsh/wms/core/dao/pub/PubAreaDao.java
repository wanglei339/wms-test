package com.lsh.wms.core.dao.pub;


import com.lsh.wms.model.pub.PubArea;
import com.lsh.wms.core.dao.MyBatisRepository;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface PubAreaDao {

	void insert(PubArea pubArea);
	
	void update(PubArea pubArea);
	
	PubArea getPubAreaById(Integer id);

    Integer countPubArea(Map<String, Object> params);

    List<PubArea> getPubAreaList(Map<String, Object> params);
	
}