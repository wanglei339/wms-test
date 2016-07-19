package com.lsh.wms.api.service.pick;

import com.lsh.wms.model.pick.PickWaveHead;
import com.lsh.wms.model.pick.WaveRequest;

import java.util.List;
import java.util.Map;

/**
 * Created by zengwenjun on 16/7/15.
 */
public interface IWaveRestService {
    public String getList(Map<String, Object> mapQuery);
    public String  getListCount(Map<String, Object> mapQuery);
    public String getWave(long iWaveId);
    public String getWaveOrders(long iWaveId);
    public String releaseWave(long iWaveId, long iUid, String iUName);

    String createWave(WaveRequest request);
    String setStatus(long iWaveId, int iStatus);
}
