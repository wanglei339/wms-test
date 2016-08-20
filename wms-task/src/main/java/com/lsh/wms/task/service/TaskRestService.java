package com.lsh.wms.task.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.BeanMapTransUtils;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.api.service.task.ITaskRestService;
import com.lsh.wms.core.service.staff.StaffService;
import com.lsh.wms.core.service.task.MessageService;
import com.lsh.wms.model.baseinfo.BaseinfoStaffInfo;
import com.lsh.wms.model.shelve.ShelveTaskHead;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.model.task.TaskMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.SystemEnvironmentPropertySource;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
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

    @Autowired
    private MessageService messageService;

    @Autowired
    private StaffService staffService;

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

    @GET
    @Path("done")
    public String done(@QueryParam("taskId") long taskId) throws BizCheckedException {
        taskRpcService.afterDone(taskId);
        return JsonUtils.SUCCESS();
    }

    @POST
    @Path("getPerformance")
    public String getPerformance(Map<String, Object> mapQuery) {
        List<Map<String,Object>> result = new ArrayList<Map<String, Object>>();
        List<BaseinfoStaffInfo> staffList =  staffService.getStaffList(mapQuery);
        for(BaseinfoStaffInfo staff : staffList) {
            mapQuery.put("staffId", staff.getStaffId());
            List<Map<String, Object>> stat = taskRpcService.getPerformance(mapQuery);
            result.addAll(stat);
        }
        return JsonUtils.SUCCESS(staffList);
    }
}
