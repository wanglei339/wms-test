package com.lsh.wms.core.dao.tu;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.tu.TuDetail;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface TuDetailDao {

	void insert(TuDetail tuDetail);
	
	void update(TuDetail tuDetail);
	
	TuDetail getTuDetailById(Long id);

    Integer countTuDetail(Map<String, Object> params);

    List<TuDetail> getTuDetailList(Map<String, Object> params);
	
}