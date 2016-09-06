package com.lsh.wms.api.service.wave;

import com.lsh.wms.model.wave.WaveRequest;

/**
 * Created by zengwenjun on 16/9/6.
 */
public interface IWaveService {
    String createAndReleaseWave(WaveRequest request);
}
