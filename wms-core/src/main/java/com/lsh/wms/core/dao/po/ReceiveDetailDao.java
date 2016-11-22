package com.lsh.wms.core.dao.po;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.po.ReceiveDetail;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface ReceiveDetailDao {

	void insert(ReceiveDetail receiveDetail);

	void batchInsert(List<ReceiveDetail> list);

	void update(ReceiveDetail receiveDetail);

	void updateByReceiveIdAndDetailOtherId(ReceiveDetail receiveDetail);

	void updateInboundQtyByReceiveIdAndDetailOtherId(BigDecimal inboundQty, Long receiveId, String detailOtherId);

	void batchUpdateInboundQtyByReceiveIdAndDetailOtherId(List<ReceiveDetail> list);
	
	ReceiveDetail getReceiveDetailById(Long id);

    Integer countReceiveDetail(Map<String, Object> params);

    List<ReceiveDetail> getReceiveDetailList(Map<String, Object> params);
	
}