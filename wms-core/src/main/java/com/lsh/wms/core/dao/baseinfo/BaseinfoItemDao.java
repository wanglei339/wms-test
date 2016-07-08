package com.lsh.wms.core.dao.baseinfo;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.baseinfo.BaseinfoItem;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface BaseinfoItemDao {

	void insert(BaseinfoItem baseinfoItem);
	
	void update(BaseinfoItem baseinfoItem);
	
	BaseinfoItem getBaseinfoItemById(Integer id);

    Integer countBaseinfoItem(Map<String, Object> params);

    List<BaseinfoItem> getBaseinfoItemList(Map<String, Object> params);
	
}