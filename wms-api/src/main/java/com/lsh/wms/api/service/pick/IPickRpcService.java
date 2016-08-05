package com.lsh.wms.api.service.pick;

import com.lsh.wms.model.wave.WaveDetail;

import java.util.List;

/**
 * Created by fengkun on 16/8/5.
 */
public interface IPickRpcService {
    public void calcPickOrder(List<WaveDetail> pickDetails);
}
