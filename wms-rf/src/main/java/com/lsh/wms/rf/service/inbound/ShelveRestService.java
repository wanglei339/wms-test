package com.lsh.wms.rf.service.inbound;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.BeanMapTransUtils;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.api.service.shelve.IShelveRestService;
import com.lsh.wms.api.service.task.ITaskRestService;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.service.shelve.ShelveTaskService;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.model.shelve.ShelveTaskHead;
import com.lsh.wms.model.stock.StockQuant;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fengkun on 16/7/28.
 */

@Service(protocol = "rest")
@Path("inbound/shelve")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class ShelveRestService implements IShelveRestService {
    @Reference
    private ITaskRpcService iTaskRpcService;
    @Autowired
    private BaseTaskService baseTaskService;
    @Autowired
    private StockQuantService stockQuantService;
    @Autowired
    private ShelveTaskService shelveTaskService;

    /**
     * 创建上架任务
     * @return
     * @throws BizCheckedException
     */
    @POST
    @Path("createTask")
    public String createTask() throws BizCheckedException {
        Map<String, Object> mapQuery = RequestUtils.getRequest();
        if(mapQuery.get("type")==null) {
            JsonUtils.EXCEPTION_ERROR();
        }
        Long taskType = Long.valueOf(mapQuery.get("type").toString());
        Long containerId = Long.valueOf(mapQuery.get("containerId").toString());
        // 检查容器信息
        if (containerId == null || containerId.equals("")) {
            throw new BizCheckedException("2030003");
        }
        // 检查该容器是否已创建过任务
        if (baseTaskService.checkTaskByContainerId(containerId)) {
            throw new BizCheckedException("2030008");
        }
        // 获取quant
        List<StockQuant> quants = stockQuantService.getQuantsByContainerId(containerId);
        if (quants.size() < 1) {
            throw new BizCheckedException("2030001");
        }
        StockQuant quant = quants.get(0);

        TaskInfo taskInfo = new TaskInfo();
        ShelveTaskHead taskHead = new ShelveTaskHead();
        TaskEntry entry = new TaskEntry();

        ObjUtils.bean2bean(quant, taskInfo);
        ObjUtils.bean2bean(quant, taskHead);

        taskInfo.setFromLocationId(quant.getLocationId());

        entry.setTaskInfo(taskInfo);
        entry.setTaskHead(taskHead);

        final Long taskId = iTaskRpcService.create(taskType, entry);
        return JsonUtils.SUCCESS(new HashMap<String, Long>() {
            {
                put("taskId", taskId);
            }
        });
    }

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
        Long containerId = Long.valueOf(mapQuery.get("containerId").toString());
        Long taskId = baseTaskService.getDraftTaskIdByContainerId(containerId);
        iTaskRpcService.assign(taskId, staffId);
        ShelveTaskHead taskHead = shelveTaskService.getShelveTaskHead(taskId);
        return JsonUtils.SUCCESS(taskHead);
    }

    /**
     * 扫描上架目标location_id
     * @return
     * @throws BizCheckedException
     */
    @POST
    @Path("scanTargetLocation")
    public String scanTargetLocation() throws BizCheckedException {
        Map<String, Object> mapQuery = RequestUtils.getRequest();
        Long taskId = Long.valueOf(mapQuery.get("taskId").toString());
        Long locationId = Long.valueOf(mapQuery.get("locationId").toString());
        TaskEntry entry = iTaskRpcService.getTaskEntryById(taskId);
        if(entry == null){
            return JsonUtils.EXCEPTION_ERROR();
        }
        iTaskRpcService.done(taskId, locationId);
        return JsonUtils.SUCCESS(true);
    }
}
