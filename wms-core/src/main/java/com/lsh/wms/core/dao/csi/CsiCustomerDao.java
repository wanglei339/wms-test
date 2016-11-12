package com.lsh.wms.core.dao.csi;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.csi.CsiCustomer;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface CsiCustomerDao {

	void insert(CsiCustomer csiCustomer);
	
	void update(CsiCustomer csiCustomer);
	
	CsiCustomer getCsiCustomerById(Long id);

    Integer countCsiCustomer(Map<String, Object> params);

    List<CsiCustomer> getCsiCustomerList(Map<String, Object> params);
	
}