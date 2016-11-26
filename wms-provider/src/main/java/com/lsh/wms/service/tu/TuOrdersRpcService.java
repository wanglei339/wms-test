package com.lsh.wms.service.tu;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.api.service.location.ILocationRpcService;
import com.lsh.wms.api.service.tu.ITuOrdersRpcService;
import com.lsh.wms.api.service.tu.ITuRpcService;
import com.lsh.wms.core.constant.ItemConstant;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.constant.TuConstant;
import com.lsh.wms.core.service.baseinfo.ItemTypeService;
import com.lsh.wms.core.service.csi.CsiCustomerService;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.so.SoDeliveryService;
import com.lsh.wms.core.service.so.SoOrderService;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.core.service.utils.PackUtil;
import com.lsh.wms.core.service.wave.WaveService;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.baseinfo.BaseinfoItemType;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.baseinfo.BaseinfoStore;
import com.lsh.wms.model.csi.CsiCustomer;
import com.lsh.wms.model.so.ObdHeader;
import com.lsh.wms.model.so.OutbDeliveryDetail;
import com.lsh.wms.model.so.OutbDeliveryHeader;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.model.tu.TuDetail;
import com.lsh.wms.model.tu.TuHead;
import com.lsh.wms.model.wave.WaveDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.config.Task;

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
    private SoDeliveryService soDeliveryService;
    @Autowired
    private SoOrderService soOrderService;
    @Autowired
    private WaveService waveService;
    @Autowired
    private BaseTaskService baseTaskService;
    @Reference
    private ILocationRpcService iLocationRpcService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private ItemTypeService itemTypeService;
    @Autowired
    private CsiCustomerService csiCustomerService;

    public Map<String, Object> getTuOrdersList(String tuId) throws BizCheckedException {
        //根据运单号获取发货信息
        TuHead tuHead = iTuRpcService.getHeadByTuId(tuId);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("transPlan", tuId);

        //根据运单号,获取发货单列表
        List<OutbDeliveryHeader> outbDeliveryHeaderList = soDeliveryService.getOutbDeliveryHeaderList(params);
        if (outbDeliveryHeaderList == null || outbDeliveryHeaderList.size() == 0) {
            throw new BizCheckedException("2990022");
        }
        Set<Long> deliveryIdSet = new HashSet<Long>();
        for (OutbDeliveryHeader oudh : outbDeliveryHeaderList) {
            deliveryIdSet.add(oudh.getDeliveryId());
        }

        //根据发货单id,获取订单id
        List<Long> deliveryIdList = new ArrayList<Long>();
        deliveryIdList.addAll(deliveryIdSet);
        List<OutbDeliveryDetail> outbDeliveryDetailList = soDeliveryService.getOutbDeliveryDetailList(deliveryIdList);
        //店铺no集合
        Set<String> storeNoSet = new HashSet<String>();
        Map<String, Set<Long>> storeNoToDeliveryId = new HashMap<String, Set<Long>>();
        Map<String, Long> storeNo2OwnerId = new HashMap<String, Long>();
        for (OutbDeliveryDetail oudd : outbDeliveryDetailList) {
            //根据订单ID获取店铺no
            ObdHeader obdHeader = soOrderService.getOutbSoHeaderByOrderId(oudd.getOrderId());
            if (obdHeader == null) {
                continue;
            }
            String storeNo = obdHeader.getDeliveryCode();

            //店铺集合
            storeNoSet.add(storeNo);

            //封装店铺和发货单映射关系
            if (storeNoToDeliveryId.get(storeNo) == null) {
                storeNoToDeliveryId.put(storeNo, new HashSet<Long>());
            }
            storeNoToDeliveryId.get(storeNo).add(oudd.getDeliveryId());
            storeNo2OwnerId.put(storeNo, obdHeader.getOwnerUid());
        }

        //获取并封装店铺信息
        //storeid key
        Map<String, Map<String, Object>> storeInfoMap = new HashMap<String, Map<String, Object>>();
        //封装门店发货单号
        List<Map<String, Object>> storeDeliveryList = new ArrayList<Map<String, Object>>();
        for (String storeNo : storeNoSet) {
            CsiCustomer customer = csiCustomerService.getCustomerByCustomerCode(storeNo);
            Map<String, Object> storeMap = new HashMap<String, Object>();
            storeMap.put("storeNo", storeNo);
            String storeId = "";
            String storeName = "";
            if (customer != null) {
                storeName = customer.getCustomerName();
                storeId = customer.getCustomerId() + "";
            }
            storeMap.put("storeId", storeId);
            storeMap.put("storeName", storeName);

            storeInfoMap.put(storeId, storeMap);

            //将店铺名称放入店铺发货单关系中
            Map<String, Object> temp = new HashMap<String, Object>();
            temp.put("storeName", storeName);
            temp.put("list", storeNoToDeliveryId.get(storeNo));
            //storeNo key
            Map<String, Object> storeDeliveryIdInfoMap = new HashMap<String, Object>();
            storeDeliveryIdInfoMap.put(storeNo, temp);
            storeDeliveryList.add(storeDeliveryIdInfoMap);
        }

        List<TuDetail> tuDetailList = iTuRpcService.getTuDeailListByTuId(tuId);
        //统计店铺其他数据 , 以店铺ID 进行统计
        Map<String, Map<String, BigDecimal>> storeInfoCountMap = new HashMap<String, Map<String, BigDecimal>>();
        for (TuDetail td : tuDetailList) {
            String storeId = String.valueOf(td.getStoreId());
            if (storeInfoCountMap.get(storeId) == null) {
                Map<String, BigDecimal> countInitMap = new HashMap<String, BigDecimal>();
                countInitMap.put("boxNum", BigDecimal.ZERO);//箱数
                countInitMap.put("turnBoxNum", BigDecimal.ZERO);//周转箱数
                countInitMap.put("containerNum", BigDecimal.ZERO);//板数
                storeInfoCountMap.put(storeId, countInitMap);
            }
            Map<String, BigDecimal> countMap = storeInfoCountMap.get(storeId);
            countMap.put("boxNum", countMap.get("boxNum").add(td.getBoxNum()));
            countMap.put("turnBoxNum", countMap.get("turnBoxNum").add(BigDecimal.valueOf(td.getTurnoverBoxNum())));
            countMap.put("containerNum", countMap.get("containerNum").add(BigDecimal.ONE));//有一条记录,板数加1
        }
        //合并统计数据,重新封装店铺信息
        BigDecimal boxNumTotal = BigDecimal.ZERO;
        BigDecimal turnBoxNumTotal = BigDecimal.ZERO;
        BigDecimal containerNumTotal = BigDecimal.ZERO;
        List<Object> storeCountInfoList = new ArrayList<Object>();
        for (String storeId : storeInfoMap.keySet()) {
            BigDecimal boxNum = BigDecimal.ZERO;
            BigDecimal turnBoxNum = BigDecimal.ZERO;
            BigDecimal containerNum = BigDecimal.ZERO;
            if (storeInfoCountMap.get(storeId) != null) {
                boxNum = storeInfoCountMap.get(storeId).get("boxNum");
                turnBoxNum = storeInfoCountMap.get(storeId).get("turnBoxNum");
                containerNum = storeInfoCountMap.get(storeId).get("containerNum");

                boxNumTotal = boxNumTotal.add(boxNum);
                turnBoxNumTotal = turnBoxNumTotal.add(turnBoxNum);
                containerNumTotal = containerNumTotal.add(containerNum);
            }
            storeInfoMap.get(storeId).put("boxNum", boxNum);//箱数
            storeInfoMap.get(storeId).put("turnBoxNum", turnBoxNum);//周转箱数
            storeInfoMap.get(storeId).put("containerNum", containerNum);//板数
            storeCountInfoList.add(storeInfoMap.get(storeId));
        }
        Map<String, Object> totalMap = new HashMap<String, Object>();
        totalMap.put("boxNum", boxNumTotal);//总箱数
        totalMap.put("turnBoxNum", turnBoxNumTotal);//总周转箱数
        totalMap.put("containerNum", containerNumTotal);//总板数


        //封装返回数据
        Map<String, Object> returnData = new HashMap<String, Object>();
        returnData.put("tuId", tuId);//运单号
        returnData.put("printDate", DateUtils.FORMAT_DATE_WITH_BAR.format(new Date()));//打印日期
        returnData.put("warehouseId", tuHead.getWarehouseId());//供货仓库
        returnData.put("companyName", tuHead.getCompanyName());//承运商
        returnData.put("name", tuHead.getName());//司机姓名
        returnData.put("cellphone", tuHead.getCellphone());//司机电话
        returnData.put("storeCountInfo", storeCountInfoList);//门店统计信息
        returnData.put("total", totalMap);
        returnData.put("storeDeliveryList", storeDeliveryList);//门店发货单号信息
        return returnData;
    }

    /**
     * 小店的派车单
     *
     * @param tuId
     * @return
     * @throws BizCheckedException
     */
    public Map<String, Object> getSendCarOrdersList(String tuId) throws BizCheckedException {
        //根据运单号获取发货信息
        TuHead tuHead = iTuRpcService.getHeadByTuId(tuId);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("tuId", tuHead.getTuId());
        params.put("name", tuHead.getName());
        params.put("carNumber", tuHead.getCarNumber());
        params.put("cellPhone", tuHead.getCellphone());
        params.put("printTime", DateUtils.FORMAT_DATE_WITH_BAR.format(new Date()));  //打印时间
        //总箱数
        BigDecimal totalPackCount = new BigDecimal("0.0000");
        Long totalTurnoverBoxCount = 0L;
        //周转箱数
        List<TuDetail> tuDetails = iTuRpcService.getTuDeailListByTuId(tuId);
        //门店信息集合
        Map<Long, Map<String, Object>> stores = new HashMap<Long, Map<String, Object>>();
        for (TuDetail tuDetail : tuDetails) {
            Long storeId = tuDetail.getStoreId();
            //查找托盘信息
//            List<WaveDetail> waveDetails = waveService.getDetailsByContainerId(tuDetail.getMergedContainerId());
//            if (null == waveDetails || waveDetails.size() < 1) {
//                throw new BizCheckedException("2990041");
//            }
//            WaveDetail detail = waveDetails.get(0);
//            TaskInfo qcInfo = this.getTaskInfoByWaveDetail(detail);
//            if (null == qcInfo) {
//                continue;   //不存在组盘未完成(不可能)
//            }
            //包含
            if (stores.containsKey(storeId)) {
                Map<String, Object> storeMap = stores.get(storeId);
                List<Map<String, Object>> containerList = (List<Map<String, Object>>) storeMap.get("containerList");
                Map<String, Object> container = new HashMap<String, Object>();
                container.put("containerId", tuDetail.getMergedContainerId());
                container.put("packCount", tuDetail.getBoxNum());
                container.put("turnoverBoxCount", tuDetail.getTurnoverBoxNum());
                container.put("isRest", tuDetail.getIsRest() == TuConstant.IS_REST); //余货(这个逻辑需要)
                container.put("isExpensive", tuDetail.getIsExpensive() == TuConstant.IS_EXPENSIVE); //余货(这个逻辑需要)
                containerList.add(container);
                //单门店总箱数
                BigDecimal storeTotalPackCount = (BigDecimal) storeMap.get("storeTotalPackCount");
                storeMap.put("storeTotalPackCount", storeTotalPackCount.add(tuDetail.getBoxNum()));
                //单门店总周转箱
                Long storeTotalTurnoverBoxCount = (Long) storeMap.get("storeTotalTurnoverBoxCount");
                storeMap.put("storeTotalTurnoverBoxCount", storeTotalTurnoverBoxCount + tuDetail.getTurnoverBoxNum());
            } else {
                //门店名,集货道list,门店id
                CsiCustomer customer = csiCustomerService.getCustomerByCustomerId(storeId);
                Map<String, Object> storeMap = new HashMap<String, Object>();
                storeMap.put("storeId", storeId);
                storeMap.put("storeName", customer.getCustomerName());
                // TODO: 16/11/16 修改店铺集货位
                //获取门店下所有的集货位
                //获取location的id
                if (null == customer) {
                    throw new BizCheckedException("2180023");
                }
                if (null == customer.getCollectRoadId()) {
                    throw new BizCheckedException("2180024");
                }
                BaseinfoLocation collectionLocation = iLocationRpcService.getLocation(customer.getCollectRoadId());

                if (collectionLocation != null) {
                    storeMap.put("collectionBins", collectionLocation.getLocationCode());
                } else {
                    storeMap.put("collectionBins", "");
                }
                //托盘箱数统计集合
                List<Map<String, Object>> containerList = new LinkedList<Map<String, Object>>();
                Map<String, Object> container = new HashMap<String, Object>();
                container.put("containerId", tuDetail.getMergedContainerId());
                container.put("packCount", tuDetail.getBoxNum());
                container.put("turnoverBoxCount", tuDetail.getTurnoverBoxNum());
                container.put("isRest", tuDetail.getIsRest() == TuConstant.IS_REST); //余货(这个逻辑需要)
                container.put("isExpensive", tuDetail.getIsExpensive() == TuConstant.IS_EXPENSIVE); //余货(这个逻辑需要)
                containerList.add(container);
                //托盘list
                storeMap.put("containerList", containerList);
                //门店总箱数,周转箱数
                storeMap.put("storeTotalPackCount", tuDetail.getBoxNum());
                storeMap.put("storeTotalTurnoverBoxCount", tuDetail.getTurnoverBoxNum());
                stores.put(storeId, storeMap);
            }
            //全运单总箱数,总周转箱数
            totalPackCount = totalPackCount.add(tuDetail.getBoxNum());
            totalTurnoverBoxCount = totalTurnoverBoxCount + tuDetail.getTurnoverBoxNum();

        }
        //放入list中
        List<Map<String, Object>> date = new ArrayList<Map<String, Object>>();
        //循环放入list
        for (Long key : stores.keySet()) {
            Map<String, Object> storeInfoMap = stores.get(key);
            date.add(storeInfoMap);
        }
//        params.put("stores", stores);
        params.put("stores", date);
        params.put("totalPackCount", totalPackCount);
        params.put("totalTurnoverBoxCount", totalTurnoverBoxCount);
        return params;
    }

    /**
     * 通过osd获取qctaskInfo的方法,task_info中的托盘有复用,用task_id查找
     *
     * @param waveDetail
     * @return
     * @throws BizCheckedException
     */
    public TaskInfo getTaskInfoByWaveDetail(WaveDetail waveDetail) throws BizCheckedException {
        Long taskId = waveDetail.getQcTaskId();
        TaskInfo qcInfo = baseTaskService.getTaskInfoById(taskId);
        if (TaskConstant.Done.equals(qcInfo.getStatus())) {
            return qcInfo;
        }
        return null;
    }

    /**
     * 获取托盘上的贵品的详情,用作贵品交付单
     *
     * @param contaienrId
     * @return
     * @throws BizCheckedException
     */
    public Map<String, Object> getContainerExpensiveGoods(Long contaienrId) throws BizCheckedException {
        //获取wave_detail
        //todo
//        List<WaveDetail> waveDetails =

        return null;
    }

    /**
     * 根据tuId获取发货单
     *
     * @param tuId
     * @return
     * @throws BizCheckedException
     */
    public Map<String, Object> getDeliveryOrdersList(String tuId) throws BizCheckedException {
        //根据运单号获取发货信息
        TuHead tuHead = iTuRpcService.getHeadByTuId(tuId);

        //根据tuid获取containerId
        //List<TuDetail> tuDetailList = iTuRpcService.getTuDeailListByTuId(tuId);

        //封装订单信息
        Map<Long, Map<String, Object>> orderGoodsInfoMap = new HashMap<Long, Map<String, Object>>();
        //封装订单的商品信息 orderId ,itemId
        Map<Long, Map<Long, Map<String, Object>>> goodsListMap = new HashMap<Long, Map<Long, Map<String, Object>>>();
        //封装订单的头信息(库组分类共用)
        Map<Long, Map<String, Object>> orderInfoMap = new HashMap<Long, Map<String, Object>>();

        List<OutbDeliveryHeader> deliveryHeaderList = soDeliveryService.getOutbDeliveryHeaderByTmsId(tuId);
        for (OutbDeliveryHeader header : deliveryHeaderList){
            List<OutbDeliveryDetail> details = soDeliveryService.getOutbDeliveryDetailListByDeliveryId(header.getDeliveryId());
            ObdHeader orderHeader = soOrderService.getOutbSoHeaderByOrderId(header.getOrderId());
            if(orderHeader == null){
                continue;
            }
            //封装订单信息
            Long orderId = header.getOrderId();
            Map<String, Object> orderMap = new HashMap<String, Object>();
            orderMap.put("orderId", header.getOrderId());//订单号
            orderMap.put("printDate", DateUtils.FORMAT_DATE_WITH_BAR.format(new Date()));//打印日期
            orderMap.put("tuId", tuId);//运单号
            orderMap.put("deliveryId", header.getDeliveryId());//发货单号
            orderMap.put("driverName", tuHead.getName());//司机姓名
            orderMap.put("driverPhone", tuHead.getCellphone());//司机电话
            orderMap.put("boxTotal",header.getBoxNum());//装车总箱数
            orderMap.put("transBoxTotal", header.getTurnoverBoxNum());//装车周转箱数

            //获取店铺信息
            CsiCustomer customer = csiCustomerService.getCustomerByCustomerCode(orderHeader.getDeliveryCode());
            if (customer == null) {
                orderMap.put("storeName", "");//收货门店
                orderMap.put("storePhone", "");//联系电话
                orderMap.put("storeAddress", "");//收货地址
            } else {
                orderMap.put("storeName", customer.getCustomerName());//收货门店
                orderMap.put("storePhone", "");//联系电话// FIXME: 16/11/7
                orderMap.put("storeAddress", customer.getAddress());//收货地址
            }

            orderMap.put("goodsList", new HashMap<String, Object>());//订单商品信息
            orderGoodsInfoMap.put(orderId, orderMap);

            for(OutbDeliveryDetail detail : details){
                Long itemId = detail.getItemId();
                /*
                封装订单对应的商品信息
                 */
                if (goodsListMap.get(orderId) == null) {
                    goodsListMap.put(orderId, new HashMap<Long, Map<String, Object>>());//订单商品信息
                }
                if (goodsListMap.get(orderId).get(itemId) == null) {
                    //行项目信息
                    BigDecimal eaQty = detail.getDeliveryNum();
                    BaseinfoItem item = itemService.getItem(itemId);
                    //订单中,商品信息
                    Map<String, Object> goodsCountMap = new HashMap<String, Object>();
                    goodsCountMap.put("itemId", item.getItemId());
                    goodsCountMap.put("itemType", item.getItemType());   //课组
                    goodsCountMap.put("goodsName", item.getSkuName());
                    goodsCountMap.put("boxNum", PackUtil.EAQty2UomQty(detail.getDeliveryNum(),detail.getPackUnit()));//箱数
                    goodsCountMap.put("eaNum", detail.getDeliveryNum());//件数
                    goodsCountMap.put("unitName", item.getPackName());//箱规,默认EA
                    goodsCountMap.put("isExpensive", item.getIsValuable() == ItemConstant.TYPE_IS_VALUABLE);   //1是贵品,2不是贵品
                    goodsListMap.get(orderId).put(itemId, goodsCountMap);
                }
            }

        }

        List<Object> returnList = new ArrayList<Object>();
        //整合订单和商品数据
        for (Long orderId : orderGoodsInfoMap.keySet()) {
            if (goodsListMap.get(orderId) == null) {
                continue;
            }
            //一个订单的商品列表
            Map<Long, Map<String, Object>> goodsListMapByOrderId = goodsListMap.get(orderId);
            //一个类型的商品
            Map<Long, Map<String, Object>> goodsMapByItemType = new HashMap<Long, Map<String, Object>>();
            //商品类型map
            Map<Long, String> itemTypeMap = new HashMap<Long, String>();

            //将每个订单的商品按类型分组
            for (Long itemId : goodsListMapByOrderId.keySet()) {
                Map<String, Object> goodsMap = goodsListMapByOrderId.get(itemId);

                Long itemTypeId = Long.parseLong(goodsMap.get("itemType").toString());
                if (goodsMapByItemType.get(itemTypeId) == null) {
                    Map<String, Object> goodsMapByItemTypeInit = new HashMap<String, Object>();
                    goodsMapByItemTypeInit.put("goodsListByItemType", new ArrayList<Object>());
                    goodsMapByItemTypeInit.put("itemTypeName", "");
                    goodsMapByItemType.put(itemTypeId, goodsMapByItemTypeInit);
                }
                if (itemTypeMap.get(itemTypeId) == null) {
                    BaseinfoItemType baseinfoItemType = itemTypeService.getBaseinfoItemTypeByItemId(itemTypeId.intValue());
                    if (baseinfoItemType != null) {
                        itemTypeMap.put(itemTypeId, baseinfoItemType.getItemName());
                    } else {
                        itemTypeMap.put(itemTypeId, "");
                    }
                }
                List<Object> tempLis = (List<Object>) goodsMapByItemType.get(itemTypeId).get("goodsListByItemType");
                tempLis.add(goodsMap);
                Map<String, Object> tempMap = goodsMapByItemType.get(itemTypeId);
                tempMap.put("goodsListByItemType", tempLis);
                goodsMapByItemType.put(itemTypeId, tempMap);
            }
            List<Object> goodsList = new ArrayList<Object>();
            for (Long itemTypeId : goodsMapByItemType.keySet()) {
                goodsMapByItemType.get(itemTypeId).put("itemTypeName", itemTypeMap.get(itemTypeId));
                goodsList.add(goodsMapByItemType.get(itemTypeId));

            }
            orderGoodsInfoMap.get(orderId).put("goodsList", goodsList);
            returnList.add(orderGoodsInfoMap.get(orderId));



        }


        Map<String, Object> returnData = new HashMap<String, Object>();
//        returnData.put("data", orderGoodsInfoMap);
//        returnData.put("data", groupListMap);
        returnData.put("data", returnList);
        return returnData;
    }


}
