package com.lsh.wms.core.dao.up;


import com.lsh.wms.model.up.UpModel;
import com.lsh.wms.core.dao.MyBatisRepository;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface UpModelDao {

	void insert(UpModel model);
	
	void update(UpModel model);
	
	UpModel getModelById(Integer id);

    Integer countModel(Map<String, Object> params);

    List<UpModel> getModelList(Map<String, Object> params);
	
}