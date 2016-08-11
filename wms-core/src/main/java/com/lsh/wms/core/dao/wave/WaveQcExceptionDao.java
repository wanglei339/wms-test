package com.lsh.wms.core.dao.wave;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.wave.WaveQcException;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface WaveQcExceptionDao {

	void insert(WaveQcException waveQcException);
	
	void update(WaveQcException waveQcException);
	
	WaveQcException getWaveQcExceptionById(Long id);

    Integer countWaveQcException(Map<String, Object> params);

    List<WaveQcException> getWaveQcExceptionList(Map<String, Object> params);
	
}