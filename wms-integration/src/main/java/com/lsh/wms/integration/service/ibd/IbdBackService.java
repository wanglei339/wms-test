package com.lsh.wms.integration.service.ibd;


import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.lsh.wms.api.model.po.IbdBackRequest;
import com.lsh.wms.api.service.po.IIbdBackService;
import com.lsh.wms.core.constant.RedisKeyConstant;
import com.lsh.wms.core.dao.redis.RedisStringDao;
import com.lsh.wms.integration.service.common.utils.HttpUtil;
import com.lsh.wms.integration.model.OrderResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by lixin-mac on 16/9/6.
 */
@Service(protocol = "dubbo",async=true)
public class IbdBackService implements IIbdBackService{
    private static Logger logger = LoggerFactory.getLogger(IbdBackService.class);


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
