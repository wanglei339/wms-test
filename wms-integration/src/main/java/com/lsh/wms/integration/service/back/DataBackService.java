package com.lsh.wms.integration.service.back;


import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.base.q.Utilities.Json.JSONObject;
import com.lsh.wms.api.model.po.IbdBackRequest;
import com.lsh.wms.api.model.po.IbdItem;
import com.lsh.wms.api.model.wumart.CreateIbdDetail;
import com.lsh.wms.api.model.wumart.CreateIbdHeader;
import com.lsh.wms.api.service.back.IDataBackService;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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

    public String wmDataBackByPost(String request, String url , Integer type,SysLog sysLog){
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

            // TODO: 2016/11/17 返回值 不确定 



            logger.info("orderResponse = " + JSON.toJSONString(orderResponse));
        }else{
            logger.info("************** token is null ");
        }
        return JSON.toJSONString(orderResponse);

    }

    public String ofcDataBackByPost(String request, String url,SysLog sysLog){
        //String jsonCreate = this.initJson(request);

        logger.info("order CreateOfcOrder json : " + request);
        String jsonStr = HttpUtil.doPost(url,request);

        logger.info("order jsonStr :" + jsonStr +"~~~~");
        OrderResponse orderResponse = JSON.parseObject(jsonStr,OrderResponse.class);
        logger.info("orderResponse = " + JSON.toJSONString(orderResponse));
        if(orderResponse.getCode().equals("0000")){
            sysLog.setLogCode(orderResponse.getCode());
            sysLog.setLogMessage("回传lshOFC成功");
            sysLog.setStatus(2l);
        }else{
            sysLog.setLogMessage(orderResponse.getMessage());
            sysLog.setStatus(3l);
            sysLog.setLogCode(orderResponse.getCode());
        }
        sysLog.setRetryTimes(sysLog.getRetryTimes()+1);
        sysLogService.updateSysLog(sysLog);

        return JSON.toJSONString(orderResponse);

    }

    public Boolean erpDataBack(String json,SysLog sysLog){
        try {
            //入口将字符串转为对象 CreateIbdHeader
            JSONObject obj = new JSONObject(json);
            CreateIbdHeader createIbdHeader = new CreateIbdHeader();
            ObjUtils.bean2bean(obj,createIbdHeader);

            final XmlRpcClient models = new XmlRpcClient() {{
                setConfig(new XmlRpcClientConfigImpl() {{
                    setServerURL(new URL(String.format("%s/xmlrpc/2/object", url)));
                }});
            }};
            //原订单ID
            Integer orderOtherId = Integer.valueOf(createIbdHeader.getItems().get(0).getPoNumber());


            Long receiveId = Long.valueOf(createIbdHeader.getItems().get(0).getVendMat());

            List<HashMap<String,Object>> list = new ArrayList<HashMap<String, Object>>();
            for(CreateIbdDetail item : createIbdHeader.getItems()){
                HashMap<String,Object> map = new HashMap<String, Object>();
                map.put("product_code",item.getMaterial());
                map.put("qty_done",item.getDeliveQty().intValue());
                list.add(map);

            }
            logger.info("~~~~~~~list : " + list + " ~~~~~~~~~~~");
            final Boolean ret1  = (Boolean)models.execute("execute_kw", Arrays.asList(
                    db, uid, password,
                    "purchase.order", "lsh_action_wms_receive",
                    Arrays.asList(Arrays.asList(orderOtherId),list,Arrays.asList(receiveId))

            ));
            //// TODO: 16/9/19 传入的参数
            logger.info("~~~~~~~~ret1 :" + ret1 + "~~~~~~~~~~~~~");
            if(ret1){
                sysLog.setStatus(2l);
                sysLog.setLogMessage("回传erp成功");
            }else{
                sysLog.setStatus(3l);
                sysLog.setLogMessage("回传erp失败");
            }
            sysLog.setRetryTimes(sysLog.getRetryTimes()+1);
            sysLogService.updateSysLog(sysLog);

        }
        catch (Exception e) {
            logger.info(e.getCause().getMessage());
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
    }




}
