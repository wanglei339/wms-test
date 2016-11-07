package com.lsh.wms.service.tu;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.api.service.tu.ITuOrdersRpcService;
import com.lsh.wms.api.service.tu.ITuRpcService;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.so.SoDeliveryService;
import com.lsh.wms.core.service.so.SoOrderService;
import com.lsh.wms.core.service.store.StoreService;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.core.service.wave.WaveService;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.baseinfo.BaseinfoStore;
import com.lsh.wms.model.so.ObdHeader;
import com.lsh.wms.model.so.OutbDeliveryDetail;
import com.lsh.wms.model.so.OutbDeliveryHeader;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.model.tu.TuDetail;
import com.lsh.wms.model.tu.TuHead;
import com.lsh.wms.model.wave.WaveDetail;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by zhanghongling on 16/11/4.
 */
@Service(protocol = "dubbo")
public class TuOrdersRpcService implements ITuOrdersRpcService {
    @Reference
    private ITuRpcService iTuRpcService;
    @Autowired
    private StoreService storeService;
    @Autowired
    private SoDeliveryService soDeliveryService;
    @Autowired
    private SoOrderService soOrderService;
    @Autowired
    private WaveService waveService;
    @Autowired
    private BaseTaskService baseTaskService;
    @Autowired
    private ItemService itemService;

    public Map<String,Object> getTuOrdersList(String tuId) throws BizCheckedException {
        //根据运单号获取发货信息
        TuHead tuHead =  iTuRpcService.getHeadByTuId(tuId);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("transPlan",tuId);

        //根据运单号,获取发货单列表
        List<OutbDeliveryHeader> outbDeliveryHeaderList = soDeliveryService.getOutbDeliveryHeaderList(params);
        if(outbDeliveryHeaderList == null || outbDeliveryHeaderList.size() == 0){
            throw  new BizCheckedException("2990022");
        }
        Set<Long> deliveryIdSet = new HashSet<Long>();
        for (OutbDeliveryHeader oudh : outbDeliveryHeaderList){
            deliveryIdSet.add(oudh.getDeliveryId());
        }

        //根据发货单id,获取订单id
        List<Long> deliveryIdList = new ArrayList<Long>();
        deliveryIdList.addAll(deliveryIdSet);
        List<OutbDeliveryDetail>  outbDeliveryDetailList = soDeliveryService.getOutbDeliveryDetailList(deliveryIdList);
        //店铺no集合
        Set<String> storeNoSet = new HashSet<String>();
        Map<String,Set<Long>> storeNoToDeliveryId = new HashMap<String, Set<Long>>();
        for(OutbDeliveryDetail oudd : outbDeliveryDetailList){


            //根据订单ID获取店铺ID
            ObdHeader obdHeader = soOrderService.getOutbSoHeaderByOrderId(oudd.getOrderId());
            if(obdHeader == null){
               continue;
            }
            String storeNo = obdHeader.getDeliveryCode();

            //店铺集合
            storeNoSet.add(storeNo);

            //封装店铺和发货单映射关系
            if(storeNoToDeliveryId.get(storeNo) == null){
                storeNoToDeliveryId.put(storeNo,new HashSet<Long>());
            }
            storeNoToDeliveryId.get(storeNo).add(oudd.getDeliveryId());

        }

        //获取并封装店铺信息
        Map<String,Map<String, Object>> storeInfoMap = new HashMap<String, Map<String, Object>>();
        for (String storeNo : storeNoSet) {
            List<BaseinfoStore> baseinfoStore = storeService.getStoreIdByCode(storeNo);
            Map<String, Object> storeMap = new HashMap<String, Object>();
            storeMap.put("storeNo", storeNo);
            String storeId = "";
            String storeName = "";
            if(baseinfoStore != null || baseinfoStore.size() > 0){
                storeId = String.valueOf(baseinfoStore.get(0).getStoreId());
                storeName = baseinfoStore.get(0).getStoreName();
            }


            storeMap.put("storeId",storeId);
            storeMap.put("storeName",storeName);
            storeInfoMap.put(storeId,storeMap);
        }

        List<TuDetail> tuDetailList = iTuRpcService.getTuDeailListByTuId(tuId);
        //统计店铺其他数据
        Map<String,Map<String, BigDecimal>> storeInfoCountMap = new HashMap<String, Map<String, BigDecimal>>();
        for (TuDetail td : tuDetailList) {
            String storeId = String.valueOf(td.getStoreId());
            if(storeInfoCountMap.get(storeId) == null){
                Map<String,BigDecimal> countInitMap = new HashMap<String, BigDecimal>();
                countInitMap.put("boxNum",BigDecimal.ZERO);//箱数
                countInitMap.put("turnBoxNum",BigDecimal.ZERO);//周转箱数
                countInitMap.put("containerNum",BigDecimal.ZERO);//板数
                storeInfoCountMap.put(storeId,countInitMap);
            }
            Map<String, BigDecimal> countMap = storeInfoCountMap.get(storeId);
            countMap.put("boxNum",countMap.get("boxNum").add(td.getBoxNum()));
            countMap.put("turnBoxNum",countMap.get("turnBoxNum").add(BigDecimal.valueOf(td.getTurnoverBoxNum())));
            countMap.put("containerNum",countMap.get("containerNum").add(BigDecimal.ONE));//有一条记录,板数加1
        }
        //合并统计数据,重新封装店铺信息
        BigDecimal boxNumTotal = BigDecimal.ZERO;
        BigDecimal turnBoxNumTotal = BigDecimal.ZERO;
        BigDecimal containerNumTotal = BigDecimal.ZERO;
        for (String storeId : storeInfoMap.keySet()) {
            BigDecimal boxNum = BigDecimal.ZERO;
            BigDecimal turnBoxNum = BigDecimal.ZERO;
            BigDecimal containerNum = BigDecimal.ZERO;
            if(storeInfoCountMap.get(storeId) != null) {
                boxNum = storeInfoCountMap.get(storeId).get("boxNum");
                turnBoxNum = storeInfoCountMap.get(storeId).get("turnBoxNum");
                containerNum = storeInfoCountMap.get(storeId).get("containerNum");

                boxNumTotal = boxNumTotal.add(boxNum);
                turnBoxNumTotal = turnBoxNumTotal.add(turnBoxNum);
                containerNumTotal = containerNumTotal.add(containerNum);
            }
            storeInfoMap.get(storeId).put("boxNum",boxNum);//箱数
            storeInfoMap.get(storeId).put("turnBoxNum",turnBoxNum);//周转箱数
            storeInfoMap.get(storeId).put("containerNum", containerNum);//板数

        }
        Map<String, Object> totalMap = new HashMap<String, Object>();
        totalMap.put("boxNum",boxNumTotal);//总箱数
        totalMap.put("turnBoxNum",turnBoxNumTotal);//总周转箱数
        totalMap.put("containerNum", containerNumTotal);//总板数
        storeInfoMap.put("total",totalMap);

        //封装返回数据
        Map<String,Object> returnData = new HashMap<String, Object>();
        returnData.put("tuId",tuId);//运单号
        returnData.put("printDate", DateUtils.FORMAT_DATE_WITH_BAR.format(new Date()));//打印日期
        returnData.put("warehouseId",tuHead.getWarehouseId());//供货仓库
        returnData.put("companyName",tuHead.getCompanyName());//承运商
        returnData.put("name",tuHead.getName());//司机姓名
        returnData.put("cellphone",tuHead.getCellphone());//司机电话
        returnData.put("storeCountInfo",storeInfoMap);//门店统计信息
        returnData.put("storeDeliveryList",storeNoToDeliveryId);//门店发货单号信息
        return returnData;
    }

    public Map<String,Object> getDeliveryOrdersList(String tuId) throws BizCheckedException{
        //根据运单号获取发货信息
        TuHead tuHead =  iTuRpcService.getHeadByTuId(tuId);

        //根据tuid获取containerId
        List<TuDetail>tuDetailList = iTuRpcService.getTuDeailListByTuId(tuId);

        //封装订单信息
        Map<Long,Map<String,Object>> orderGoodsInfoMap = new HashMap<Long,Map<String,Object>>();
        //封装订单的商品信息
        Map<Long,Map<Long,Map<String,Object>>> goodsListMap = new HashMap<Long,Map<Long,Map<String,Object>>>();

        //根据containerId获取waveDetaiList
        for(TuDetail td:tuDetailList){
            Long mergedContainerId = td.getMergedContainerId();
            Long storeId = td.getStoreId();

            Map<String,Object> params = new HashMap<String,Object>();
            params.put("mergedContainerId",mergedContainerId);
            List<WaveDetail> waveDetailList = waveService.getWaveDetailsByMergedContainerId(mergedContainerId);
            if(waveDetailList == null||waveDetailList.size() == 0){
                waveDetailList = waveService.getDetailsByContainerId(mergedContainerId);
            }

            for(WaveDetail wd:waveDetailList){
                Long orderId=wd.getOrderId();
                Long itemId = wd.getItemId();
                if(orderGoodsInfoMap.get(orderId) == null){
                    //封装订单信息
                    Map<String,Object> orderMap = new HashMap<String,Object>();
                    orderMap.put("orderId",orderId);//订单号
                    orderMap.put("printDate",new Date());//打印日期// FIXME: 16/11/7
                    orderMap.put("tuId",tuId);//运单号
                    orderMap.put("deliveryId",wd.getDeliveryId());//发货单号
                    orderMap.put("driverName",tuHead.getName());//司机姓名
                    orderMap.put("driverPhone",tuHead.getCellphone());//司机电话
                    orderMap.put("boxTotal",BigDecimal.ZERO);//装车总箱数
                    orderMap.put("transBoxTotal",BigDecimal.ZERO);//装车周转箱数

                    //获取店铺信息
                    BaseinfoStore baseinfoStore = storeService.getStoreByStoreId(storeId);
                    if(baseinfoStore == null){
                        orderMap.put("storeName","");//收货门店
                        orderMap.put("storePhone","");//联系电话
                        orderMap.put("storeAddress","");//收货地址
                    }else{
                        orderMap.put("storeName",baseinfoStore.getStoreName());//收货门店
                        orderMap.put("storePhone","");//联系电话// FIXME: 16/11/7
                        orderMap.put("storeAddress",baseinfoStore.getAddress());//收货地址
                    }

                    orderMap.put("goodsList",new HashMap<String, Object>());//订单商品信息

                    orderGoodsInfoMap.put(orderId,orderMap);

                }

                Long taskId = wd.getQcTaskId();
                TaskInfo taskInfo = baseTaskService.getTaskInfoById(taskId);

                //统计装车箱数
                BigDecimal packBoxNum = BigDecimal.valueOf(taskInfo.getExt3());
                BigDecimal oldBoxTotal = (BigDecimal)orderGoodsInfoMap.get(orderId).get("boxTotal");
                orderGoodsInfoMap.get(orderId).put("boxTotal",packBoxNum.add(oldBoxTotal));


                //统计周转箱数
                BigDecimal transBoxNum = taskInfo.getTaskPackQty();
                BigDecimal oldTransBoxTotal  = (BigDecimal)orderGoodsInfoMap.get(orderId).get("transBoxTotal");
                orderGoodsInfoMap.get(orderId).put("transBoxTotal",transBoxNum.add(oldTransBoxTotal));

                /*
                封装订单对应的商品信息
                 */
                if(goodsListMap.get(orderId) == null) {
                    goodsListMap.put(orderId, new HashMap<Long, Map<String, Object>>());//订单商品信息
                }
                if(goodsListMap.get(orderId).get(itemId) == null) {

                    BaseinfoItem item = itemService.getItem(itemId);
                    String goodsName = "";
                    if(item != null ){
                        goodsName = item.getSkuName();
                    }
                    //订单中,商品信息
                    Map<String, Object> goodsCountMap = new HashMap<String, Object>();
                    goodsCountMap.put("goodsName", goodsName);
                    goodsCountMap.put("boxNum", BigDecimal.ZERO);//箱数
                    goodsCountMap.put("eaNum", BigDecimal.ZERO);//件数
                    goodsCountMap.put("unitName", "EA");//箱规,默认EA
                    goodsListMap.get(orderId).put(itemId, goodsCountMap);
                }


                if(wd.getAllocUnitName().equals("EA")){
                    //统计散件数
                    String eaNumStr = goodsListMap.get(orderId).get(itemId).get("eaNum").toString();
                    BigDecimal eaNum = BigDecimal.valueOf(Double.parseDouble(eaNumStr)).add(wd.getQcQty());
                    goodsListMap.get(orderId).get(itemId).put("eaNum",eaNum);
                }else{
                    goodsListMap.get(orderId).get(itemId).put("unitName",wd.getAllocUnitName().substring(1,wd.getAllocUnitName().length()));//箱规
                    //统计箱数
                    String boxNumStr = goodsListMap.get(orderId).get(itemId).get("boxNum").toString();
                    BigDecimal boxNum = BigDecimal.valueOf(Double.parseDouble(boxNumStr)).add(wd.getQcQty());
                    goodsListMap.get(orderId).get(itemId).put("boxNum",boxNum);
                }


            }

        }
        //整合订单和商品数据
        for(Long orderId : orderGoodsInfoMap.keySet()){
            if(goodsListMap.get(orderId) != null){
                orderGoodsInfoMap.get(orderId).put("goodsList",goodsListMap.get(orderId));
            }

        }
        Map<String,Object> returnData = new HashMap<String, Object>();
        returnData.put("data",orderGoodsInfoMap);
        return returnData;
    }

}
