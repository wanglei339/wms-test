package com.lsh.wms.integration.service.back;


import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lsh.base.common.config.PropertyUtils;
import com.lsh.base.common.net.HttpClientUtils;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.api.model.po.IbdBackRequest;
import com.lsh.wms.api.model.po.IbdItem;
import com.lsh.wms.api.model.wumart.CreateIbdDetail;
import com.lsh.wms.api.model.wumart.CreateIbdHeader;
import com.lsh.wms.api.service.back.IDataBackService;
import com.lsh.wms.core.constant.IntegrationConstan;
import com.lsh.wms.core.constant.RedisKeyConstant;
import com.lsh.wms.core.constant.SysLogConstant;
import com.lsh.wms.core.dao.redis.RedisStringDao;
import com.lsh.wms.core.service.system.SysLogService;
import com.lsh.wms.core.service.system.SysMsgService;
import com.lsh.wms.integration.model.OrderResponse;
import com.lsh.wms.integration.service.common.utils.HttpUtil;
import com.lsh.wms.model.system.SysLog;
import com.lsh.wms.model.system.SysMsg;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.util.*;

/**
 * Created by lixin-mac on 16/9/6.
 */
@Service(protocol = "dubbo",async=true)
public class DataBackService implements IDataBackService {
    private static Logger logger = LoggerFactory.getLogger(DataBackService.class);

    final static String url = "http://115.182.215.119",
            db = "lsh-odoo-test",
            username = "yg-rd@lsh123.com",
            password = "YgRd@Lsh123",
            uid = "7";

    @Autowired
    private RedisStringDao redisStringDao;

    @Autowired
    private SysMsgService sysMsgService;

    @Autowired
    private SysLogService sysLogService;

    public void wmDataBackByPost(String request, String url , Integer type,SysLog sysLog){
        try{
            String jsonCreate = request;
            //获取redis中缓存的token
            String token = redisStringDao.get(RedisKeyConstant.WM_BACK_TOKEN);
            if (token == null || token.equals("")){
                token = this.getToken(url);
                redisStringDao.set(RedisKeyConstant.WM_BACK_TOKEN,token);
            }
            OrderResponse orderResponse = new OrderResponse();
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
                    sysLog.setStatus(SysLogConstant.LOG_STATUS_FINISH);
                }else{
                    sysLog.setLogMessage(orderResponse.getMessage());
                    sysLog.setStatus(SysLogConstant.LOG_STATUS_FAILED);
                    sysLog.setLogCode(orderResponse.getCode());
                }
                logger.info("orderResponse = " + JSON.toJSONString(orderResponse));
            }else{
                logger.info("************** token is null ");
            }
        }catch (Exception ex){
            logger.info("回传物美OFC异常 ex : " + ex.fillInStackTrace());
            sysLog.setStatus(SysLogConstant.LOG_STATUS_FAILED);
            sysLog.setSysCode("回传物美OFC异常");
            sysLog.setSysMessage(ex.getMessage());

        }

    }

    public String ofcDataBackByPost(String request, String url,SysLog sysLog){
        //String jsonCreate = this.initJson(request);

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
                sysLog.setStatus(SysLogConstant.LOG_STATUS_FINISH);
            }else{
                sysLog.setLogMessage(orderResponse.getMessage());
                sysLog.setStatus(SysLogConstant.LOG_STATUS_FAILED);
                sysLog.setLogCode(orderResponse.getCode());
            }
        }catch (Exception ex){
            sysLog.setStatus(SysLogConstant.LOG_STATUS_CREATE);
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
        try {
            final XmlRpcClient models = new XmlRpcClient() {{
                setConfig(new XmlRpcClientConfigImpl() {{
                    setServerURL(new URL(String.format("%s/xmlrpc/2/object", url)));
                }});
            }};
            //原订单ID
            Integer orderOtherId = Integer.valueOf(createIbdHeader.getItems().get(0).getPoNumber());
            Long receiveId = Long.valueOf(createIbdHeader.getItems().get(0).getVendMat());
            Map<String,Object> params = new HashMap<String, Object>();
            params.put("order_id",orderOtherId);
            params.put("receive_code",receiveId.toString());
            List<HashMap<String,Object>> list = new ArrayList<HashMap<String, Object>>();
            for(CreateIbdDetail item : createIbdHeader.getItems()){
                HashMap<String,Object> map = new HashMap<String, Object>();
                map.put("product_code",item.getMaterial());
                map.put("qty_done",item.getDeliveQty().intValue());
                list.add(map);
            }
            params.put("details",list);
            logger.info("~~~~~~~params : " + params + " ~~~~~~~~~~~");
            final Boolean ret1  = (Boolean)models.execute("execute_kw", Arrays.asList(
                    db, Integer.valueOf(uid), password,
                    "purchase.order", "lsh_action_wms_receive",
                    Arrays.asList(params)
            ));
            //// TODO: 16/9/19 传入的参数
            logger.info("~~~~~~~~ret1 :" + ret1 + "~~~~~~~~~~~~~");
            if(ret1){
                sysLog.setStatus(SysLogConstant.LOG_STATUS_FINISH);
                sysLog.setLogMessage("回传erp成功");
            }else{
                sysLog.setStatus(SysLogConstant.LOG_STATUS_FAILED);
                sysLog.setLogMessage("回传erp失败");
            }
            //sysLog.setRetryTimes(sysLog.getRetryTimes()+1);
            //sysLogService.updateSysLog(sysLog);

        }
        catch (Exception e) {
            logger.info(e.getCause().getMessage());
            sysLog.setSysMessage(e.getMessage());
            sysLog.setSysCode("回传ERP异常");
            sysLog.setStatus(SysLogConstant.LOG_STATUS_FAILED);
        }
        return false;
    }



    private String getToken(String url){
        String jsonStr = HttpUtil.doPostToken(url);
        System.out.println(jsonStr);
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

        String json =  "{\"message\":\"调用成功!\",\"code\":200,\"success\":true}";
        OrderResponse  orderResponse = JSON.parseObject(json,OrderResponse.class);
        JSONObject jsonObject = JSON.parseObject(json);
        if((Boolean) jsonObject.get("success")){
            System.out.println("~~~~~~");
        }
        System.out.println(JSON.toJSONString(orderResponse));
    }




}
