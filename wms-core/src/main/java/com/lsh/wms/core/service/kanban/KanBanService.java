package com.lsh.wms.core.service.kanban;


import com.lsh.wms.core.constant.PoConstant;
import com.lsh.wms.core.dao.po.InbPoDetailDao;
import com.lsh.wms.core.dao.po.InbPoHeaderDao;
import com.lsh.wms.core.dao.task.TaskInfoDao;
import com.lsh.wms.model.po.InbPoDetail;
import com.lsh.wms.model.po.InbPoHeader;
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
    private InbPoHeaderDao inbPoHeaderDao;

    @Autowired
    private InbPoDetailDao inbPoDetailDao;


    public List<Map<String,Object>> getKanBanCount(Long type){
        return taskInfoDao.getKanBanCount(type);
    }

    public List<Map<String, Object>> getPoKanBanCount(Long orderType){
        return inbPoHeaderDao.getPoKanBanCount(orderType);
    }

    public List<Map<String, Object>> getPoDetailKanBanCount(Long orderType){
        List<InbPoHeader> headers = inbPoHeaderDao.getPoDayCount(orderType);
        Map<Long,Integer> fMap = new HashMap<Long, Integer>();
        BigDecimal sumQty = new BigDecimal(0);

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
            sumQty1 = sumQty1.add(inbPoDetailDao.getInbPoDetailCountByOrderId(orderId));
        }
        //收货中商品总量
        BigDecimal sumQty2 = new BigDecimal(0);
        for(Long orderId : orderIds2){
            sumQty2 = sumQty2.add(inbPoDetailDao.getInbPoDetailCountByOrderId(orderId));
        }
        //部分收货商品总量
        BigDecimal sumQty3 = new BigDecimal(0);
        for(Long orderId : orderIds3){
            sumQty3 = sumQty3.add(inbPoDetailDao.getInbPoDetailCountByOrderId(orderId));
        }
        //已收货商品总量
        BigDecimal sumQty4 = new BigDecimal(0);
        for(Long orderId : orderIds4){
            sumQty4 = sumQty4.add(inbPoDetailDao.getInbPoDetailCountByOrderId(orderId));
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


}
