package com.lsh.wms.core.dao.baseinfo;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.baseinfo.BaseinfoLocationShelf;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface BaseinfoLocationShelfDao {

	void insert(BaseinfoLocationShelf baseinfoLocationShelf);
	
	void update(BaseinfoLocationShelf baseinfoLocationShelf);
	
	BaseinfoLocationShelf getBaseinfoLocationShelfById(Long id);

    Integer countBaseinfoLocationShelf(Map<String, Object> params);

    List<BaseinfoLocationShelf> getBaseinfoLocationShelfList(Map<String, Object> params);
	
}