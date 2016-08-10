package com.lsh.wms.core.dao.baseinfo;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.baseinfo.BaseinfoShelflifeRule;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface BaseinfoShelflifeRuleDao {

	void insert(BaseinfoShelflifeRule baseinfoShelflifeRule);
	
	void update(BaseinfoShelflifeRule baseinfoShelflifeRule);
	
	BaseinfoShelflifeRule getBaseinfoShelflifeRuleById(Long id);

    Integer countBaseinfoShelflifeRule(Map<String, Object> params);

    List<BaseinfoShelflifeRule> getBaseinfoShelflifeRuleList(Map<String, Object> params);

	void delete(BaseinfoShelflifeRule baseinfoShelflifeRule);
}