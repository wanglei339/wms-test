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
        List<WaveDetail> pickWaveDetailList = new ArrayList<WaveDetail>();
        if(pickTaskIdList != null && pickTaskIdList.size() > 0){
            Map<String,Object> waveMap = new HashMap<String, Object>();
            waveMap.put("pickTaskIds",pickTaskIdList);
            pickWaveDetailList = waveService.getWaveDetails(waveMap);
        }
        List<WaveDetail> qcWaveDetailList = new ArrayList<WaveDetail>();
       if(qcTaskIdList != null && qcTaskIdList.size() >0){
           Map<String,Object> waveMap = new HashMap<String, Object>();
           waveMap.put("qcTaskIds",qcTaskIdList);
           qcWaveDetailList = waveService.getWaveDetails(waveMap);
       }
        //改为list
        //Map<Long,Set<Long>> itemSetByTaskId = new HashMap<Long, Set<Long>>();
        Map<Long,List<Long>> itemListByTaskId = new HashMap<Long, List<Long>>();
        //统计每个拣货任务中的商品数
        for (WaveDetail waveDetail : pickWaveDetailList) {
            Long taskId = waveDetail.getPickTaskId();
            if(itemListByTaskId.get(taskId) == null){
                itemListByTaskId.put(taskId,new ArrayList<Long>());
            }

            List<Long> itemList = itemListByTaskId.get(taskId);
            itemList.add(waveDetail.getItemId());
            itemListByTaskId.put(taskId,itemList);
        }
        //统计每个QC任务中的商品数
        for (WaveDetail waveDetail : qcWaveDetailList) {
            Long taskId = waveDetail.getQcTaskId();
            if(itemListByTaskId.get(taskId) == null){
                itemListByTaskId.put(taskId,new ArrayList<Long>());
            }
            List<Long> itemList = itemListByTaskId.get(taskId);
            itemList.add(waveDetail.getItemId());
            itemListByTaskId.put(taskId,itemList);
        }
        if(itemListByTaskId.size() > 0){
            //将商品数匹配到每条绩效记录中
            for (Map<String,Object> map : taskInfoList) {
                String taskInfos = map.get("taskIds").toString();
                String taskInfoArr [] = taskInfos.split(",");
                Long type = (Long) map.get("type");
                if(type != TaskConstant.TYPE_QC && type != TaskConstant.TYPE_PICK) {
                    continue;
                }
                List<Long> itemSet = new ArrayList<Long>();//统计每条绩效的商品sku数
                for(String taskIds : taskInfoArr){
                    Long taskId = Long.parseLong(taskIds);
                    if(itemListByTaskId.get(taskId) != null){
                        itemSet.addAll(itemListByTaskId.get(taskId));
                    }
                }
                map.put("skuCount",itemSet.size());
            }
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
