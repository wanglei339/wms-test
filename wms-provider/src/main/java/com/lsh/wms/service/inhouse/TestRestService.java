package com.lsh.wms.service.inhouse;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.alibaba.fastjson.JSON;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.api.service.task.ITestRestService;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.model.taking.StockTakingHead;
import com.lsh.wms.model.task.StockTakingTask;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskInfo;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by mali on 16/7/23.
 */


@Service(protocol = "rest")
@Path("inhouse/test")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class TestRestService implements ITestRestService {
    @Reference
    private ITaskRpcService iTaskRpcService;

    @POST
    @Path("init")
    public String init(TaskInfo taskInfo) throws BizCheckedException{

        TaskEntry taskEntry = new TaskEntry();
        taskInfo.setType(TaskConstant.TYPE_STOCK_TAKING);
        taskInfo.setOperator(101L);
        taskEntry.setTaskInfo(taskInfo);

        StockTakingTask taskHead = new StockTakingTask();
        taskHead.setRound(8L);
        taskHead.setTakingId(taskInfo.getPlanId());
        taskEntry.setTaskHead(taskHead);

        iTaskRpcService.create(TaskConstant.TYPE_STOCK_TAKING, taskEntry);

        return JsonUtils.SUCCESS();
    }

}
