package com.lsh.wms.rf.service.outbound;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.service.pick.IPickRestService;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.service.pick.PickTaskService;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.model.wave.WaveDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.config.Task;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

/**
 * Created by fengkun on 16/8/4.
 */
@Service(protocol = "rest")
@Path("outbound/pick")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class PickRestService implements IPickRestService {
    private static Logger logger = LoggerFactory.getLogger(PickRestService.class);

    private Long taskType = TaskConstant.TYPE_PICK;

    @Reference
    private ITaskRpcService iTaskRpcService;
    @Autowired
    private BaseTaskService baseTaskService;
    @Autowired
    private PickTaskService pickTaskService;

    /**
     * 创建拣货任务(测试用)
     * @return
     * @throws BizCheckedException
     */
    @POST
    @Path("createTask")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
    public String createTask() throws BizCheckedException {
        Map<String, Object> mapQuery = RequestUtils.getRequest();
        return JsonUtils.SUCCESS(123);
    }

    /**
     * 扫描拣货签(拣货任务id)
     * @return
     * @throws BizCheckedException
     */
    @POST
    @Path("scanPickTask")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
    public String scanPickTask() throws BizCheckedException {
        Map<String, Object> mapQuery = RequestUtils.getRequest();
        Long taskId = Long.valueOf(mapQuery.get("taskId").toString());
        Long staffId = Long.valueOf(mapQuery.get("operator").toString());
        TaskInfo taskInfo = baseTaskService.getTaskInfoById(taskId);
        if (taskInfo == null || !taskInfo.getType().equals(taskType)) {
            throw new BizCheckedException("2060003");
        }
        iTaskRpcService.assign(taskId, staffId);
        List<WaveDetail> pickDetails = pickTaskService.getPickTaskDetails(taskId);
        // TODO: 拣货顺序算法
        return JsonUtils.SUCCESS(pickDetails);
    }

    /**
     * 扫描拣货位进行拣货
     * @return
     * @throws BizCheckedException
     */
    @POST
    @Path("scanPickLocation")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
    public String scanPickLocation() throws BizCheckedException {
        Map<String, Object> mapQuery = RequestUtils.getRequest();
        Long taskId = Long.valueOf(mapQuery.get("taskId").toString());
        Long staffId = Long.valueOf(mapQuery.get("operator").toString());
        return JsonUtils.SUCCESS(123);
    }
}
