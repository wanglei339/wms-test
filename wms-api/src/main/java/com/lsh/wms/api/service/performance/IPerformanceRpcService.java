package com.lsh.wms.api.service.performance;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.model.system.StaffPerformance;
import com.lsh.wms.model.task.TaskInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by lixin-mac on 16/8/24.
 */
public interface IPerformanceRpcService {

    void createPerformance(Map<String, Object> condition) throws BizCheckedException;

    List<StaffPerformance> getPerformance(Map<String, Object> condition) throws BizCheckedException;

    List<TaskInfo> getPerformaceDetaile(Map<String,Object> mapQuery);

    Integer getPerformanceCount(Map<String, Object> mapQuery);

}
