package com.lsh.wms.core.dao.seed;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.seed.SeedingTaskHead;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface SeedingTaskHeadDao {

	void insert(SeedingTaskHead seedingTaskHead);
	
	void update(SeedingTaskHead seedingTaskHead);
	
	SeedingTaskHead getSeedingTaskHeadById(Long id);

	SeedingTaskHead getSeedingTaskHeadByTaskId(Long taskId);

    Integer countSeedingTaskHead(Map<String, Object> params);

	Integer countDistinctTaskHead(Map<String, Object> params);

    List<SeedingTaskHead> getSeedingTaskHeadList(Map<String, Object> params);

	List<SeedingTaskHead> getDistinctTaskHeadList(Map<String, Object> params);
	
}