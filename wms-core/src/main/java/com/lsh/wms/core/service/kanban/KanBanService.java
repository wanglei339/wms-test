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
        resultList.add(this.getKanBanByStatus(taskInfos,waveDetails,1L));
        resultList.add(this.getKanBanByStatus(taskInfos,waveDetails,2L));
        resultList.add(this.getKanBanByStatus(taskInfos,waveDetails,4L));
        return resultList;
    }

    public Map<String,Object> getKanBanByStatus(List<TaskInfo> taskInfos,List<WaveDetail> waveDetails,Long status){
        Map<String,Object> map = new HashMap<String, Object>();
        Set<Long> containerIds = new HashSet<Long>();
        List<Long> taskIds = new ArrayList<Long>();
        for (TaskInfo taskInfo : taskInfos) {
            //1待拣货,先将id统计
            if(taskInfo.getStatus() == status){
                taskIds.add(taskInfo.getTaskId());
                containerIds.add(taskInfo.getContainerId());
            }
        }
        //统计箱数与商品数
        BigDecimal sumQty = BigDecimal.ZERO;
        BigDecimal packSum = BigDecimal.ZERO;
        Set<Long> skuCode = new HashSet<Long>();
        for (WaveDetail waveDetail : waveDetails) {
            if(taskIds != null && taskIds.contains(waveDetail.getPickTaskId())){
                skuCode.add(waveDetail.getItemId());
                sumQty = sumQty.add(waveDetail.getAllocQty());
                packSum = packSum.add(waveDetail.getAllocUnitQty());
            }
        }
        map.put("status",status);
        map.put("qtyNum" , sumQty);
        map.put("packNum" , packSum);
        map.put("containerNum",containerIds.size());
        map.put("taskNum",taskIds.size());
        map.put("skuCount",skuCode.size());
        return map;

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
