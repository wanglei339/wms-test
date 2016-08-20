package com.lsh.wms.core.dao.wave;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.wave.WaveTemplate;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface WaveTemplateDao {

	void insert(WaveTemplate waveTemplate);
	
	void update(WaveTemplate waveTemplate);
	
	WaveTemplate getWaveTemplateById(Long id);

    Integer countWaveTemplate(Map<String, Object> params);

    List<WaveTemplate> getWaveTemplateList(Map<String, Object> params);
	
}