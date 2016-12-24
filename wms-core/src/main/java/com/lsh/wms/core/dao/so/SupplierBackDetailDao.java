package com.lsh.wms.core.dao.so;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.so.ObdDetail;
import com.lsh.wms.model.so.SupplierBackDetail;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface SupplierBackDetailDao {

	void insert(SupplierBackDetail supplierBackDetail);

	void batchInsert(List<SupplierBackDetail> list);

	void update(SupplierBackDetail supplierBackDetail);
	
	SupplierBackDetail getSupplierBackDetailById(Long id);

    Integer countSupplierBackDetail(Map<String, Object> params);

    List<SupplierBackDetail> getSupplierBackDetailList(Map<String, Object> params);
	
}