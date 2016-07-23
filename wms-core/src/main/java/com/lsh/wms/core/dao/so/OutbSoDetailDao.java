package com.lsh.wms.core.dao.so;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.so.OutbSoDetail;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface OutbSoDetailDao {

	void insert(OutbSoDetail outbSoDetail);

	void batchInsert(List<OutbSoDetail> list);

	void update(OutbSoDetail outbSoDetail);
	
	OutbSoDetail getOutbSoDetailById(Long id);

    Integer countOutbSoDetail(Map<String, Object> params);

    List<OutbSoDetail> getOutbSoDetailList(Map<String, Object> params);
	
}