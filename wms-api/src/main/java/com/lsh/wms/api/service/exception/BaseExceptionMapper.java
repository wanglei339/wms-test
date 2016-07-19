package com.lsh.wms.api.service.exception;

import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BaseCheckedException;
import com.lsh.wms.api.model.base.BaseResponse;
import com.lsh.wms.api.model.base.Head;
import com.lsh.wms.api.model.base.ResUtils;
import com.lsh.wms.api.model.base.ResponseConstant;

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
public class BaseExceptionMapper implements ExceptionMapper<BaseCheckedException> {
    public Response toResponse(BaseCheckedException ex) {
        StringBuffer msg = new StringBuffer();
        msg.append(ex.getMessage());
        msg.append(" case by :");
        msg.append(ex.getExceptionStackInfo());
        Integer status = ex.getCode()!=null?Integer.parseInt(ex.getCode()):ExceptionConstant.RES_CODE_500;
        BaseResponse responseBaseVo = ResUtils.getResponse(status,msg.toString(),null);
        Response response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseBaseVo).type(ContentType.APPLICATION_JSON_UTF_8).build();
        return  response;
    }
}
