package com.lsh.wms.core.dao.wave;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.wave.WaveHead;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface WaveHeadDao {

	void insert(WaveHead waveHead);
	
	void update(WaveHead waveHead);
	
	WaveHead getWaveHeadById(Long id);

    Integer countWaveHead(Map<String, Object> params);

    List<WaveHead> getWaveHeadList(Map<String, Object> params);
	
}