package com.lsh.wms.core.dao.so;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.so.OutbDeliveryDetail;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface OutbDeliveryDetailDao {

	void insert(OutbDeliveryDetail outbDeliveryDetail);

	void batchInsert(List<OutbDeliveryDetail> list);
	
	void update(OutbDeliveryDetail outbDeliveryDetail);
	
	OutbDeliveryDetail getOutbDeliveryDetailById(Long id);

    Integer countOutbDeliveryDetail(Map<String, Object> params);

    List<OutbDeliveryDetail> getOutbDeliveryDetailList(Map<String, Object> params);
	
}