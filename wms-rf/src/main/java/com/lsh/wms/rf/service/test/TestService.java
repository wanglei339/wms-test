package com.lsh.wms.rf.service.test;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.api.model.base.BaseResponse;
import com.lsh.wms.api.model.base.ResUtils;
import com.lsh.wms.api.model.base.ResponseConstant;
import com.lsh.wms.api.model.so.SoRequest;
import com.lsh.wms.api.service.ITestService;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.core.service.system.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by mali on 16/11/17.
 */

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import java.util.Map;

@Service(protocol = "rest")
@Path("test")
@Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class TestService implements ITestService {

    @Autowired
    private SysLogService sysLogService;

    @POST
    @Path("test")
    public void test() throws BizCheckedException {
        Map<String, Object> mapQuery = RequestUtils.getRequest();
        Long type = Long.valueOf(mapQuery.get("type").toString());
        sysLogService.getAndLockSysLogByType(type);
    }
}
