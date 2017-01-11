package com.lsh.wms.core.service.performance;

import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.dao.task.TaskInfoDao;
import com.lsh.wms.core.service.wave.WaveService;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.model.wave.WaveDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(PerformanceService.class);

    @Autowired
    private TaskInfoDao taskInfoDao;
    @Autowired
    private WaveService waveService;


    /*public List<Map<String, Object>> getPerformance(Map<String, Object> condition) {
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
    }*/



    public List<Map<String, Object>> getPerformance(Map<String, Object> condition) {
        List<Map<String, Object>> taskInfoList = taskInfoDao.getPerformance(condition);
        List<Map<String, Object>> newTaskInfoList = new ArrayList<Map<String, Object>>();
        List<Long> pickTaskIdList = new ArrayList<Long>();
        List<Long> qcTaskIdList = new ArrayList<Long>();
        //获取任务中所有的拣货任务ID和QC任务ID
        for (Map<String,Object> map : taskInfoList){
            Long type = (Long) map.get("type");
            if(type == TaskConstant.TYPE_QC || type == TaskConstant.TYPE_PICK) {
                //拣货跟QC统计一个任务中商品的个数
                String taskInfos = map.get("taskIds").toString();
                logger.info("[getPerformance]" + taskInfos);
                String taskInfoArr [] = taskInfos.split(",");
                for (String taskInfoIds : taskInfoArr){
                    Long taskId = Long.parseLong(taskInfoIds);
                    if(type == TaskConstant.TYPE_PICK) {
                        pickTaskIdList.add(taskId);
                    }else{
                        qcTaskIdList.add(taskId);
                    }
                }
            }
            map.put("skuCount",1);
            newTaskInfoList.add(map);
        }

        Map<String,Object> waveMap = new HashMap<String, Object>();
        waveMap.put("pickTaskIds",pickTaskIdList);
        List<WaveDetail> pickWaveDetailList = waveService.getWaveDetails(waveMap);
        waveMap = new HashMap<String, Object>();
        waveMap.put("qcTaskIds",qcTaskIdList);
        List<WaveDetail> qcWaveDetailList = waveService.getWaveDetails(waveMap);
        Map<Long,Set<Long>> itemSetByTaskId = new HashMap<Long, Set<Long>>();
        //统计每个拣货任务中的商品数
        for (WaveDetail waveDetail : pickWaveDetailList) {
            Long taskId = waveDetail.getPickTaskId();
            if(itemSetByTaskId.get(taskId) == null){
                itemSetByTaskId.put(taskId,new HashSet<Long>());
            }
            Set<Long> itemSet = itemSetByTaskId.get(taskId);
            itemSet.add(waveDetail.getItemId());
            itemSetByTaskId.put(taskId,itemSet);
        }
        //统计每个QC任务中的商品数
        for (WaveDetail waveDetail : qcWaveDetailList) {
            Long taskId = waveDetail.getQcTaskId();
            if(itemSetByTaskId.get(taskId) == null){
                itemSetByTaskId.put(taskId,new HashSet<Long>());
            }
            Set<Long> itemSet = itemSetByTaskId.get(taskId);
            itemSet.add(waveDetail.getItemId());
            itemSetByTaskId.put(taskId,itemSet);
        }
        //将商品数匹配到每条绩效记录中
        for (Map<String,Object> map : taskInfoList) {
            String taskInfos = map.get("taskIds").toString();
            String taskInfoArr [] = taskInfos.split(",");
            Long type = (Long) map.get("type");
            if(type != TaskConstant.TYPE_QC && type != TaskConstant.TYPE_PICK) {
                continue;
            }
            Set<Long> itemSet = new HashSet<Long>();//统计每条绩效的商品sku数
            for(String taskIds : taskInfoArr){
                Long taskId = Long.parseLong(taskIds);
                if(itemSetByTaskId.get(taskId) != null){
                    itemSet.addAll(itemSetByTaskId.get(taskId));
                }
            }
            map.put("skuCount",itemSet.size());
        }
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
