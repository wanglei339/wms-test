package com.lsh.wms.core.dao.back;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.back.BackTaskDetail;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface BackTaskDetailDao {

	void insert(BackTaskDetail backTaskDetail);
	
	void update(BackTaskDetail backTaskDetail);
	
	BackTaskDetail getBackTaskDetailById(Long id);

    Integer countBackTaskDetail(Map<String, Object> params);

    List<BackTaskDetail> getBackTaskDetailList(Map<String, Object> params);
	
}