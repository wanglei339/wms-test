package com.lsh.wms.core.dao.datareport;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.datareport.SkuMap;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface SkuMapDao {

	void insert(SkuMap skuMap);

	void batchInsert(List<SkuMap> skuMaps);

	void update(SkuMap skuMap);

	void batchUpdate(List<SkuMap> skuMaps);

	SkuMap getSkuMapById(Long id);

    Integer countSkuMap(Map<String, Object> params);

    List<SkuMap> getSkuMapList(Map<String, Object> params);
	
}