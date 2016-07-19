package com.lsh.wms.api.service.request;

import com.alibaba.dubbo.rpc.RpcContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Project Name: lsh-wms
 * Created by fuhao
 * Date: 16/7/19
 * Time: 16/7/19.
 * 北京链商电子商务有限公司
 * Package name:com.lsh.wms.api.service.request.
 * desc:类功能描述
 */
public class RequestUtils {
    private static final Logger logger = LoggerFactory.getLogger(RequestUtils.class);
    public static Map<String,String> getRequest(){
        HttpServletRequest request = (HttpServletRequest) RpcContext.getContext().getRequest();
        Map<String, String[]> paramMap = request.getParameterMap();
        Map<String,String> requestMap = new HashMap<String, String>();
        for (Map.Entry<String, String[]> entry : paramMap.entrySet()) {
            logger.debug("key= " + entry.getKey() + " and value= " + entry.getValue());

            String[] parameterValues = entry.getValue();
            if (parameterValues != null && parameterValues.length > 0) {
                requestMap.put(entry.getKey(),entry.getValue()[0]);
            }
        }
        return  requestMap;
    }
}
