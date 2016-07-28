package com.lsh.wms.task.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.BeanMapTransUtils;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.api.service.task.ITaskRestService;
import com.lsh.wms.model.shelve.ShelveTaskHead;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.SystemEnvironmentPropertySource;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zengwenjun on 16/7/24.
 */
@Service(protocol = "rest")
@Path("task")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class TaskRestService implements ITaskRestService {
    @Autowired
    private TaskRpcService taskRpcService;

    @POST
    @Path("getTaskList")
    public String getTaskList(Map<String, Object> mapQuery) {
        if(mapQuery.get("type")==null){
            JsonUtils.EXCEPTION_ERROR();
        }
        Long taskType = Long.valueOf(mapQuery.get("type").toString());
        List<TaskEntry> taskEntries = taskRpcService.getTaskList(taskType, mapQuery);
        return JsonUtils.SUCCESS(taskEntries);
    }

    @POST
    @Path("getTaskCount")
    public String getTaskCount(Map<String, Object> mapQuery) {
        if(mapQuery.get("type")==null){
            JsonUtils.EXCEPTION_ERROR();
        }
        Long taskType =  Long.valueOf(mapQuery.get("type").toString());
        int num = taskRpcService.getTaskCount(taskType, mapQuery);
        return JsonUtils.SUCCESS(num);
    }

    @POST
    @Path("getTaskHeadList")
    public String getTaskHeadList(Map<String, Object> mapQuery) {
        if(mapQuery.get("type")==null){
            JsonUtils.EXCEPTION_ERROR();
        }
        Long taskType = Long.valueOf(mapQuery.get("type").toString());
        List<TaskEntry> taskEntries = taskRpcService.getTaskHeadList(taskType, mapQuery);
        return JsonUtils.SUCCESS(taskEntries);
    }

    @GET
    @Path("getTask")
    public String getTask(@QueryParam("taskId") long taskId) throws BizCheckedException{
        TaskEntry entry = taskRpcService.getTaskEntryById(taskId);
        return JsonUtils.SUCCESS(entry);
    }

    @GET
    @Path("getTaskMove")
    public String getTaskMove(@QueryParam("taskId") long taskId) throws BizCheckedException {
        TaskEntry entry = taskRpcService.getTaskEntryById(taskId);
        if(entry == null){
            return JsonUtils.EXCEPTION_ERROR();
        }
        return JsonUtils.SUCCESS(entry.getStockMoveList());
    }

    @POST
    @Path("scanContainer")
    public String scanContainer() throws BizCheckedException {
        Map<String, Object> mapQuery = RequestUtils.getRequest();
        if(mapQuery.get("type")==null) {
            JsonUtils.EXCEPTION_ERROR();
        }
        Long taskType = Long.valueOf(mapQuery.get("type").toString());
        Long staffId = Long.valueOf(mapQuery.get("operator").toString());
        TaskInfo taskInfo = BeanMapTransUtils.map2Bean(mapQuery, TaskInfo.class);
        ShelveTaskHead taskHead = BeanMapTransUtils.map2Bean(mapQuery, ShelveTaskHead.class);
        TaskEntry entry = new TaskEntry();
        entry.setTaskInfo(taskInfo);
        entry.setTaskHead(taskHead);
        final Long taskId = taskRpcService.create(taskType, entry);
        taskRpcService.assign(taskId, staffId);
        return JsonUtils.SUCCESS(new HashMap<String, Long>() {
            {
                put("taskId", taskId);
            }
        });
    }

    @GET
    @Path("scanTargetLocation")
    public String scanTargetLocation(@QueryParam("taskId") Long taskId, @QueryParam("locationId") Long locationId) throws BizCheckedException {
        TaskEntry entry = taskRpcService.getTaskEntryById(taskId);
        if(entry == null){
            return JsonUtils.EXCEPTION_ERROR();
        }
        taskRpcService.done(taskId, locationId);
        return "true";
    }
}
