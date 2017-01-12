package com.lsh.wms.core.service.kanban;


import com.lsh.wms.core.constant.PoConstant;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.dao.po.IbdDetailDao;
import com.lsh.wms.core.dao.po.IbdHeaderDao;
import com.lsh.wms.core.dao.so.ObdHeaderDao;
import com.lsh.wms.core.dao.task.TaskInfoDao;
import com.lsh.wms.core.dao.wave.WaveHeadDao;
import com.lsh.wms.core.service.wave.WaveService;
import com.lsh.wms.model.po.IbdHeader;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.model.wave.WaveDetail;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by lixin-mac on 16/8/26.
 */
@Component
@Transactional(readOnly = true)
public class KanBanService {
    @Autowired
    private TaskInfoDao taskInfoDao;

    @Autowired
    private IbdHeaderDao ibdHeaderDao;

    @Autowired
    private IbdDetailDao ibdDetailDao;

    @Autowired
    private ObdHeaderDao obdHeaderDao;

    @Autowired
    private WaveHeadDao waveHeadDao;
    @Autowired
    private WaveService waveService;


    public List<Map<String,Object>> getKanBanCount(Long type){

        return taskInfoDao.getKanBanCount(type);
    }

    public  List<Map<String,Object>> getKanBanCountNew(Long type,Long subType){
        Map<String,Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("type",type);
        mapQuery.put("subType",subType);
        List<TaskInfo> taskInfos = taskInfoDao.getKanBanCountNew(mapQuery);
        List<WaveDetail> waveDetails = waveService.getWaveDetails(new HashMap<String, Object>());

        List<Map<String,Object>> resultList = new ArrayList<Map<String, Object>>();
        List<Long> taskId1s = new ArrayList<Long>();
        List<Long> taskId2s = new ArrayList<Long>();
        List<Long> taskId4s = new ArrayList<Long>();
        Set<Long> containerIds1 = new HashSet<Long>();
        Set<Long> containerIds2 = new HashSet<Long>();
        Set<Long> containerIds4 = new HashSet<Long>();

        for (TaskInfo taskInfo : taskInfos) {
            //1待拣货,先将id统计
            if(taskInfo.getStatus() == 1){
                taskId1s.add(taskInfo.getTaskId());
                containerIds1.add(taskInfo.getContainerId());
            }else if(taskInfo.getStatus() == 2){
                taskId2s.add(taskInfo.getTaskId());
                containerIds2.add(taskInfo.getContainerId());
            }else if(taskInfo.getStatus() == 4){
                taskId4s.add(taskInfo.getTaskId());
                containerIds4.add(taskInfo.getContainerId());
            }
        }
        //统计箱数与商品数
        BigDecimal sumQty1 = BigDecimal.ZERO;
        BigDecimal sumQty2 = BigDecimal.ZERO;
        BigDecimal sumQty4 = BigDecimal.ZERO;

        BigDecimal packSum1 = BigDecimal.ZERO;
        BigDecimal packSum2 = BigDecimal.ZERO;
        BigDecimal packSum4 = BigDecimal.ZERO;
        Set<Long> skuCode1 = new HashSet<Long>();
        Set<Long> skuCode2 = new HashSet<Long>();
        Set<Long> skuCode4 = new HashSet<Long>();

        for (WaveDetail waveDetail : waveDetails) {
            if(taskId1s != null && taskId1s.contains(waveDetail.getPickTaskId())){
                skuCode1.add(waveDetail.getItemId());
                sumQty1 = sumQty1.add(waveDetail.getAllocQty());
                packSum1 = packSum1.add(waveDetail.getAllocUnitQty());
            }else if(taskId2s != null && taskId2s.contains(waveDetail.getPickTaskId())){
                skuCode2.add(waveDetail.getItemId());
                sumQty2 = sumQty2.add(waveDetail.getAllocQty());
                packSum2 = packSum2.add(waveDetail.getAllocUnitQty());
            }else if(taskId4s != null && taskId4s.contains(waveDetail.getPickTaskId())){
                skuCode4.add(waveDetail.getItemId());
                sumQty4 = sumQty4.add(waveDetail.getAllocQty());
                packSum4 = packSum4.add(waveDetail.getAllocUnitQty());
            }
        }
        Map<String,Object> map1 = new HashMap<String, Object>();
        map1.put("status",1);
        map1.put("qtyNum" , sumQty1);
        map1.put("packNum" , packSum1);
        map1.put("containerNum",containerIds1.size());
        map1.put("taskNum",taskId1s.size());
        map1.put("skuCount",skuCode1.size());
        Map<String,Object> map2 = new HashMap<String, Object>();
        map2.put("status",2);
        map2.put("packNum",packSum2);
        map2.put("qtyNum" , sumQty2);
        map2.put("containerNum",containerIds2.size());
        map2.put("taskNum",taskId2s.size());
        map2.put("skuCount",skuCode2.size());
        Map<String,Object> map4 = new HashMap<String, Object>();
        map4.put("status",4);
        map4.put("packNum",packSum4);
        map4.put("qtyNum" , sumQty4);
        map4.put("containerNum",containerIds4.size());
        map4.put("taskNum",taskId4s.size());
        map4.put("skuCount",skuCode4.size());
        resultList.add(map1);
        resultList.add(map2);
        resultList.add(map4);

        return resultList;
    }

    public List<Map<String,Object>> getKanBanCountByStatus(Long type){
        return taskInfoDao.getKanBanCountByStatus(type);
    }



    public List<Map<String, Object>> getPoKanBanCount(Long orderType){
        return ibdHeaderDao.getPoKanBanCount(orderType);
    }

    public List<Map<String, Object>> getPoDetailKanBanCount(Long orderType){
        List<IbdHeader> headers = ibdHeaderDao.getPoDayCount(orderType);
        Map<Long,Integer> fMap = new HashMap<Long, Integer>();
        BigDecimal sumQty = null; //new BigDecimal(0);

        Map<String,Object> newMap = new HashMap<String, Object>();

        for(int i=0;i<headers.size();i++){
            Long orderId = headers.get(i).getOrderId();
            Integer orderStatus = headers.get(i).getOrderStatus();
            fMap.put(orderId,orderStatus);
        }
        ArrayList<Long> orderIds1 = valueGetKey(fMap, PoConstant.ORDER_THROW);//待收货
        ArrayList<Long> orderIds2 = valueGetKey(fMap,PoConstant.ORDER_RECTIPTING);//收货中
        ArrayList<Long> orderIds3 = valueGetKey(fMap,PoConstant.ORDER_RECTIPT_PART);//部分收货
        ArrayList<Long> orderIds4 = valueGetKey(fMap,PoConstant.ORDER_RECTIPT_ALL);//已收货
        //待收货商品总量
        BigDecimal sumQty1 = new BigDecimal(0);
        for(Long orderId : orderIds1){
            sumQty1 = sumQty1.add(ibdDetailDao.getInbPoDetailCountByOrderId(orderId));
        }
        //收货中商品总量
        BigDecimal sumQty2 = new BigDecimal(0);
        for(Long orderId : orderIds2){
            sumQty2 = sumQty2.add(ibdDetailDao.getInbPoDetailCountByOrderId(orderId));
        }
        //部分收货商品总量
        BigDecimal sumQty3 = new BigDecimal(0);
        for(Long orderId : orderIds3){
            sumQty3 = sumQty3.add(ibdDetailDao.getInbPoDetailCountByOrderId(orderId));
        }
        //已收货商品总量
        BigDecimal sumQty4 = new BigDecimal(0);
        for(Long orderId : orderIds4){
            sumQty4 = sumQty4.add(ibdDetailDao.getInbPoDetailCountByOrderId(orderId));
        }

        //商品总量
        sumQty = sumQty1.add(sumQty2).add(sumQty3).add(sumQty4);

        newMap.put("sumQty",sumQty);
        newMap.put("throwQty1",sumQty1);
        newMap.put("rectiptingQty2",sumQty2);
        newMap.put("partQty3",sumQty3);
        newMap.put("allQty4",sumQty4);

        List<Map<String, Object>> newList = new ArrayList<Map<String, Object>>();
        newList.add(newMap);
        return newList;

    }


    private static ArrayList<Long> valueGetKey(Map map,Integer value) {
        Set set = map.entrySet();
        ArrayList<Long> arr = new ArrayList<Long>();
        Iterator it = set.iterator();
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            if(entry.getValue().equals(value)) {
                Long s = (Long)entry.getKey();
                arr.add(s);
            }
        }
        return arr;
    }


    public List<Map<String, Object>> getSoKanBanCount(Long orderType){
        return obdHeaderDao.getSoKanBanCount(orderType);
    }

    public List<Map<String, Object>> getWaveKanBanCount(){
        return waveHeadDao.getWaveKanBanCount();
    }

}
