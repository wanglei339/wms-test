package com.lsh.wms.core.dao.pick;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.pick.PickModel;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface PickModelDao {

	void insert(PickModel pickModel);
	
	void update(PickModel pickModel);
	
	PickModel getPickModelById(Long id);

    Integer countPickModel(Map<String, Object> params);

    List<PickModel> getPickModelList(Map<String, Object> params);
	
}