package com.lsh.wms.core.dao.po;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.po.InbPoDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface InbPoDetailDao {

	void insert(InbPoDetail inbPoDetail);

	void batchInsert(List<InbPoDetail> list);

	void update(InbPoDetail inbPoDetail);

	void updateInboundQtyByOrderIdAndSkuId(Long inboundQty, Long orderId,Long skuId);
	void batchUpdateInboundQtyByOrderIdAndSkuId(List<InbPoDetail> list);

	InbPoDetail getInbPoDetailById(Long id);

	Integer countInbPoDetail(Map<String, Object> params);

	List<InbPoDetail> getInbPoDetailList(Map<String, Object> params);
	
}