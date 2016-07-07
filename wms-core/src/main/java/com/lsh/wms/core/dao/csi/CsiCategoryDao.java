package com.lsh.wms.core.dao.csi;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.csi.CsiCategory;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface CsiCategoryDao {

	void insert(CsiCategory csiCategory);
	
	void update(CsiCategory csiCategory);
	
	CsiCategory getCsiCategoryById(Integer id);

    Integer countCsiCategory(Map<String, Object> params);

    List<CsiCategory> getCsiCategoryList(Map<String, Object> params);
	
}