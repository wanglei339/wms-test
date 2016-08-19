package com.lsh.wms.core.dao.shelve;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.shelve.AtticShelveTaskDetail;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface AtticShelveTaskDetailDao {

	void insert(AtticShelveTaskDetail atticShelveTaskDetail);
	
	void update(AtticShelveTaskDetail atticShelveTaskDetail);
	
	AtticShelveTaskDetail getAtticShelveTaskDetailById(Long id);

    Integer countAtticShelveTaskDetail(Map<String, Object> params);

    List<AtticShelveTaskDetail> getAtticShelveTaskDetailList(Map<String, Object> params);
	
}