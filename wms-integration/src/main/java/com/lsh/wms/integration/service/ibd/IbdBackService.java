package com.lsh.wms.integration.service.ibd;


import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.lsh.wms.api.model.po.IbdBackRequest;
import com.lsh.wms.api.service.po.IIbdBackService;
import com.lsh.wms.integration.service.common.utils.HttpUtil;
import com.lsh.wms.integration.model.OrderResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by lixin-mac on 16/9/6.
 */
@Service(protocol = "dubbo")
public class IbdBackService implements IIbdBackService{
    private static Logger logger = LoggerFactory.getLogger(IbdBackService.class);

    public String createOrderByPost(Object request, String token,String url){

        String jsonPoCreate = this.initOrderCreate(request);

        //token 缓存 2小时 获取token

        //可配置
        //String url = "http://service.wumart.com/wms/ibd";//ibd _url
        //String url = "http://service.wumart.com/wms/stockChange";// 报损报溢的


        if (token == null || token.equals("")){
            token = this.getToken(url);
        }

        OrderResponse orderResponse = new OrderResponse();
        if(token != null && !token.equals("")){
            System.out.println("order wumart CreateOrder json : " + jsonPoCreate);
            String jsonStr = HttpUtil.doPost(url,jsonPoCreate,token);
            System.out.println("order wumart res " + jsonStr);
            orderResponse = JSON.parseObject(jsonStr, OrderResponse.class);
            System.out.println("orderResponse " + orderResponse);
            logger.info("orderResponse = " + JSON.toJSONString(orderResponse));
        }else{
            logger.info("************** token is null ");
        }
        return JSON.toJSONString(orderResponse);

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
