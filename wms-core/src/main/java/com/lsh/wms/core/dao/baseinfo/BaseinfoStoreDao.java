package com.lsh.wms.core.dao.baseinfo;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.baseinfo.BaseinfoStore;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface BaseinfoStoreDao {

	void insert(BaseinfoStore baseinfoStore);
	
	void update(BaseinfoStore baseinfoStore);
	
	BaseinfoStore getBaseinfoStoreById(String storeNo);

    Integer countBaseinfoStore(Map<String, Object> params);

    List<BaseinfoStore> getBaseinfoStoreList(Map<String, Object> params);
	
}