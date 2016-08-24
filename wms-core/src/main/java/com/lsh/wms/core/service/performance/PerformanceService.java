package com.lsh.wms.core.service.performance;

import com.lsh.wms.core.dao.task.TaskInfoDao;
import com.lsh.wms.model.task.TaskInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by lixin-mac on 16/8/24.
 */
@Component
@Transactional(readOnly = true)
public class PerformanceService {

    @Autowired
    private TaskInfoDao taskInfoDao;


    public List<Map<String, Object>> getPerformance(Map<String, Object> condition) {
        List<Map<String, Object>> taskInfoList = taskInfoDao.getPerformance(condition);
        return taskInfoList;
    }

    //获取总数
    public Integer getPerformanceCount(Map<String, Object> condition){

        return taskInfoDao.getPerformanceCount(condition);
    }

    public List<TaskInfo> getPerformaceDetaile(Map<String,Object> mapQuery){
        // 根据收货类型,日期,员工确定明细
        List<TaskInfo> taskInfos = taskInfoDao.getTaskInfoList(mapQuery);
        return taskInfos;
    };


}
