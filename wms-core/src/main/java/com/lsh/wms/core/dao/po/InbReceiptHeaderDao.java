package com.lsh.wms.core.dao.po;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.po.InbReceiptHeader;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface InbReceiptHeaderDao {

	void insert(InbReceiptHeader inbReceiptHeader);
	
	void update(InbReceiptHeader inbReceiptHeader);

	void updateByReceiptId(InbReceiptHeader inbReceiptHeader);
	
	InbReceiptHeader getInbReceiptHeaderById(Long id);

    Integer countInbReceiptHeader(Map<String, Object> params);

    List<InbReceiptHeader> getInbReceiptHeaderList(Map<String, Object> params);
	
}