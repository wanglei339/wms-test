package com.lsh.wms.api.model.base;

import com.lsh.base.common.json.JsonUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by peter on 16/7/6.
 */
public class BaseResponse implements Serializable{
    private Head head;
    private Object body = new HashMap<String,Object>();

    public Head getHead() {
        return head;
    }

    public void setHead(Head head) {
        this.head = head;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public static void  main(String args[]){
        Head head = new Head();
        head.setStatus(0);
        head.setMessage("1212");
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setHead(head);
        System.out.println(JsonUtils.obj2Json(baseResponse));
        System.out.println(JsonUtils.obj2Json(ResUtils.getResponse(ResponseConstant.RES_CODE_0,"ok",null)));
    }
}
