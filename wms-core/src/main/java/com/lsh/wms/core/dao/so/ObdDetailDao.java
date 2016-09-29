package com.lsh.wms.core.dao.so;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.so.ObdDetail;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface ObdDetailDao {

	void insert(ObdDetail obdDetail);

	void batchInsert(List<ObdDetail> list);

	void update(ObdDetail obdDetail);

	ObdDetail getObdDetailById(Long id);

	Integer countObdDetail(Map<String, Object> params);

	List<ObdDetail> getObdDetailList(Map<String, Object> params);
}