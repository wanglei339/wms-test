package com.lsh.wms.core.dao.baseinfo;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.baseinfo.BaseinfoLocationBin;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface BaseinfoLocationBinDao {

	void insert(BaseinfoLocationBin baseinfoLocationBin);
	
	void update(BaseinfoLocationBin baseinfoLocationBin);
	
	BaseinfoLocationBin getBaseinfoLocationBinById(Long id);

    Integer countBaseinfoLocationBin(Map<String, Object> params);

    List<BaseinfoLocationBin> getBaseinfoLocationBinList(Map<String, Object> params);
	
}