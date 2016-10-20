package com.lsh.wms.core.dao.tu;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.tu.TuHead;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface TuHeadDao {

	void insert(TuHead tuHead);
	
	void update(TuHead tuHead);
	
	TuHead getTuHeadById(Long id);

    Integer countTuHead(Map<String, Object> params);

    List<TuHead> getTuHeadList(Map<String, Object> params);
	
}