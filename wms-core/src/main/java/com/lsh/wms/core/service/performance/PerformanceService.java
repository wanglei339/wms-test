package com.lsh.wms.core.service.performance;

import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.dao.task.TaskInfoDao;
import com.lsh.wms.core.service.wave.WaveService;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.model.wave.WaveDetail;
import org.apache.tools.ant.taskdefs.Javadoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by lixin-mac on 16/8/24.
 */
@Component
@Transactional(readOnly = true)
public class PerformanceService {

    @Autowired
    private TaskInfoDao taskInfoDao;
    @Autowired
    private WaveService waveService;


    public List<Map<String, Object>> getPerformance(Map<String, Object> condition) {
        List<Map<String, Object>> taskInfoList = taskInfoDao.getPerformance(condition);
        List<Map<String, Object>> newTaskInfoList = new ArrayList<Map<String, Object>>();
        for (Map<String,Object> map : taskInfoList){
            Long type = (Long) map.get("type");
            //拣货跟QC统计一个任务中商品的个数
            if(type == TaskConstant.TYPE_QC || type == TaskConstant.TYPE_PICK){
                Map<String,Object> mapQuery = new HashMap<String, Object>();
                mapQuery.put("operator",map.get("uid"));
                mapQuery.put("date",map.get("date"));
                mapQuery.put("type",map.get("type"));
                mapQuery.put("subType",map.get("sub_type"));
                List<TaskInfo> taskInfos = taskInfoDao.getTaskInfoList(mapQuery);
                Set<Long> itemSet = new HashSet<Long>();
                int skuCount = 1;
                for (TaskInfo taskInfo : taskInfos){
                    Long taskId = taskInfo.getTaskId();
                    Map<String,Object> waveMap = new HashMap<String, Object>();
                    if(taskInfo.getType() == TaskConstant.TYPE_PICK) {
                        waveMap.put("pickTaskId",taskId);
                    }else{
                        waveMap.put("qcTaskId",taskId);
                    }
                    List<WaveDetail> waveDetails = waveService.getWaveDetails(waveMap);
                    if(waveDetails == null || waveDetails.size() <= 0){
                        skuCount = 1;
                    }else{
                        for (WaveDetail waveDetail : waveDetails) {
                            itemSet.add(waveDetail.getItemId());
                        }
                        skuCount = itemSet.size();
                    }
                }
                map.put("skuCount",skuCount);
            }else{
                map.put("skuCount" ,1);
            }
            newTaskInfoList.add(map);
        }
        //return taskInfoList;
        return newTaskInfoList;
    }

    //获取总数
    public Integer getPerformanceCount(Map<String, Object> condition){

        return taskInfoDao.getPerformanceCount(condition);
    }

    public List<TaskInfo> getPerformaceDetaile(Map<String,Object> mapQuery){
        // 根据收货类型,日期,员工确定明细
        List<TaskInfo> taskInfos = taskInfoDao.getTaskInfoList(mapQuery);
        return taskInfos;
    }

    public Set<Long> getItemSet(){
        return null;
    }


}
