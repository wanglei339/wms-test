package com.lsh.wms.core.dao.pick;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.pick.PickAllocDetail;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface PickAllocDetailDao {

	void insert(PickAllocDetail pickAllocDetail);
	
	void update(PickAllocDetail pickAllocDetail);
	
	PickAllocDetail getPickAllocDetailById(Long id);

    Integer countPickAllocDetail(Map<String, Object> params);

    List<PickAllocDetail> getPickAllocDetailList(Map<String, Object> params);
	
}