package com.lsh.wms.rpc.service.task;

import com.alibaba.dubbo.common.json.JSON;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.service.task.ITaskRestService;
import com.lsh.wms.core.service.task.BaseTaskHandler;
import com.lsh.wms.core.service.task.StockTakingTaskHandler;
import com.lsh.wms.core.service.task.TaskHandlerFactory;
import com.lsh.wms.model.task.Operation;
import com.lsh.wms.model.task.StockTakingTask;
import com.lsh.wms.model.task.TaskInfo;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by mali on 16/7/20.
 */
@Service(protocol = "rest")
@Path("task")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class TaskRestService implements ITaskRestService {
    @Autowired
    private TaskHandlerFactory handlerFactory;

    @Autowired
    private StockTakingTaskHandler stockTakingTaskHandler;

    @GET
    @Path("create")
    public String create(@QueryParam("taskType") Long taskType,
                         @QueryParam("data") String data) {
        BaseTaskHandler taskHandler = handlerFactory.getTaskHandler(taskType);
        TaskInfo task = com.alibaba.fastjson.JSON.parseObject(data, StockTakingTask.class);
        List<Operation> list = new ArrayList<Operation>();
        taskHandler.create(task, list);
        return JsonUtils.SUCCESS();
    }

    @POST
    @Path("getList")
    public String getTaskList(Map<String, Object> mapQuery) {
        return JsonUtils.SUCCESS();
    }

}
