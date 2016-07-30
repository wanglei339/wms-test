package com.lsh.wms.core.dao.wave;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.wave.WaveAllocDetail;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface WaveAllocDetailDao {

	void insert(WaveAllocDetail waveAllocDetail);
	
	void update(WaveAllocDetail waveAllocDetail);

	WaveAllocDetail getWaveAllocDetailById(Long id);

    Integer countWaveAllocDetail(Map<String, Object> params);

    List<WaveAllocDetail> getWaveAllocDetailList(Map<String, Object> params);
	
}