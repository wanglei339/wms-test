package com.lsh.wms.rpc.service.system;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.service.system.ISysLogRestService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Map;

/**
 * Created by lixin-mac on 2016/10/24.
 */

@Service(protocol = "rest")
@Path("sysLog")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class SysLogRestService implements ISysLogRestService{

    @Autowired
    private SysLogRpcService sysLogRpcService;

    @POST
    @Path("getSysLogList")
    public String getSysLogList(Map<String, Object> params) {
        return JsonUtils.SUCCESS(sysLogRpcService.getSysLogList(params));
    }

    @POST
    @Path("countSysLog")
    public String countSysLog(Map<String, Object> params) {
        return JsonUtils.SUCCESS(sysLogRpcService.countSysLog(params));
    }

    @GET
    @Path("retransmission")
    public String retransmission(@QueryParam("logId") Long logId) throws BizCheckedException {

        sysLogRpcService.Retransmission(logId);

        return JsonUtils.SUCCESS();
    }
}
