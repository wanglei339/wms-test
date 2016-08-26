package com.lsh.wms.rpc.service.kanban;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.service.kanban.IKanBanRestService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by lixin-mac on 16/8/26.
 */

@Service(protocol = "rest")
@Path("kanban")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class KanBanRestService implements IKanBanRestService {

    @Autowired
    private KanBanRpcService kanBanRpcService;

    @GET
    @Path("getKanbanCount")
    public String getKanbanCount(@QueryParam("type") Long type) {
        return JsonUtils.SUCCESS(kanBanRpcService.getKanbanCount(type));
    }
}
