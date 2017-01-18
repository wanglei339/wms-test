package com.lsh.wms.integration.service.tmstu;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.lsh.base.common.config.PropertyUtils;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.net.HttpClientUtils;
import com.lsh.base.common.utils.BeanMapTransUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.api.service.location.ILocationRpcService;
import com.lsh.wms.api.service.tmstu.ITmsTuRpcService;
import com.lsh.wms.api.service.tu.ITuRpcService;
import com.lsh.wms.core.constant.TuConstant;
import com.lsh.wms.core.service.baseinfo.ItemTypeService;
import com.lsh.wms.core.service.csi.CsiCustomerService;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.so.SoDeliveryService;
import com.lsh.wms.core.service.so.SoOrderService;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.core.service.tu.TuService;
import com.lsh.wms.core.service.utils.HttpUtils;
import com.lsh.wms.core.service.wave.WaveService;
import com.lsh.wms.model.csi.CsiCustomer;
import com.lsh.wms.model.so.ObdHeader;
import com.lsh.wms.model.so.OutbDeliveryDetail;
import com.lsh.wms.model.so.OutbDeliveryHeader;
import com.lsh.wms.model.tu.TuDetail;
import com.lsh.wms.model.tu.TuHead;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * Created by fengkun on 2016/11/16.
 */
@Service(protocol = "dubbo")
public class TmsTuRpc implements ITmsTuRpcService {
    private static Logger logger = LoggerFactory.getLogger(TmsTu.class);

    @Autowired
    private TuService tuService;
    @Autowired
    private CsiCustomerService csiCustomerService;
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

    /**
     * 使用POST方式将TU发车
     *
     * @param tuId
     * @throws BizCheckedException
     */
    public Boolean postTuDetails(String tuId) throws BizCheckedException {
        TuHead tuHead = tuService.getHeadByTuId(tuId);
        Map<String, Object> result = new HashMap<String, Object>();
        String responseBody = "";
        if (tuHead == null) {
            throw new BizCheckedException("2990022");
        }
        if (!tuHead.getStatus().equals(TuConstant.SHIP_OVER)) {
            throw new BizCheckedException("2990037");
        }
        List<TuDetail> tuDetails = tuService.getTuDeailListByTuId(tuId);
        List<Map<String, Object>> travelOrderList = this.getTravelOrderList(tuId);
        List<Map<String, Object>> details = new ArrayList<Map<String, Object>>();
        for (TuDetail tuDetail : tuDetails) {
            Map<String, Object> detail = BeanMapTransUtils.Bean2map(tuDetail);
            CsiCustomer csiCustomer = csiCustomerService.getCustomerByCustomerId(tuDetail.getStoreId());
            detail.put("customerCode", csiCustomer.getCustomerCode());
            detail.put("customerName", csiCustomer.getCustomerName());
            details.add(detail);
        }
        result.put("tuId", tuId); //先转成long
        result.put("tuHead", tuHead);
        result.put("scale", tuHead.getScale());
        result.put("tuDetails", details);
        result.put("travelOrderList", travelOrderList);
        String url = PropertyUtils.getString("tms_ship_over_url");
        /*int timeout = PropertyUtils.getInt("tms_timeout");
        String charset = PropertyUtils.getString("tms_charset");
        Map<String, String> headMap = new HashMap<String, String>();
        headMap.put("Content-type", "application/x-www-form-urlencoded; charset=utf-8");*/
        // headMap.put("Accept", "**/*//*");
        logger.info("[SHIP OVER]Begin to transfer to TMS, " + "URL: " + url + ", Request body: " + JSON.toJSONString(result));
        try {
            // responseBody = HttpClientUtils.post(url, result, timeout, charset, headMap);
            responseBody = HttpUtils.doPost(url, result);
        } catch (Exception e) {
            logger.info("[SHIP OVER]Transfer to TMS failed: " + responseBody);
            return false;
        }
        logger.info("[SHIP OVER]Transfer to TMS success: " + responseBody);
        return true;
    }

    /**
     * 根据tu获取以门店聚类的发货单单号的list
     *
     * @param tuId
     * @return
     * @throws BizCheckedException
     */
    public List<Map<String, Object>> getTravelOrderList(String tuId) throws BizCheckedException {
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
            String customerCode = obdHeader.getDeliveryCode();

            //店铺集合
            storeNoSet.add(customerCode);

            //封装店铺和发货单映射关系
            if (storeNoToDeliveryId.get(customerCode) == null) {
                storeNoToDeliveryId.put(customerCode, new HashSet<Long>());
            }
            storeNoToDeliveryId.get(customerCode).add(oudd.getDeliveryId());
            storeNo2OwnerId.put(customerCode, obdHeader.getOwnerUid());
        }

        //获取并封装店铺信息
        //storeid key
        Map<String, Map<String, Object>> storeInfoMap = new HashMap<String, Map<String, Object>>();
        //封装门店发货单号
        List<Map<String, Object>> storeDeliveryList = new ArrayList<Map<String, Object>>();
        for (String customerCode : storeNoSet) {
            CsiCustomer customer = csiCustomerService.getCustomerByCustomerCode(customerCode);
            Map<String, Object> storeMap = new HashMap<String, Object>();
            storeMap.put("customerCode", customerCode);
            String customerId = "";
            String customerName = "";
            if (customer != null) {
                customerName = customer.getCustomerName();
                customerId = customer.getCustomerId() + "";
            }
            storeMap.put("customerId", customerId);
            storeMap.put("customerName", customerName);

            storeInfoMap.put(customerId, storeMap);

            //将店铺名称放入店铺发货单关系中
            Map<String, Object> temp = new HashMap<String, Object>();
            temp.put("customerName", customerName);
            temp.put("customerId", customerId);
            temp.put("list", storeNoToDeliveryId.get(customerCode));
            //storeNo key
            Map<String, Object> storeDeliveryIdInfoMap = new HashMap<String, Object>();
            storeDeliveryIdInfoMap.put(customerCode, temp);
            storeDeliveryList.add(storeDeliveryIdInfoMap);
        }
        return storeDeliveryList;
    }


    /**
     * 使用post请求tms获取,通过线路编号获取司机的信息
     *
     * @param transPlan
     * @return
     * @throws BizCheckedException
     */
    public static TuHead test(String transPlan) throws BizCheckedException {
        if (null == transPlan) {
            throw new BizCheckedException("2990055");
        }
        Map<String, Object> request = new HashMap<String, Object>();
        request.put("transPlan", transPlan);

        // String url = PropertyUtils.getString("tms_trans_request_url");
        String url = "http://qa2.market-tms.wmdev.lsh123.com/order/wave/getroutetransinfo";
//        int tms_timeout = PropertyUtils.getInt("tms_timeout");
//        String tms_charset = PropertyUtils.getString("tms_charset");
//        Map<String, String> headerMap = new HashMap<String, String>();
//        headerMap.put("Content-type", "application/json; charset=utf-8");
//        headerMap.put("Accept", "application/json");
//        headerMap.put("api-version", "1.1");
//        headerMap.put("random", RandomUtils.randomStr2(32));
//        headerMap.put("platform", "1");
//        //String url, Map<String, String> params, int timeout, String charset, Map<String, String> headMap
//        String res = HttpClientUtils.post(url, request, tms_timeout, tms_charset, headerMap);
        logger.info("request tms json : " + JSON.toJSONString(request));
        String responseBody = HttpUtils.doPostByForm(url, request);

        logger.info("tms transPlan Response jsonStr :" + responseBody + "~~~~~~~~~~~~~");
        Map<String, Object> responseMap = JSON.parseObject(responseBody, Map.class);
        TuHead tuHead = new TuHead();
        try {
            logger.info("tms transPlan map " + responseMap + "~~~~~~~~~~~~~");

            if (null != responseMap) {
                Map<String,Map<String,Object>> driverInfo = (Map<String, Map<String, Object>>) responseMap.get("content");
                if (null == driverInfo || driverInfo.isEmpty()) {
                    throw new BizCheckedException("2990049");
                }
                if (null==driverInfo.get("transPlan")
                        ||null==driverInfo.get("transUid")
                        ||null==driverInfo.get("carNumber")
                        ||null==driverInfo.get("name")
                        ||null==driverInfo.get("cellphone")){
                    throw new BizCheckedException("2990051");
                }

                tuHead.setCarNumber(driverInfo.get("carNumber").toString());
                //tranPlan
                tuHead.setTransPlan(driverInfo.get("transPlan").toString());
                tuHead.setName(driverInfo.get("name").toString());
                tuHead.setCellphone(driverInfo.get("cellphone").toString());
                tuHead.setTransUid(Long.valueOf(driverInfo.get("cellphone").toString()));

            } else {
                throw new BizCheckedException("2990050");
            }
        } catch (Exception e) {
            logger.info("抛出异常 ex:" + e);
        }
        return tuHead;
    }
    public TuHead requestTMSGetDriverInfo(String transPlan) throws BizCheckedException {
        if (null == transPlan) {
            throw new BizCheckedException("2990048");
        }
        Map<String, Object> request = new HashMap<String, Object>();
        request.put("transPlan", transPlan);

       // String url = PropertyUtils.getString("tms_trans_request_url");
        String url = "http://qa2.market-tms.wmdev.lsh123.com/order/wave/getroutetransinfo";
        logger.info("request tms json : " + JSON.toJSONString(request));
        String responseBody = HttpUtils.doPostByForm(url, request);

        logger.info("tms transPlan Response jsonStr :" + responseBody + "~~~~~~~~~~~~~");
        Map<String, Map<String,Object>> responseMap = JSON.parseObject(responseBody, Map.class);
        TuHead tuHead = new TuHead();
        try {
            logger.info("tms transPlan map " + responseMap.get("content") + "~~~~~~~~~~~~~");
            if (null != responseMap) {
                Map<String,Object> content =  responseMap.get("content");
                if (null == content || content.isEmpty()) {
                    throw new BizCheckedException("2990049");
                }
                if (null==content.get("transPlan")
                        ||null==content.get("transUid")
                        ||null==content.get("carNumber")
                        ||null==content.get("name")
                        ||null==content.get("cellphone")){
                    throw new BizCheckedException("2990051");
                }

                tuHead.setCarNumber(content.get("carNumber").toString());
                //tranPlan
                tuHead.setTransPlan(content.get("transPlan").toString());
                tuHead.setName(content.get("name").toString());
                tuHead.setCellphone(content.get("cellphone").toString());
                tuHead.setTransUid(Long.valueOf(content.get("cellphone").toString()));
            } else {
                throw new BizCheckedException("2990050");
            }
        } catch (Exception e) {
            logger.info("抛出异常 ex:" + e);
            throw new BizCheckedException("2990052");
        }
        return tuHead;
    }
}
