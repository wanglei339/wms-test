package com.lsh.wms.core.dao.pub;


import com.lsh.wms.model.pub.PubConfigShow;
import com.lsh.wms.core.dao.MyBatisRepository;

@MyBatisRepository
public interface PubConfigShowDao {

	void insert(PubConfigShow pubConfigShow);
	
	void update(PubConfigShow pubConfigShow);
	
	PubConfigShow getPubConfigShowById(Integer id);

	PubConfigShow getPubConfigShowByCode(String code);

}