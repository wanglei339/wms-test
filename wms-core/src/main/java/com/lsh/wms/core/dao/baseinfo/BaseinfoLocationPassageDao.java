package com.lsh.wms.core.dao.baseinfo;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.baseinfo.BaseinfoLocationPassage;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface BaseinfoLocationPassageDao {

	void insert(BaseinfoLocationPassage baseinfoLocationPassage);
	
	void update(BaseinfoLocationPassage baseinfoLocationPassage);
	
	BaseinfoLocationPassage getBaseinfoLocationPassageById(Long id);

    Integer countBaseinfoLocationPassage(Map<String, Object> params);

    List<BaseinfoLocationPassage> getBaseinfoLocationPassageList(Map<String, Object> params);
	
}