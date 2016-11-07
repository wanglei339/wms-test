package com.lsh.wms.api.service.tu;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.model.wave.WaveDetail;

import java.util.Map;

/**
 * Created by zhanghongling on 16/11/4.
 */
public interface ITuOrdersRpcService {
    public Map<String,Object> getTuOrdersList(String tuId) throws BizCheckedException;
    public Map<String,Object> getDeliveryOrdersList(String tuId) throws BizCheckedException;
    public Map<String,Object> getSendCarOrdersList(String tuId) throws BizCheckedException;
    public TaskInfo getTaskInfoByWaveDetail(WaveDetail waveDetail) throws BizCheckedException;



}
