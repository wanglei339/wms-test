package com.lsh.wms.api.service.pick;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.model.wave.WaveDetail;

import java.util.List;

/**
 * Created by fengkun on 16/8/5.
 */
public interface IPickRpcService {
    public List<WaveDetail> calcPickOrder(List<WaveDetail> pickDetails);
}
