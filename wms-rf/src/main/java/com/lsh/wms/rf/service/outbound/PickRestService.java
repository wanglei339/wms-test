package com.lsh.wms.rf.service.outbound;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.wms.api.service.pick.IPickRestService;
import com.lsh.wms.api.service.pick.IPickRpcService;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.api.service.staff.IStaffRpcService;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.service.container.ContainerService;
import com.lsh.wms.core.service.pick.PickTaskService;
import com.lsh.wms.core.service.staff.StaffService;
import com.lsh.wms.core.service.stock.StockMoveService;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.core.service.wave.WaveService;
import com.lsh.wms.model.baseinfo.BaseinfoContainer;
import com.lsh.wms.model.baseinfo.BaseinfoStaffInfo;
import com.lsh.wms.model.pick.PickTaskHead;
import com.lsh.wms.model.stock.StockQuant;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.model.wave.WaveDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.config.Task;
import org.w3c.dom.ls.LSException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.util.*;

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
    private IStaffRpcService iStaffRpcService;
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
        Long staffId = Long.valueOf(mapQuery.get("operator").toString());
        List<Map> taskList = JSON.parseArray(mapQuery.get("taskList").toString(), Map.class);
        final List<WaveDetail> pickDetails = new ArrayList<WaveDetail>();
        List<Map<String, Long>> assignParams = new ArrayList<Map<String, Long>>();

        // 判断用户是否存在
        BaseinfoStaffInfo staffInfo = iStaffRpcService.getStaffById(staffId);
        if (staffInfo == null) {
            throw new BizCheckedException("2000003");
        }

        // 判断该用户是否已领取过拣货任务
        List<TaskInfo> assignedTaskInfos = baseTaskService.getAssignedTaskByOperator(staffId, TaskConstant.TYPE_PICK);
        if (assignedTaskInfos.size() > 0) {
            throw new BizCheckedException("2060011");
        }

        for (Map<String, Object> task: taskList) {
            Long taskId = Long.valueOf(task.get("taskId").toString());
            Long containerId = Long.valueOf(task.get("containerId").toString());
            Map<String, Long> assignParam = new HashMap<String, Long>();
            BaseinfoContainer container = containerService.getContainer(containerId);
            if (container == null) {
                throw new BizCheckedException("2000002");
            }
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
            assignParam.put("taskId", taskId);
            assignParam.put("staffId", staffId);
            assignParam.put("containerId", containerId);
            assignParams.add(assignParam);
            pickDetails.addAll(pickTaskService.getPickTaskDetails(taskId));
        }
        iTaskRpcService.assignMul(assignParams);

        // 拣货顺序算法
        iPickRpcService.calcPickOrder(pickDetails);
        // 返回值按pick_order排序
        Collections.sort(pickDetails, new Comparator<WaveDetail>() {
            public int compare(WaveDetail o1, WaveDetail o2) {
                return o1.getPickOrder().compareTo(o2.getPickOrder());
            }
        });
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("pick_details", pickDetails);
        return JsonUtils.SUCCESS(result);
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
        Long staffId = Long.valueOf(mapQuery.get("operator").toString());
        Long locationId = Long.valueOf(mapQuery.get("locationId").toString());
        Map<String, Object> result = new HashMap<String, Object>();

        // 判断用户是否存在
        BaseinfoStaffInfo staffInfo = iStaffRpcService.getStaffById(staffId);
        if (staffInfo == null) {
            throw new BizCheckedException("2000003");
        }

        // 获取分配给操作人员的所有拣货任务
        Map<String, Object> taskInfoParams = new HashMap<String, Object>();
        taskInfoParams.put("operator", staffId);
        taskInfoParams.put("type", TaskConstant.TYPE_PICK);
        taskInfoParams.put("status", TaskConstant.Assigned);
        List<TaskInfo> taskInfos = baseTaskService.getTaskInfoList(taskInfoParams);

        if (taskInfos == null) {
            throw new BizCheckedException("2060003");
        }

        // 取taskId
        List<Long> taskIds = new ArrayList<Long>();

        for (TaskInfo taskInfo: taskInfos) {
            taskIds.add(taskInfo.getTaskId());
        }

        // 取排好序的拣货详情
        if (taskIds.size() < 1) {
            throw new BizCheckedException("2060010");
        }
        List<WaveDetail> pickDetails = waveService.getOrderedDetailsByPickTaskIds(taskIds);
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
            TaskInfo collcetTaskInfo = taskInfos.get(0); // 取第一个拣货任务
            Long taskId = collcetTaskInfo.getTaskId();
            PickTaskHead taskHead = pickTaskService.getPickTaskHead(taskId);
            Long allocCollectLocationId = taskHead.getAllocCollectLocation();
            if (!allocCollectLocationId.equals(locationId)) {
                throw new BizCheckedException("2060009");
            }
            // 完成拣货任务
            iTaskRpcService.done(taskId, locationId, staffId);
            // 获取下一个拣货位id
            if (taskInfos.size() > 1) {
                PickTaskHead nextTaskHead = pickTaskService.getPickTaskHead(taskInfos.get(1).getTaskId());
                result.put("next_collection", nextTaskHead);
            }
            result.put("done", true);
            return JsonUtils.SUCCESS(result);
        }
        Long taskId = needPickDetail.getPickTaskId();
        PickTaskHead taskHead = pickTaskService.getPickTaskHead(taskId);
        Long containerId = taskHead.getContainerId();
        // 拣货并转移库存至托盘
        if (mapQuery.get("qty") != null) {
            BigDecimal qty = BigDecimal.valueOf(Double.valueOf(mapQuery.get("qty").toString()));
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
        }
        // 获取下一个wave_detail,如已做完则获取集货位id
        Long nextPickOrder = needPickDetail.getPickOrder() + 1;
        Boolean pickDone = false; // 货物是否已捡完
        WaveDetail nextPickDetail = new WaveDetail();
        for (WaveDetail pickDetail: pickDetails) {
            if (pickDetail.getPickOrder().equals(nextPickOrder)) {
                nextPickDetail = pickDetail;
            }
        }
        if (nextPickDetail.getPickTaskId() == null || nextPickDetail.getPickTaskId().equals(0L)) {
            pickDone = true;
            result.put("next_collection", pickTaskService.getPickTaskHead(taskIds.get(0))); // 返回第一个任务的头信息用于集货位分配
        } else {
            result.put("next_detail", nextPickDetail);
        }
        result.put("done", pickDone);
        return JsonUtils.SUCCESS(result);
    }
}
