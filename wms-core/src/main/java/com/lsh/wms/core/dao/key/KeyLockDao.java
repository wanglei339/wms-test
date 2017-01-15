package com.lsh.wms.core.dao.key;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.key.KeyLock;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface KeyLockDao {

	void insert(KeyLock keyLock);
	
	void update(KeyLock keyLock);
	
	KeyLock getKeyLockById(Long id);

    Integer countKeyLock(Map<String, Object> params);

    List<KeyLock> getKeyLockList(Map<String, Object> params);

	void deleteKeyLock(KeyLock keyLock);
	
}