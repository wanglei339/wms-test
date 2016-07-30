package com.lsh.wms.core.dao.wave;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.wave.WaveDetail;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface WaveDetailDao {

	void insert(WaveDetail pickTaskDetail);
	
	void update(WaveDetail pickTaskDetail);

	WaveDetail getWaveDetailById(Long id);

    Integer countWaveDetail(Map<String, Object> params);

    List<WaveDetail> getWaveDetailList(Map<String, Object> params);
	
}