package com.lsh.wms.core.dao.up;

import com.lsh.wms.api.model.up.UpPackage;
import com.lsh.wms.core.dao.MyBatisRepository;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface UpPackageDao {

	void insert(UpPackage upPackage);
	
	void update(UpPackage upPackage);

	UpPackage getPackageById(Long id);

    Integer countPackage(Map<String, Object> params);

    List<UpPackage> getPackageList(Map<String, Object> params);
	
}