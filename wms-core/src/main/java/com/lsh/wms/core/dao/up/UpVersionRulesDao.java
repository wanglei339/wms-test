package com.lsh.wms.core.dao.up;


import com.lsh.wms.model.up.UpVersionRules;
import com.lsh.wms.core.dao.MyBatisRepository;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface UpVersionRulesDao {

	void insert(UpVersionRules versionRules);
	
	void update(UpVersionRules versionRules);
	
	UpVersionRules getVersionRulesById(Long id);

    Integer countVersionRules(Map<String, Object> params);

    List<UpVersionRules> getVersionRulesList(Map<String, Object> params);
	
}