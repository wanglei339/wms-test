package com.lsh.wms.integration.service.ibd;


import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.lsh.wms.api.model.po.IbdBackRequest;
import com.lsh.wms.api.model.po.IbdItem;
import com.lsh.wms.api.service.po.IIbdBackService;
import com.lsh.wms.core.constant.RedisKeyConstant;
import com.lsh.wms.core.dao.redis.RedisStringDao;
import com.lsh.wms.integration.service.common.utils.HttpUtil;
import com.lsh.wms.integration.model.OrderResponse;
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
public class IbdBackService implements IIbdBackService{
    private static Logger logger = LoggerFactory.getLogger(IbdBackService.class);

    final static String url = "http://115.182.215.119",
            db = "lsh-odoo-test",
            username = "yg-rd@lsh123.com",
            password = "YgRd@Lsh123",
            uid = "7";

    @Autowired
    private RedisStringDao redisStringDao;

    public String createOrderByPost(Object request, String url){
        String jsonPoCreate = this.initOrderCreate(request);

        //token 缓存 2小时 获取token
        //可配置
        //String url = "http://service.wumart.com/wms/ibd";//ibd _url
        //String url = "http://service.wumart.com/wms/stockChange";// 报损报溢的
        //url = "http://service.wumart.com/wms/soobd";//SO


        //获取redis中缓存的token
        String token = redisStringDao.get(RedisKeyConstant.WM_BACK_TOKEN);


        if (token == null || token.equals("")){
            token = this.getToken(url);
            redisStringDao.set(RedisKeyConstant.WM_BACK_TOKEN,token);
        }

        OrderResponse orderResponse = new OrderResponse();
        if(token != null && !token.equals("")){
            logger.info("order wumart CreateOrder json : " + jsonPoCreate);
            String jsonStr = HttpUtil.doPost(url,jsonPoCreate,token);
            logger.info("order wumart res " + jsonStr+"~~~~~~");

            orderResponse = JSON.parseObject(jsonStr, OrderResponse.class);
            if(orderResponse.getCode() == 1){
                token = orderResponse.getGatewayToken();
                redisStringDao.set(RedisKeyConstant.WM_BACK_TOKEN,token);
                jsonStr = HttpUtil.doPost(url,jsonPoCreate,token);
                orderResponse = JSON.parseObject(jsonStr,OrderResponse.class);
            }

            logger.info("orderResponse = " + JSON.toJSONString(orderResponse));
        }else{
            logger.info("************** token is null ");
        }
        return JSON.toJSONString(orderResponse);

    }

    public String createOfcOrderByPost(Object request, String url){
        String jsonPoCreate = this.initOrderCreate(request);

        logger.info("order CreateOfcOrder json : " + jsonPoCreate);
        String jsonStr = HttpUtil.doPost(url,jsonPoCreate);

        logger.info("order jsonStr :" + jsonStr +"~~~~");
        OrderResponse orderResponse = JSON.parseObject(jsonStr,OrderResponse.class);
        logger.info("orderResponse = " + JSON.toJSONString(orderResponse));
        return JSON.toJSONString(orderResponse);

    }

    public Boolean receivePurchaseOrder(IbdBackRequest request){
        try {
            final XmlRpcClient models = new XmlRpcClient() {{
                setConfig(new XmlRpcClientConfigImpl() {{
                    setServerURL(new URL(String.format("%s/xmlrpc/2/object", url)));
                }});
            }};
            //原订单ID
            Integer orderOtherId = Integer.valueOf(request.getHeader().getPoNumber());
            List<HashMap<String,Object>> list = new ArrayList<HashMap<String, Object>>();
            for(IbdItem item : request.getItems()){
                HashMap<String,Object> map = new HashMap<String, Object>();
                map.put("product_id",Integer.valueOf(item.getMaterialNo()));
                map.put("qty_done",item.getEntryQnt().intValue());
                list.add(map);
                
            }
            logger.info("~~~~~~~list : " + list + " ~~~~~~~~~~~");
            final Boolean ret1  = (Boolean)models.execute("execute_kw", Arrays.asList(
                    db, uid, password,
                    "purchase.order", "lsh_action_wms_receive",
                    Arrays.asList(Arrays.asList(orderOtherId),list)

            ));
            //// TODO: 16/9/19 传入的参数
            logger.info("~~~~~~~~ret1 :" + ret1 + "~~~~~~~~~~~~~");


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

        if(orderResponse != null && orderResponse.getCode() == 1){
            return orderResponse.getGatewayToken();
        }

        return null;
    }


    private String initOrderCreate(Object request){


        return JSON.toJSONString(request);
    }




}
