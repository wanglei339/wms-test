package com.lsh.wms.api.model.base;

/**
 * Project Name: lsh-wms
 * Created by fuhao
 * Date: 16/7/19
 * Time: 16/7/19.
 * 北京链商电子商务有限公司
 * Package name:com.lsh.wms.api.model.base.
 * desc:类功能描述
 */
public class ResUtils {
    public static BaseResponse getResponse(Integer status,String message,Object body){
        Head head = new Head();
        head.setStatus(status);
        head.setMessage(message);
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setHead(head);
        if(null != body){
            baseResponse.setBody(body);
        }
        return baseResponse;
    }
}
