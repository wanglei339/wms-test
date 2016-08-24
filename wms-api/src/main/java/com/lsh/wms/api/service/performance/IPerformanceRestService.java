package com.lsh.wms.api.service.performance;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.model.task.TaskInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by lixin-mac on 16/8/24.
 */
public interface IPerformanceRestService {

    String getPerformance(Map<String, Object> condition) throws BizCheckedException;

    String getPerformaceDetaile(Map<String,Object> mapQuery);
    String getPerformanceCount(Map<String, Object> mapQuery) throws BizCheckedException;
}
