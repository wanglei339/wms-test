package com.lsh.wms.core.dao.so;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.so.OutbSoHeader;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface OutbSoHeaderDao {

	void insert(OutbSoHeader outbSoHeader);
	
	void update(OutbSoHeader outbSoHeader);

	void updateByOrderOtherIdOrOrderId(OutbSoHeader outbSoHeader);

	OutbSoHeader getOutbSoHeaderById(Long id);

    Integer countOutbSoHeader(Map<String, Object> params);

    List<OutbSoHeader> getOutbSoHeaderList(Map<String, Object> params);
	
}