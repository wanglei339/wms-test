package com.lsh.wms.core.dao.csi;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.csi.CsiSupplier;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface CsiSupplierDao {

	void insert(CsiSupplier csiSupplier);
	
	void update(CsiSupplier csiSupplier);
	
	CsiSupplier getCsiSupplierById(Integer id);

    Integer countCsiSupplier(Map<String, Object> params);

    List<CsiSupplier> getCsiSupplierList(Map<String, Object> params);
	
}