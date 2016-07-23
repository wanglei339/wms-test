package com.lsh.wms.service.inhouse;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.alibaba.fastjson.JSON;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.model.taking.StockTakingDetail;
import com.lsh.wms.model.taking.StockTakingHead;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.task.service.TaskRpcService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by mali on 16/7/23.
 */


@Service(protocol = "rest")
@Path("inhouse/test")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class TestRestService {
    @Autowired
    private TaskRpcService taskRpcService;

    @POST
    @Path("create")
    public String create(String stockTakingInfo) {
        StockTakingHead head = JSON.parseObject(stockTakingInfo, StockTakingHead.class);
        TaskEntry taskEntry = new TaskEntry();
        return JsonUtils.SUCCESS();
    }

}
