package com.lsh.wms.api.service.wave;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.model.wave.WaveRequest;

import javax.ws.rs.QueryParam;

/**
 * Created by zengwenjun on 16/7/15.
 */
public interface IWaveRpcService {
    public Long createWave(WaveRequest request) throws BizCheckedException;
    public void releaseWave(long iWaveId, long iUid) throws BizCheckedException;
}
