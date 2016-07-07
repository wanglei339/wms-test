package com.lsh.wms.core.dao.system;


import com.lsh.wms.model.system.SysRoleFunctionRelation;
import com.lsh.wms.core.dao.MyBatisRepository;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface SysRoleFunctionRelationDao {

	void insert(SysRoleFunctionRelation sysRoleFunctionRelation);
	
	void update(SysRoleFunctionRelation sysRoleFunctionRelation);
	
	void deleteById(Integer id);
	
	SysRoleFunctionRelation getSysRoleFunctionRelationById(Integer id);

    Integer countSysRoleFunctionRelation(Map<String, Object> params);

    List<SysRoleFunctionRelation> getSysRoleFunctionRelationList(Map<String, Object> params);
	
}