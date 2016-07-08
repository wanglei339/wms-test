package com.lsh.wms.core.dao.pub;

import com.lsh.wms.model.pub.PubDictItem;
import com.lsh.wms.core.dao.MyBatisRepository;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface PubDictItemDao {

	void insert(PubDictItem pubDictItem);
	
	void update(PubDictItem pubDictItem);
	
	PubDictItem getPubDictItemById(Integer id);

    Integer countPubDictItem(Map<String, Object> params);

    List<PubDictItem> getPubDictItemList(Map<String, Object> params);
	
}