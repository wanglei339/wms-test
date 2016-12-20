package com.lsh.wms.core.dao.wave;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.wave.WaveDetail;

import java.util.List;
import java.util.Map;
import java.math.BigDecimal;

@MyBatisRepository
public interface WaveDetailDao {

	void insert(WaveDetail waveDetail);
	
	void update(WaveDetail waveDetail);

	void shipWave(long waveId);
	
	WaveDetail getWaveDetailById(Long id);

    Integer countWaveDetail(Map<String, Object> params);

    List<WaveDetail> getWaveDetailList(Map<String, Object> params);

	BigDecimal getUnPickedQty(Map<String, Object> params);

    List<WaveDetail> getOrderedWaveDetailList(Map<String, Object> params);

	List<Long> getPickLocationsByPickTimeRegion(Map<String, Object> params);

	List<Long> getAllocatedLocationList(Map<String, Object> params);

}