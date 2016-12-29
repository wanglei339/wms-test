package com.lsh.wms.integration.service.back;


import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lsh.base.common.config.PropertyUtils;
import com.lsh.base.common.exception.BusinessException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.model.CommonResult;
import com.lsh.base.common.net.HttpClientUtils;
import com.lsh.base.common.utils.BeanMapTransUtils;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.api.model.po.IbdBackRequest;
import com.lsh.wms.api.model.po.IbdItem;
import com.lsh.wms.api.model.po.ReceiptItem;
import com.lsh.wms.api.model.wumart.*;
import com.lsh.wms.api.service.back.IDataBackService;
import com.lsh.wms.core.constant.CsiConstan;
import com.lsh.wms.core.constant.IntegrationConstan;
import com.lsh.wms.core.constant.RedisKeyConstant;
import com.lsh.wms.core.constant.SysLogConstant;
import com.lsh.wms.core.dao.redis.RedisStringDao;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.system.SysLogService;
import com.lsh.wms.core.service.system.SysMsgService;
import com.lsh.wms.core.service.taking.StockTakingService;
import com.lsh.wms.integration.model.OrderResponse;
import com.lsh.wms.integration.service.common.utils.HttpUtil;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.datareport.SkuMap;
import com.lsh.wms.model.so.ObdDetail;
import com.lsh.wms.model.stock.OverLossReport;
import com.lsh.wms.model.system.SysLog;
import com.lsh.wms.model.system.SysMsg;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by lixin-mac on 16/9/6.
 */
@Service(protocol = "dubbo")
public class DataBackService implements IDataBackService {
    private static Logger logger = LoggerFactory.getLogger(DataBackService.class);

//    final static String url = "http://115.182.215.119",
//            db = "lsh-odoo-test",
//            username = "yg-rd@lsh123.com",
//            password = "YgRd@Lsh123",
//            uid = "7";

    @Autowired
    private RedisStringDao redisStringDao;

    @Autowired
    private SysMsgService sysMsgService;

    @Autowired
    private SysLogService sysLogService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private StockTakingService stockTakingService;

    public String wmDataBackByPost(String request, String url , Integer type,SysLog sysLog){
        OrderResponse orderResponse = new OrderResponse();
        sysLog.setTargetSystem(SysLogConstant.LOG_TARGET_WUMART);
        try{
            String jsonCreate = request;
            //获取redis中缓存的token
            String token = redisStringDao.get(RedisKeyConstant.WM_BACK_TOKEN);
            if (token == null || token.equals("")){
                token = this.getToken(url);
                redisStringDao.set(RedisKeyConstant.WM_BACK_TOKEN,token);
            }

            if(token != null && !token.equals("")){
                logger.info("order wumart CreateOrder json : " + jsonCreate);
                String jsonStr = HttpUtil.doPost(url,jsonCreate,token);
                logger.info("order wumart res " + jsonStr+"~~~~~~");

                orderResponse = JSON.parseObject(jsonStr, OrderResponse.class);
                if(Integer.valueOf(orderResponse.getCode()) == 1){
                    token = orderResponse.getGatewayToken();
                    redisStringDao.set(RedisKeyConstant.WM_BACK_TOKEN,token);
                    jsonStr = HttpUtil.doPost(url,jsonCreate,token);

                    orderResponse = JSON.parseObject(jsonStr,OrderResponse.class);
                }
                JSONObject json = JSON.parseObject(jsonStr);

                if((Boolean) json.get("success")){
                    sysLog.setLogCode(orderResponse.getCode());
                    sysLog.setLogMessage(orderResponse.getMessage());
                    sysLog.setSysCode("");
                    sysLog.setSysMessage("");
                    sysLog.setStatus(SysLogConstant.LOG_STATUS_FINISH);
                }else{
                    sysLog.setLogMessage(orderResponse.getMessage());
                    sysLog.setSysCode("");
                    sysLog.setSysMessage("");
                    sysLog.setStatus(SysLogConstant.LOG_STATUS_FAILED);
                    sysLog.setLogCode(orderResponse.getCode());
                }
                logger.info("orderResponse = " + JSON.toJSONString(orderResponse));
            }else{
                logger.info("************** token is null ");
            }
        }catch (Exception ex){
            logger.info("回传物美OFC异常 ex : " + ex.fillInStackTrace());
            sysLog.setStatus(SysLogConstant.LOG_STATUS_THROW);
            sysLog.setSysCode("回传物美OFC异常");
            sysLog.setSysMessage(ex.getMessage());

        }
        return JSON.toJSONString(orderResponse);
    }

    public String ofcDataBackByPost(String request, String url,SysLog sysLog){
        //String jsonCreate = this.initJson(request);
        sysLog.setTargetSystem(SysLogConstant.LOG_TARGET_LSHOFC);
        logger.info("order CreateOfcOrder json : " + request);
        int dc41_timeout = PropertyUtils.getInt("dc41_timeout");
        String dc41_charset = PropertyUtils.getString("dc41_charset");
        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("Content-type", "application/json; charset=utf-8");
        headerMap.put("Accept", "application/json");
        headerMap.put("api-version", "1.1");
        headerMap.put("random", RandomUtils.randomStr2(32));
        headerMap.put("platform", "1");
        String res  = HttpClientUtils.postBody(url,  request,dc41_timeout , dc41_charset, headerMap);


        //String jsonStr = HttpUtil.doPost(url,request);

        logger.info("order jsonStr :" + res +"~~~~");
        OrderResponse orderResponse = new OrderResponse();
        try{
            orderResponse = JSON.parseObject(res,OrderResponse.class);
            logger.info("orderResponse = " + JSON.toJSONString(orderResponse));
            if(orderResponse != null && orderResponse.getCode().equals("0000")){
                sysLog.setLogCode(orderResponse.getCode());
                sysLog.setLogMessage("回传lshOFC成功");
                sysLog.setSysCode("");
                sysLog.setSysMessage("");
                sysLog.setStatus(SysLogConstant.LOG_STATUS_FINISH);
            }else{
                sysLog.setLogMessage(orderResponse.getMessage());
                sysLog.setStatus(SysLogConstant.LOG_STATUS_FAILED);
                sysLog.setSysCode("");
                sysLog.setSysMessage("");
                sysLog.setLogCode(orderResponse.getCode());
            }
        }catch (Exception ex){
            sysLog.setStatus(SysLogConstant.LOG_STATUS_THROW);
            logger.info("抛出异常 ex:" + ex);
            sysLog.setLogCode("回传异常");
            sysLog.setLogMessage(ex.getMessage());
            sysLog.setSysCode("回传异常");
            sysLog.setSysMessage(ex.getMessage());
        }
        //sysLog.setRetryTimes(sysLog.getRetryTimes()+1);
        //sysLogService.updateSysLog(sysLog);

        return JSON.toJSONString(orderResponse);

    }

    public Boolean erpDataBack(CreateIbdHeader createIbdHeader,SysLog sysLog){
        sysLog.setTargetSystem(SysLogConstant.LOG_TARGET_ERP);
        try {
            final XmlRpcClient models = new XmlRpcClient() {{
                setConfig(new XmlRpcClientConfigImpl() {{
                    setServerURL(new URL(String.format("%s/xmlrpc/2/object", PropertyUtils.getString("odoo_url"))));
                }});
            }};
            logger.info("~~~~~~~~111111 url:" + PropertyUtils.getString("odoo_url"));
            //原订单ID
            Integer orderOtherId = Integer.valueOf(createIbdHeader.getItems().get(0).getPoNumber());
            Long receiveId = Long.valueOf(createIbdHeader.getItems().get(0).getVendMat());
            Map<String,Object> params = new HashMap<String, Object>();
            params.put("order_id",orderOtherId);
            params.put("receive_code",receiveId.toString());
            params.put("operation_code",sysLog.getLogId().toString());
            //// TODO: 2016/12/23  当前时间 "2016-12-22"
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String date = sdf.format(new Date());
            params.put("receive_date",date);
            List<HashMap<String,Object>> list = new ArrayList<HashMap<String, Object>>();
            for(CreateIbdDetail item : createIbdHeader.getItems()){
                HashMap<String,Object> map = new HashMap<String, Object>();
                map.put("product_code",item.getMaterial());
                map.put("qty_done",item.getDeliveQty().intValue());
                list.add(map);
            }
            params.put("details",list);
            logger.info("~~~~~~~params : " + params + " ~~~~~~~~~~~");
            logger.info("~~~~~~~~~~~~~222222 db: "+ PropertyUtils.getString("odoo_db") + " odoo_uid :" + PropertyUtils.getString("odoo_uid") + " password :" + PropertyUtils.getString("odoo_password"));
            final Boolean ret1  = (Boolean)models.execute("execute_kw", Arrays.asList(
                    PropertyUtils.getString("odoo_db"), Integer.valueOf(PropertyUtils.getString("odoo_uid")), PropertyUtils.getString("odoo_password"),
                    "purchase.order", "lsh_action_wms_receive",
                    Arrays.asList(params)
            ));
            //// TODO: 16/9/19 传入的参数
            logger.info("~~~~~~~~ret1 :" + ret1 + "~~~~~~~~~~~~~");
            if(ret1){
                sysLog.setStatus(SysLogConstant.LOG_STATUS_FINISH);
                sysLog.setLogMessage("回传erp成功");
                sysLog.setSysCode("");
                sysLog.setSysMessage("");
            }else{
                sysLog.setStatus(SysLogConstant.LOG_STATUS_FAILED);
                sysLog.setLogMessage("回传erp失败");
            }
            //sysLog.setRetryTimes(sysLog.getRetryTimes()+1);
            //sysLogService.updateSysLog(sysLog);

        }
        catch (Exception e) {
            //logger.info(e.getCause().getMessage());
            sysLog.setSysMessage(e.getMessage());
            sysLog.setSysCode("回传ERP异常");
            sysLog.setStatus(SysLogConstant.LOG_STATUS_THROW);
        }
        return false;
    }
    public Boolean erpOvLosserBack(OverLossReport overLossReport,SysLog sysLog){
        sysLog.setTargetSystem(SysLogConstant.LOG_TARGET_ERP);
        try {
            if(overLossReport!=null){
                String warehouseCode = locationService.getWarehouseLocation().getLocationCode();
                Map<String,Object> params =  BeanMapTransUtils.Bean2map(overLossReport);
                params.put("warehouseCode",warehouseCode);
                String requestBody = JsonUtils.obj2Json(params);
                int dc41_timeout = PropertyUtils.getInt("dc41_timeout");
                String dc41_charset = PropertyUtils.getString("dc41_charset");
                Map<String, String> headerMap = new HashMap<String, String>();
                headerMap.put("Content-type", "application/json; charset=utf-8");
                headerMap.put("Accept", "application/json");
                headerMap.put("api-version", "1.1");
                headerMap.put("random", RandomUtils.randomStr2(32));
                headerMap.put("platform", "1");
                String result  = HttpClientUtils.postBody(PropertyUtils.getString("url_over_loss_erp"),  requestBody,dc41_timeout , dc41_charset, headerMap);
                logger.info("~~~~~~~~~~下发erp数据 request : " + JSON.toJSONString(params) + "~~~~~~~~~");
                Map<String,Object> head = (Map)JSON.parseObject(result).get("head");
                if(!head.get("status").toString().equals("1")){
                    sysLog.setLogMessage(head.get("message").toString());
                    sysLog.setLogCode("回传ERP异常");
                    sysLog.setStatus(SysLogConstant.LOG_STATUS_FAILED);
                }


            }
            sysLog.setStatus(SysLogConstant.LOG_STATUS_FINISH);
            sysLog.setLogMessage("回传erp成功");
            sysLog.setSysCode("");
            sysLog.setSysMessage("");
        }
        catch (Exception e) {
            logger.info(e.getMessage());
            sysLog.setSysMessage(e.getMessage());
            sysLog.setSysCode("回传ERP异常");
            sysLog.setStatus(SysLogConstant.LOG_STATUS_THROW);
        }
        return false;
    }
    public Boolean erpBackCommodityData(BaseinfoItem item,SysLog sysLog){
        sysLog.setTargetSystem(SysLogConstant.LOG_TARGET_ERP);
        try {
            if(item!=null){
                String warehouseCode = locationService.getWarehouseLocation().getLocationCode();
                Map<String,Object> params =  BeanMapTransUtils.Bean2map(item);
                params.put("warehouseCode",warehouseCode);
                String requestBody = JsonUtils.obj2Json(params);
                int dc41_timeout = PropertyUtils.getInt("dc41_timeout");
                String dc41_charset = PropertyUtils.getString("dc41_charset");
                Map<String, String> headerMap = new HashMap<String, String>();
                headerMap.put("Content-type", "application/json; charset=utf-8");
                headerMap.put("Accept", "application/json");
                headerMap.put("api-version", "1.1");
                headerMap.put("random", RandomUtils.randomStr2(32));
                headerMap.put("platform", "1");
                String result  = HttpClientUtils.postBody(PropertyUtils.getString("url_back_commodity_erp"),  requestBody,dc41_timeout , dc41_charset, headerMap);
                logger.info("~~~~~~~~~~下发erp数据 request : " + JSON.toJSONString(params) + "~~~~~~~~~");
                Map<String,Object> head = (Map)JSON.parseObject(result).get("head");
                if(!head.get("status").toString().equals("1")){
                    sysLog.setLogMessage(head.get("message").toString());
                    sysLog.setLogCode("回传ERP异常");
                    sysLog.setStatus(SysLogConstant.LOG_STATUS_FAILED);
                }


            }
            sysLog.setStatus(SysLogConstant.LOG_STATUS_FINISH);
            sysLog.setLogMessage("回传erp成功");
            sysLog.setSysCode("");
            sysLog.setSysMessage("");
        }
        catch (Exception e) {
            logger.info(e.getMessage());
            sysLog.setSysMessage(e.getMessage());
            sysLog.setSysCode("回传ERP异常");
            sysLog.setStatus(SysLogConstant.LOG_STATUS_THROW);
        }
        return false;
    }

    public Boolean obd2Erp(CreateObdHeader createObdHeader, SysLog sysLog) {
        sysLog.setTargetSystem(SysLogConstant.LOG_TARGET_ERP);
        try {
            final XmlRpcClient models = new XmlRpcClient() {{
                setConfig(new XmlRpcClientConfigImpl() {{
                    setServerURL(new URL(String.format("%s/xmlrpc/2/object", PropertyUtils.getString("odoo_url"))));
                }});
            }};
            logger.info("~~~~~~~~111111 url:" + PropertyUtils.getString("odoo_url"));
            //原订单ID
            Integer orderOtherId = Integer.valueOf(createObdHeader.getOrderOtherId());
            Long delivery = Long.valueOf(createObdHeader.getDeliveryId());
            Map<String,Object> params = new HashMap<String, Object>();
            params.put("picking_id",orderOtherId);
            params.put("delivery_code",delivery.toString());
            //params.put("operation_code",sysLog.getLogId().toString());
            //// TODO: 2016/12/23  当前时间 "2016-12-22"
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String date = sdf.format(new Date());
            params.put("delivery_date",date);
            List<HashMap<String,Object>> list = new ArrayList<HashMap<String, Object>>();
            for(CreateObdDetail item : createObdHeader.getItems()){
                HashMap<String,Object> map = new HashMap<String, Object>();
                map.put("product_code",item.getMaterial());
                map.put("qty_done",item.getDlvQty().intValue());
                list.add(map);
            }
            params.put("details",list);
            logger.info("~~~~~~~params : " + params + " ~~~~~~~~~~~");
            logger.info("~~~~~~~~~~~~~222222 db: "+ PropertyUtils.getString("odoo_db") + " odoo_uid :" + PropertyUtils.getString("odoo_uid") + " password :" + PropertyUtils.getString("odoo_password"));
            final Boolean ret1  = (Boolean)models.execute("execute_kw", Arrays.asList(
                    PropertyUtils.getString("odoo_db"), Integer.valueOf(PropertyUtils.getString("odoo_uid")), PropertyUtils.getString("odoo_password"),
                    "stock.picking", "lsh_action_wms_delivery",
                    Arrays.asList(params)
            ));
            //// TODO: 16/9/19 传入的参数
            logger.info("~~~~~~~~ret1 :" + ret1 + "~~~~~~~~~~~~~");
            if(ret1){
                sysLog.setStatus(SysLogConstant.LOG_STATUS_FINISH);
                sysLog.setLogMessage("回传erp成功");
                sysLog.setSysCode("");
                sysLog.setSysMessage("");
            }else{
                sysLog.setStatus(SysLogConstant.LOG_STATUS_FAILED);
                sysLog.setLogMessage("回传erp失败");
            }
            //sysLog.setRetryTimes(sysLog.getRetryTimes()+1);
            //sysLogService.updateSysLog(sysLog);

        }
        catch (Exception e) {
            //logger.info(e.getCause().getMessage());
            sysLog.setSysMessage(e.getMessage());
            sysLog.setSysCode("回传ERP异常");
            sysLog.setStatus(SysLogConstant.LOG_STATUS_THROW);
        }
        return false;
    }

    public List<SkuMap> skuMapFromErp(List<String> skuCodes) {
        List<SkuMap> skuMaps = new ArrayList<SkuMap>();
        try {
            final XmlRpcClient models = new XmlRpcClient() {{
                setConfig(new XmlRpcClientConfigImpl() {{
                    setServerURL(new URL(String.format("%s/xmlrpc/2/object", PropertyUtils.getString("odoo_url"))));
                }});
            }};
            Map<String,List<Object>> map = new HashMap<String, List<Object>>();
            List<Object> list = new ArrayList<Object>();
            list.add("default_code");
            list.add("standard_price");//未税
            list.add("tax_standard_price_in");//含税
            map.put("fields",list);
            List<Object> arr = new ArrayList<Object>();
            arr.add("default_code");
            arr.add("in");
            arr.add(skuCodes);

            logger.info("参数 : skucodes :" + arr + " map :" + map);

            final Object ret1  = models.execute("execute_kw", Arrays.asList(
                    PropertyUtils.getString("odoo_db"), Integer.valueOf(PropertyUtils.getString("odoo_uid")), PropertyUtils.getString("odoo_password"),
                    "product.product", "search_read",
                    Arrays.asList(Arrays.asList(arr)),map
            ));

//            List<ErpSkuMap> receiptItemList = JSON.parseArray((String)ret1, ErpSkuMap.class);
//            System.out.println(JSON.toJSONString(receiptItemList));

            String jsonStr = JSON.toJSONString(ret1);

            JSONArray jsonArray = JSONObject.parseArray(jsonStr);


            for(int i = 0; i< jsonArray.size();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Long id = jsonObject.getLong("id");
                BigDecimal taxPrice = jsonObject.getBigDecimal("tax_standard_price_in");
                BigDecimal standardPrice = jsonObject.getBigDecimal("standard_price");
                String skuCode = jsonObject.getString("default_code");
                SkuMap skuMap = new SkuMap();
                skuMap.setMovingAveragePrice(standardPrice);
                skuMap.setSkuCode(skuCode);
                skuMap.setOwnerId(CsiConstan.OWNER_LSH);
                skuMap.setSourceSystem(2);
                skuMaps.add(skuMap);
            }


            List<Object> list1 = new ArrayList<Object>();
            ObjUtils.bean2bean(ret1,list1);
            System.out.println(JSON.toJSONString(list1));

            System.out.println("ret1 :" + JSON.toJSONString(ret1));

        }catch (Exception e){
            e.getMessage();
            return new ArrayList<SkuMap>();
        }

        return skuMaps;
    }


    private String getToken(String url){
        String jsonStr = HttpUtil.doPostToken(url);
        // System.out.println(jsonStr);
        OrderResponse orderResponse = JSON.parseObject(jsonStr, OrderResponse.class);
        if(orderResponse != null && Integer.valueOf(orderResponse.getCode()) == 1){
            return orderResponse.getGatewayToken();
        }
        return null;
    }


    private String initJson(Object request){


        return JSON.toJSONString(request);
    }

    public static void main(String[] args) {
        List<SkuMap> list = new ArrayList<SkuMap>();
        SkuMap skuMap1 = new SkuMap();
        skuMap1.setOwnerId(2l);
        skuMap1.setSkuCode("1111111");
        skuMap1.setMovingAveragePrice(BigDecimal.ONE);
        SkuMap skuMap2 = new SkuMap();
        skuMap2.setOwnerId(2l);
        skuMap2.setSkuCode("1111111");
        skuMap2.setMovingAveragePrice(BigDecimal.ONE);
        list.add(skuMap1);
        list.add(skuMap2);
        System.out.println(JSON.toJSONString(list));


    }

}
