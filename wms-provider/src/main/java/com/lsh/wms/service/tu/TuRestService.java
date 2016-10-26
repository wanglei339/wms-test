package com.lsh.wms.service.tu;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.api.service.tu.ITuRestService;
import com.lsh.wms.api.service.tu.ITuRpcService;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Map;

/**
 * @Author 马启迪 maqidi@lsh123.com
 * @Date 2016/10/25 下午9:25
 */
@Service(protocol = "rest")
@Path("tu")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class TuRestService implements ITuRestService {
    @Reference
    private ITuRpcService iTuRpcService;

    @POST
    @Path("getTuheadList")
    public String getTuheadList() throws BizCheckedException {
        Map<String, Object> mapRequest = RequestUtils.getRequest();
//        Long startTime = Long.valueOf(mapRequest.get("startTime").toString());    前端传回来的时间,时间需要转换
//        Long endTime = Long.valueOf(mapRequest.get("endTime").toString());
        return JsonUtils.SUCCESS(iTuRpcService.getTuHeadListOnPc(mapRequest));
    }
}
