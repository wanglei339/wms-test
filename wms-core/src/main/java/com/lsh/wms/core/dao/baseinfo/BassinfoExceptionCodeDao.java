package com.lsh.wms.core.dao.baseinfo;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.baseinfo.BassinfoExceptionCode;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface BassinfoExceptionCodeDao {

	void insert(BassinfoExceptionCode bassinfoExceptionCode);
	
	void update(BassinfoExceptionCode bassinfoExceptionCode);
	
	BassinfoExceptionCode getBassinfoExceptionCodeById(Long id);

    Integer countBassinfoExceptionCode(Map<String, Object> params);

    List<BassinfoExceptionCode> getBassinfoExceptionCodeList(Map<String, Object> params);
	
}