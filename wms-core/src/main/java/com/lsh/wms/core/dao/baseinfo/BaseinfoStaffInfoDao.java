package com.lsh.wms.core.dao.baseinfo;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.baseinfo.BaseinfoStaffInfo;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface BaseinfoStaffInfoDao {

	void insert(BaseinfoStaffInfo baseinfoStaffInfo);
	
	void update(BaseinfoStaffInfo baseinfoStaffInfo);
	
	BaseinfoStaffInfo getBaseinfoStaffInfoById(Long id);

    Integer countBaseinfoStaffInfo(Map<String, Object> params);

    List<BaseinfoStaffInfo> getBaseinfoStaffInfoList(Map<String, Object> params);
	
}