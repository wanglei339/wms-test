package com.lsh.wms.core.dao.up;


import com.lsh.wms.model.up.UpRule;
import com.lsh.wms.core.dao.MyBatisRepository;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface UpRuleDao {

	void insert(UpRule rule);
	
	void update(UpRule rule);
	
	UpRule getRuleById(Integer id);

    Integer countRule(Map<String, Object> params);

    List<UpRule> getRuleList(Map<String, Object> params);
	
}