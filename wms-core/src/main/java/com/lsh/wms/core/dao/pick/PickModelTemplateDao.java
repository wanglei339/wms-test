package com.lsh.wms.core.dao.pick;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.pick.PickModelTemplate;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface PickModelTemplateDao {

	void insert(PickModelTemplate pickModelTemplate);
	
	void update(PickModelTemplate pickModelTemplate);
	
	PickModelTemplate getPickModelTemplateById(Long id);

    Integer countPickModelTemplate(Map<String, Object> params);

    List<PickModelTemplate> getPickModelTemplateList(Map<String, Object> params);
	
}