package com.lsh.wms.api.service.exception;


import com.alibaba.dubbo.rpc.protocol.rest.RpcExceptionMapper;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.wms.api.model.base.BaseResponse;
import com.lsh.wms.api.model.base.Head;
import com.lsh.wms.api.model.base.ResUtils;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
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
public class ValidateExceptionMapper extends RpcExceptionMapper {
    protected Response handleConstraintViolationException(ConstraintViolationException cve) {
        StringBuffer msg = new StringBuffer();

        for (ConstraintViolation cv : cve.getConstraintViolations()) {
            msg.append(cv.getPropertyPath().toString())
                    .append(":")
                    .append(cv.getMessage())
                    .append(",")
                    .append(cv.getInvalidValue() == null ? "null" : cv.getInvalidValue().toString())
                    .append(";");

        }
        BaseResponse responseBaseVo = ResUtils.getResponse(ExceptionConstant.RES_CODE_100,msg.toString(),null);
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseBaseVo).type(ContentType.APPLICATION_JSON_UTF_8).build();
    }
}
