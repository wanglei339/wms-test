package com.lsh.wms.core.dao.pub;

import com.lsh.wms.model.pub.PubDict;
import com.lsh.wms.core.dao.MyBatisRepository;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface PubDictDao {

	void insert(PubDict pubDict);
	
	void update(PubDict pubDict);
	
	PubDict getPubDictById(Integer id);

    Integer countPubDict(Map<String, Object> params);

    List<PubDict> getPubDictList(Map<String, Object> params);
	
}