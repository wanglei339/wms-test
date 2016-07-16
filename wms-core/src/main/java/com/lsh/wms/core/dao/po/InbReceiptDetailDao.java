package com.lsh.wms.core.dao.po;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.po.InbReceiptDetail;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface InbReceiptDetailDao {

	void insert(InbReceiptDetail inbReceiptDetail);

	void batchInsert(List<InbReceiptDetail> list);

	void update(InbReceiptDetail inbReceiptDetail);
	
	InbReceiptDetail getInbReceiptDetailById(Long id);

    Integer countInbReceiptDetail(Map<String, Object> params);

    List<InbReceiptDetail> getInbReceiptDetailList(Map<String, Object> params);
	
}