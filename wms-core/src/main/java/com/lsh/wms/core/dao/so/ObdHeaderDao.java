package com.lsh.wms.core.dao.so;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.so.ObdHeader;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface ObdHeaderDao {

	void insert(ObdHeader obdHeader);
	
	void update(ObdHeader obdHeader);

	void updateByOrderOtherIdOrOrderId(ObdHeader obdHeader);

	ObdHeader getObdHeaderById(Long id);

	Integer countObdHeader(Map<String, Object> params);

	List<ObdHeader> getObdHeaderList(Map<String, Object> params);

	List<Map<String, Object>> getSoKanBanCount(Long orderType);
	
}