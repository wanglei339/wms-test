package com.lsh.wms.api.service.pick;

import com.lsh.base.common.exception.BizCheckedException;

import java.util.Map;

/**
 * Created by zengwenjun on 16/11/12.
 */
public interface IPCPickRestService {
    public String getPickTaskInfo(Map<String, Object> mapInput);

    public String getContainerExpensiveGoods(Long containerId, Long waveId) throws BizCheckedException;

}
