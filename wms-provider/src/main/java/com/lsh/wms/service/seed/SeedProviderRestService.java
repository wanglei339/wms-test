package com.lsh.wms.service.seed;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.api.service.seed.ISeedProveiderRestService;
import com.lsh.wms.core.service.seed.SeedTaskHeadService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Map;

/**
 * Created by lixin-mac on 2016/10/21.
 */

@Service(protocol = "rest")
@Path("seed")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class SeedProviderRestService implements ISeedProveiderRestService{

    @Autowired
    private SeedTaskHeadService seedTaskHeadService;




    @POST
    @Path("countSeedTask")
    public String countSeedTask() {
        Map<String,Object> request = RequestUtils.getRequest();
        return JsonUtils.SUCCESS(seedTaskHeadService.countHead(request));
    }
    @POST
    @Path("getSeedTask")
    public String getSeedTask() {
        Map<String,Object> request = RequestUtils.getRequest();

        return JsonUtils.SUCCESS(seedTaskHeadService.getHeadList(request));
    }
    @GET
    @Path("getSeedTaskByContainerId")
    public String getSeedTask(@QueryParam("realContainerId") Long realContainerId) {
        return JsonUtils.SUCCESS(seedTaskHeadService.getTaskByRealContainerId(realContainerId));
    }

}
