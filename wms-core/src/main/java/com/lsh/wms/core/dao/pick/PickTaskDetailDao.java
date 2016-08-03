package com.lsh.wms.core.dao.pick;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.pick.PickTaskDetail;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface PickTaskDetailDao {

	void insert(PickTaskDetail pickTaskDetail);
	
	void update(PickTaskDetail pickTaskDetail);
	
	PickTaskDetail getPickTaskDetailById(Long id);

    Integer countPickTaskDetail(Map<String, Object> params);

    List<PickTaskDetail> getPickTaskDetailList(Map<String, Object> params);
	
}