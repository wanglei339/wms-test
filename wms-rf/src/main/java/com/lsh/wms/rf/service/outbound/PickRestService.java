package com.lsh.wms.rf.service.outbound;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.service.pick.IPickRestService;
import com.lsh.wms.api.service.pick.IPickRpcService;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.service.container.ContainerService;
import com.lsh.wms.core.service.pick.PickTaskService;
import com.lsh.wms.core.service.stock.StockMoveService;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.core.service.wave.WaveService;
import com.lsh.wms.model.pick.PickTaskHead;
import com.lsh.wms.model.stock.StockQuant;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.model.wave.WaveDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.config.Task;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.util.HashMap;
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
    @Autowired
    private ContainerService containerService;
    @Autowired
    private WaveService waveService;
    @Autowired
    private StockQuantService stockQuantService;
    @Reference
    private IPickRpcService iPickRpcService;

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
        Long containerId = Long.valueOf(mapQuery.get("containerId").toString());
        TaskInfo taskInfo = baseTaskService.getTaskInfoById(taskId);
        PickTaskHead taskHead = pickTaskService.getPickTaskHead(taskId);
        if (!taskInfo.getStatus().equals(TaskConstant.Draft)) {
            throw new BizCheckedException("2060001");
        }
        if (taskInfo == null || !taskInfo.getType().equals(taskType)) {
            throw new BizCheckedException("2060003");
        }
        // 检查是否有已分配的任务
        if (baseTaskService.checkTaskByContainerId(containerId)) {
            throw new BizCheckedException("2030008");
        }
        // 检查container是否可用
        if (containerService.isContainerInUse(containerId)) {
            throw new BizCheckedException("2000002");
        }
        iTaskRpcService.assign(taskId, staffId, containerId);
        List<WaveDetail> pickDetails = pickTaskService.getPickTaskDetails(taskId);
        // 拣货顺序算法
        iPickRpcService.calcPickOrder(pickDetails);
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
        Long locationId = Long.valueOf(mapQuery.get("locationId").toString());
        BigDecimal qty = BigDecimal.valueOf(Double.valueOf(mapQuery.get("qty").toString()));
        TaskInfo taskInfo = baseTaskService.getTaskInfoById(taskId);
        PickTaskHead taskHead = pickTaskService.getPickTaskHead(taskId);
        Long containerId = taskHead.getContainerId();
        if (taskInfo == null) {
            throw new BizCheckedException("2060003");
        }
        if (!taskInfo.getStatus().equals(TaskConstant.Assigned)) {
            throw new BizCheckedException("2060004");
        }
        List<WaveDetail> pickDetails = waveService.getOrderedDetailsByPickTaskId(taskId);
        // 查找最后未完成的任务
        WaveDetail needPickDetail = new WaveDetail();
        for (WaveDetail pickDetail : pickDetails) {
            Long pickAt = pickDetail.getPickAt();
            if (pickAt == null || pickAt.equals(0L)) {
                needPickDetail = pickDetail;
                break;
            }
        }
        // 全部捡完,则需要扫描集货位
        if (needPickDetail.getPickTaskId() == null || needPickDetail.getPickTaskId().equals("")) {
            Long allocCollectLocationId = taskHead.getAllocCollectLocation();
            if (!allocCollectLocationId.equals(locationId)) {
                throw new BizCheckedException("2060009");
            }
            // 完成拣货任务
            iTaskRpcService.done(taskId, locationId, staffId);
            return JsonUtils.SUCCESS(new HashMap<String, Object>(){
                {
                    put("done", true);
                }
            });
        }
        Long allocLocationId = needPickDetail.getAllocPickLocation();
        // 判断是否与分配拣货位一致
        if (!allocLocationId.equals(locationId)) {
            throw new BizCheckedException("2060005");
        }
        Long itemId = needPickDetail.getItemId();
        // 判断拣货数量与库存数量
        BigDecimal allocQty = needPickDetail.getAllocQty();
        BigDecimal quantQty = stockQuantService.getQuantQtyByLocationIdAndItemId(locationId, itemId);
        if (qty.compareTo(new BigDecimal(0)) == -1) {
            throw new BizCheckedException("2060008");
        }
        if (qty.compareTo(allocQty) == 1) {
            throw new BizCheckedException("2060006");
        }
        if (allocQty.compareTo(quantQty) == 1 && qty.compareTo(quantQty) == 1) {
            throw new BizCheckedException("2060007", quantQty.toString());
        }
        // 库移
        pickTaskService.pickOne(needPickDetail, locationId, containerId, qty, staffId);
        return JsonUtils.SUCCESS(new HashMap<String, Object>(){
            {
                put("done", false);
            }
        });
    }
}
