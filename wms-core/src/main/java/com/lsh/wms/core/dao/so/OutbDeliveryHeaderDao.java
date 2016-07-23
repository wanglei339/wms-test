package com.lsh.wms.core.dao.so;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.so.OutbDeliveryHeader;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface OutbDeliveryHeaderDao {

	void insert(OutbDeliveryHeader outbDeliveryHeader);
	
	void update(OutbDeliveryHeader outbDeliveryHeader);

	void updateByDeliveryId(OutbDeliveryHeader outbDeliveryHeader);
	
	OutbDeliveryHeader getOutbDeliveryHeaderById(Long id);

    Integer countOutbDeliveryHeader(Map<String, Object> params);

    List<OutbDeliveryHeader> getOutbDeliveryHeaderList(Map<String, Object> params);
	
}