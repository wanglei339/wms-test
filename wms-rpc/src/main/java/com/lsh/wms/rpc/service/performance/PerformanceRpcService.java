package com.lsh.wms.rpc.service.performance;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.api.service.performance.IPerformanceRpcService;
import com.lsh.wms.core.service.performance.PerformanceService;
import com.lsh.wms.model.task.TaskInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * Created by lixin-mac on 16/8/24.
 */
@Service(protocol = "dubbo")
public class PerformanceRpcService implements IPerformanceRpcService {
    @Autowired
    private PerformanceService performanceService;

    public List<Map<String, Object>> getPerformance(Map<String, Object> condition) throws BizCheckedException {
        return performanceService.getPerformance(condition);
    }

    public List<TaskInfo> getPerformaceDetaile(Map<String, Object> mapQuery) {
        return performanceService.getPerformaceDetaile(mapQuery);
    }

    public Integer getPerformanceCount(Map<String, Object> mapQuery){
        return performanceService.getPerformanceCount(mapQuery);
    }
}
