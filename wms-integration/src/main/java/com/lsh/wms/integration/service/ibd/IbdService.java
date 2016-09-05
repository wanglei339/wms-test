package com.lsh.wms.integration.service.ibd;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.wms.api.model.base.BaseResponse;
import com.lsh.wms.api.model.base.ResUtils;
import com.lsh.wms.api.model.base.ResponseConstant;
import com.lsh.wms.api.model.po.IbdRequest;
import com.lsh.wms.api.service.po.IIbdService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by mali on 16/9/2.
 */
@Service(protocol = "rest", validation = "true")
@Path("ibd")
@Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class IbdService implements IIbdService {
    private static Logger logger = LoggerFactory.getLogger(IbdService.class);

    @POST
    @Path("add")
    public BaseResponse add(IbdRequest request) {
        return ResUtils.getResponse(ResponseConstant.RES_CODE_1, ResponseConstant.RES_MSG_OK, null);
    }
}
