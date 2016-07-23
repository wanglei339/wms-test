package com.lsh.wms.core.dao.baseinfo;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.baseinfo.BaseinfoLocationWarehouse;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface BaseinfoLocationWarehouseDao {

	void insert(BaseinfoLocationWarehouse baseinfoLocationWarehouse);
	
	void update(BaseinfoLocationWarehouse baseinfoLocationWarehouse);
	
	BaseinfoLocationWarehouse getBaseinfoLocationWarehouseById(Long id);

    Integer countBaseinfoLocationWarehouse(Map<String, Object> params);

    List<BaseinfoLocationWarehouse> getBaseinfoLocationWarehouseList(Map<String, Object> params);
	
}