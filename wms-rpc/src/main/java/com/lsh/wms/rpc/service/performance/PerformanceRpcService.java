package com.lsh.wms.rpc.service.performance;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.utils.BeanMapTransUtils;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.api.service.performance.IPerformanceRpcService;
import com.lsh.wms.core.service.performance.PerformanceService;
import com.lsh.wms.model.system.StaffPerformance;
import com.lsh.wms.model.task.TaskInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.*;

/**
 * Created by lixin-mac on 16/8/24.
 */
@Service(protocol = "dubbo")
public class PerformanceRpcService implements IPerformanceRpcService {
    @Autowired
    private PerformanceService performanceService;

    public List<StaffPerformance> getPerformance(Map<String, Object> condition) throws BizCheckedException {
        System.out.print("#####firstStart #####" + DateUtils.getCurrentSeconds());
        List<StaffPerformance> list  = performanceService.getStaffPerformance(condition);
        System.out.print("#####firstEnd #####" + DateUtils.getCurrentSeconds());
        return list;
    }

    public void createPerformance(Map<String, Object> condition) throws BizCheckedException {
        if(condition != null && condition.containsKey("startDate") && condition.containsKey("endDate") ){

        }else{
            condition = new HashMap<String, Object>();
            Calendar cal   =   Calendar.getInstance();
            cal.add(Calendar.DATE,   -1);
            DateUtils.getCurrentSeconds();
            String yesterdayStr = DateUtils.FORMAT_DATE_WITH_BAR.format(cal.getTime());
            try {
                Date yesterDay = DateUtils.FORMAT_DATE_WITH_BAR.parse(yesterdayStr);
                condition.put("startDate",yesterDay.getTime()/1000);
                condition.put("endDate",yesterDay.getTime()/1000);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        performanceService.createPerformance(condition);
    }


    public List<TaskInfo> getPerformaceDetaile(Map<String, Object> mapQuery) {
        return performanceService.getPerformaceDetaile(mapQuery);
    }

    public Integer getPerformanceCount(Map<String, Object> mapQuery){
        return performanceService.getPerformanceCount(mapQuery);
    }

    public TaskInfo getTaskInfo(Map<String, Object> mapQuery){
        List<Map<String, Object>> lists = performanceService.getPerformance(mapQuery);
        Map<String, Object> map = new HashMap<String, Object>();
        if(lists.size() > 0){
            map = lists.get(0);
        }
        TaskInfo taskInfo = BeanMapTransUtils.map2Bean(map,TaskInfo.class);
        return  taskInfo;
    }
}
