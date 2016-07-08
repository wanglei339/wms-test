package com.lsh.wms.core.dao.pub;

import com.lsh.wms.model.pub.PubConfigPage;
import com.lsh.wms.core.dao.MyBatisRepository;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface PubConfigPageDao {

	void insert(PubConfigPage pubConfigPage);
	
	void update(PubConfigPage pubConfigPage);
	
	PubConfigPage getPubConfigPageById(Integer id);

    Integer countPubConfigPage(Map<String, Object> params);

    List<PubConfigPage> getPubConfigPageList(Map<String, Object> params);
	
}