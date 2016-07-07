package com.lsh.wms.core.dao.up;


import com.lsh.wms.core.model.up.UpVersionRulesCons;
import com.lsh.wms.core.dao.MyBatisRepository;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface UpVersionRulesConsDao {

	void insert(UpVersionRulesCons versionRulesCons);
	
	void update(UpVersionRulesCons versionRulesCons);

	void delete(Long id);
	
	UpVersionRulesCons getVersionRulesConsById(Long id);

    Integer countVersionRulesCons(Map<String, Object> params);

    List<UpVersionRulesCons> getVersionRulesConsList(Map<String, Object> params);
	
}