package com.lsh.wms.api.service.exception;

import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.wms.api.model.base.BaseResponse;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import java.util.Date;

/**
 * Project Name: lsh-wms
 * Created by fuhao
 * Date: 16/7/16
 * Time: 16/7/16.
 * 北京链商电子商务有限公司
 * Package name:com.lsh.wms.api.service.exception.
 * desc:类功能描述
 */
public class SysExceptionMapper implements ExceptionMapper<Throwable> {
    public  Response toResponse(Throwable throwable) {
        BaseResponse responseBaseVo = new BaseResponse();
        responseBaseVo.setDataKey(new Date());
        responseBaseVo.setStatus(ExceptionConstant.RES_CODE_500);
        StringBuffer msg = new StringBuffer();
        msg.append(throwable.getMessage());
        msg.append(" case by :");
        msg.append(throwable.getCause()!=null?throwable.getCause().getMessage():"");
        responseBaseVo.setMsg(msg.toString());
        Response response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseBaseVo).type(ContentType.APPLICATION_JSON_UTF_8).build();
        return  response;
    }

    public static void main(String args[]){
        SysExceptionMapper wmsSysExceptionMapper = new SysExceptionMapper();
        Exception exception = new Exception();
        wmsSysExceptionMapper.toResponse(exception);
    }
}
