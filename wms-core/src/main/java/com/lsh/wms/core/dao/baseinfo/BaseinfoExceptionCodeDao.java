package com.lsh.wms.core.dao.baseinfo;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.baseinfo.BaseinfoExceptionCode;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface BaseinfoExceptionCodeDao {

	void insert(BaseinfoExceptionCode baseinfoExceptionCode);
	
	void update(BaseinfoExceptionCode baseinfoExceptionCode);
	
	BaseinfoExceptionCode getBaseinfoExceptionCodeById(Long id);

    Integer countBaseinfoExceptionCode(Map<String, Object> params);

    List<BaseinfoExceptionCode> getBaseinfoExceptionCodeList(Map<String, Object> params);
	
}