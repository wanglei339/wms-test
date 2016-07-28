package com.lsh.wms.service.inbound;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.BeanMapTransUtils;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.api.service.shelve.IShelveRestService;
import com.lsh.wms.api.service.task.ITaskRestService;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.model.shelve.ShelveTaskHead;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskInfo;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fengkun on 16/7/28.
 */

@Service(protocol = "rest")
@Path("task")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class ShelveRestService implements IShelveRestService {
    @Reference
    private ITaskRpcService iTaskRpcService;

    /**
     * 扫描需上架的容器id
     * @return
     * @throws BizCheckedException
     */
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
        final Long taskId = iTaskRpcService.create(taskType, entry);
        iTaskRpcService.assign(taskId, staffId);
        return JsonUtils.SUCCESS(new HashMap<String, Long>() {
            {
                put("taskId", taskId);
            }
        });
    }

    /**
     * 扫描上架目标location_id
     * @param taskId
     * @param locationId
     * @return
     * @throws BizCheckedException
     */
    @GET
    @Path("scanTargetLocation")
    public String scanTargetLocation(@QueryParam("taskId") Long taskId, @QueryParam("locationId") Long locationId) throws BizCheckedException {
        TaskEntry entry = iTaskRpcService.getTaskEntryById(taskId);
        if(entry == null){
            return JsonUtils.EXCEPTION_ERROR();
        }
        iTaskRpcService.done(taskId, locationId);
        return "true";
    }
}
