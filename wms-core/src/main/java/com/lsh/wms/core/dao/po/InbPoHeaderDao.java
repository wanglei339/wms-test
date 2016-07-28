package com.lsh.wms.core.dao.po;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.po.InbPoHeader;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface InbPoHeaderDao {

	void insert(InbPoHeader inbPoHeader);
	
	void update(InbPoHeader inbPoHeader);

	void updateByOrderOtherIdOrOrderId(InbPoHeader inbPoHeader);

	InbPoHeader getInbPoHeaderById(Long id);

    Integer countInbPoHeader(Map<String, Object> params);

    List<InbPoHeader> getInbPoHeaderList(Map<String, Object> params);
	
}