package com.lsh.wms.core.dao.baseinfo;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.baseinfo.BaseinfoLot;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface BaseinfoLotDao {

	void insert(BaseinfoLot baseinfoLot);
	
	void update(BaseinfoLot baseinfoLot);
	
	BaseinfoLot getLotByLotId(Long lotId);

	BaseinfoLot getBaseinfoLotById(Long id);

    Integer countBaseinfoLot(Map<String, Object> params);

    List<BaseinfoLot> getBaseinfoLotList(Map<String, Object> params);
	
}