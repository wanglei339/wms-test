package com.lsh.wms.core.dao.po;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.po.IbdDetail;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface IbdDetailDao {

	void insert(IbdDetail ibdDetail);

	void batchInsert(List<IbdDetail> list);

	void update(IbdDetail ibdDetail);

	void updateInboundQtyByOrderIdAndDetailOtherId(Long inboundQty, Long orderId,String detailOtherId);

	void batchUpdateInboundQtyByOrderIdAndDetailOtherId(List<IbdDetail> list);

	IbdDetail getIbdDetailById(Long id);

	Integer countIbdDetail(Map<String, Object> params);

	List<IbdDetail> getIbdDetailList(Map<String, Object> params);

	BigDecimal getInbPoDetailCountByOrderId(Long orderId);
	
}