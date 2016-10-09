package com.lsh.wms.core.dao.po;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.po.ReceiveHeader;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface ReceiveHeaderDao {

	void insert(ReceiveHeader receiveHeader);
	
	void update(ReceiveHeader receiveHeader);
	
	ReceiveHeader getReceiveHeaderById(Long id);

    Integer countReceiveHeader(Map<String, Object> params);

    List<ReceiveHeader> getReceiveHeaderList(Map<String, Object> params);
	
}