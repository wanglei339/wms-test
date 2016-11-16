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
import com.lsh.base.common.utils.BeanMapTransUtils;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.wms.api.service.inhouse.IStockTakingRpcService;
import com.lsh.wms.api.service.location.ILocationRpcService;
import com.lsh.wms.api.service.pick.IPickRestService;
import com.lsh.wms.api.service.pick.IPickRpcService;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.api.service.staff.IStaffRpcService;
import com.lsh.wms.api.service.system.ISysUserRpcService;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.constant.PickConstant;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.service.container.ContainerService;
import com.lsh.wms.core.service.pick.PickTaskService;
import com.lsh.wms.core.service.staff.StaffService;
import com.lsh.wms.core.service.stock.StockMoveService;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.core.service.task.MessageService;
import com.lsh.wms.core.service.utils.PackUtil;
import com.lsh.wms.core.service.wave.WaveService;
import com.lsh.wms.model.baseinfo.BaseinfoContainer;
import com.lsh.wms.model.baseinfo.BaseinfoStaffInfo;
import com.lsh.wms.model.pick.PickTaskHead;
import com.lsh.wms.model.stock.StockQuant;
import com.lsh.wms.model.system.SysUser;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.model.task.TaskMsg;
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
    @Reference
    private ISysUserRpcService iSysUserRpcService;
    @Reference
    private IPickRpcService iPickRpcService;
    @Reference
    private ILocationRpcService iLocationRpcService;
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
    @Autowired
    private MessageService messageService;
    @Reference
    private IStockTakingRpcService iStockTakingRpcService;

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
        // Long staffId = Long.valueOf(mapQuery.get("operator").toString());
        Long staffId = Long.valueOf(RequestUtils.getHeader("uid"));
        List<Map> taskList = JSON.parseArray(mapQuery.get("taskList").toString(), Map.class);
        List<WaveDetail> pickDetails = new ArrayList<WaveDetail>();
        List<Map<String, Long>> assignParams = new ArrayList<Map<String, Long>>();

        // 判断用户是否存在
        SysUser sysUser = iSysUserRpcService.getSysUserById(staffId);
        if (sysUser == null) {
            throw new BizCheckedException("2000003");
        }

        // 回溯
        Map<String, Object> restoreResult = pickTaskService.restore(staffId, taskList);

        if (restoreResult != null) {
            return JsonUtils.SUCCESS(restoreResult);
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
            pickDetails.addAll(waveService.getDetailsByPickTaskId(taskId));
        }
        iTaskRpcService.assignMul(assignParams);

        // 拣货顺序算法
        pickDetails = iPickRpcService.calcPickOrder(pickDetails);
        // 返回值按pick_order排序
        Collections.sort(pickDetails, new Comparator<WaveDetail>() {
            public int compare(WaveDetail o1, WaveDetail o2) {
                return o1.getPickOrder().compareTo(o2.getPickOrder());
            }
        });

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("next_detail", pickTaskService.renderResult(BeanMapTransUtils.Bean2map(pickDetails.get(0)), "allocPickLocation", "allocPickLocationCode"));
        result.put("done", false);
        result.put("pick_done", false);
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
        // Long staffId = Long.valueOf(mapQuery.get("operator").toString());
        Long staffId = Long.valueOf(RequestUtils.getHeader("uid"));
        String locationCode = mapQuery.get("locationCode").toString();
        Long locationId = iLocationRpcService.getLocationIdByCode(locationCode);
        Map<String, Object> result = new HashMap<String, Object>();

        // 判断用户是否存在
        SysUser sysUser = iSysUserRpcService.getSysUserById(staffId);
        if (sysUser == null) {
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
        if (needPickDetail.getPickTaskId() == null || needPickDetail.getPickTaskId().equals(0L)) {
            TaskInfo collcetTaskInfo = taskInfos.get(0); // 取第一个拣货任务
            Long taskId = collcetTaskInfo.getTaskId();
            PickTaskHead taskHead = pickTaskService.getPickTaskHead(taskId);
            Long allocCollectLocationId = taskHead.getAllocCollectLocation();
            if (!allocCollectLocationId.equals(locationId)) {
                throw new BizCheckedException("2060009");
            }
            // 完成拣货任务
            List<WaveDetail> collectWaveDetails = waveService.getDetailsByPickTaskId(taskId);
            for (WaveDetail collectWaveDetail: collectWaveDetails) {
                collectWaveDetail.setRealCollectLocation(locationId);
                waveService.updateDetail(collectWaveDetail);
            }
            iTaskRpcService.done(taskId, locationId, staffId);
            // 获取下一个集货位id
            if (taskInfos.size() > 1) {
                PickTaskHead nextTaskHead = pickTaskService.getPickTaskHead(taskInfos.get(1).getTaskId());
                result.put("next_detail", pickTaskService.renderResult(BeanMapTransUtils.Bean2map(nextTaskHead), "allocCollectLocation", "allocCollectLocationCode"));
                result.put("done", false);
            } else {
                result.put("done", true);
            }
            result.put("pick_done", true);
            return JsonUtils.SUCCESS(result);
        }
        Long taskId = needPickDetail.getPickTaskId();
        PickTaskHead taskHead = pickTaskService.getPickTaskHead(taskId);
        TaskInfo taskInfo = baseTaskService.getTaskInfoById(taskId);
        Long containerId = taskHead.getContainerId();
        // 拣货并转移库存至托盘
        if (mapQuery.get("qty") != null) {
            try {
                new BigDecimal(mapQuery.get("qty").toString());
            } catch (Exception e) {
                throw new BizCheckedException("2060013");
            }
            BigDecimal qty = new BigDecimal(mapQuery.get("qty").toString());
            // 货架拣货箱数转成EA
            if (taskInfo.getSubType().equals(PickConstant.SHELF_TASK_TYPE)) {
                qty = PackUtil.UomQty2EAQty(qty, needPickDetail.getAllocUnitName());
            }
            // 整托拣货输入的必为一托,EA数则为waveDetail中分配的数量
            if (taskInfo.getSubType().equals(PickConstant.SHELF_PALLET_TASK_TYPE)) {
                qty = needPickDetail.getAllocQty();
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
                // 整托拣货库存不足整托,则有多少捡多少
                if (taskInfo.getSubType().equals(PickConstant.SHELF_PALLET_TASK_TYPE)) {
                    qty = quantQty;
                } else {
                    throw new BizCheckedException("2060007", quantQty.toString());
                }
            }
            // 存捡合一
            if (taskInfo.getSubType().equals(PickConstant.SPLIT_TASK_TYPE) && quantQty.compareTo(allocQty) == -1) {
                BigDecimal splitQty = allocQty.subtract(quantQty);
                waveService.splitWaveDetail(needPickDetail, splitQty);
            }
            // 库移
            pickTaskService.pickOne(needPickDetail, locationId, containerId, qty, staffId);
            // 发送缺货消息(拣货缺交)
            if (allocQty.compareTo(quantQty) == 1) {
                TaskMsg msg = new TaskMsg();
                msg.setType(TaskConstant.EVENT_OUT_OF_STOCK);
                Map<String, Object> body = new HashMap<String, Object>();
                body.put("itemId", itemId);
                body.put("locationId", locationId);
                msg.setMsgBody(body);
                messageService.sendMessage(msg);
                //捡货缺交，如果捡获数量比系统记载数量，则生成盘点任务
                logger.info("-------- in create taking_task");
                Map<String,Object> queryMap = new HashMap<String, Object>();
                queryMap.put("locationId",needPickDetail.getRealPickLocation());
                BigDecimal stockQty = stockQuantService.getQty(queryMap);
                if(stockQty.compareTo(needPickDetail.getPickQty())>0) {
                    iStockTakingRpcService.create(needPickDetail.getRealPickLocation(), staffId);
                }
            }
        }
        // 获取下一个wave_detail,如已做完则获取集货位id
        Boolean pickDone = false; // 货物是否已捡完
        List<WaveDetail> nextPickDetails = waveService.getOrderedDetailsByPickTaskIds(taskIds); // 因为可能拆分,所以需要重新获取一次
        WaveDetail nextPickDetail = new WaveDetail();
        for (WaveDetail pickDetail: nextPickDetails) {
            Long pickAt = pickDetail.getPickAt();
            if (pickAt == null || pickAt.equals(0L)) {
                nextPickDetail = pickDetail;
                break;
            }
        }
        // 正常拣货的最后一步
        if (nextPickDetail.getPickTaskId() == null || nextPickDetail.getPickTaskId().equals(0L) || nextPickDetail.getPickTaskId().equals("")) {
            // 货架补拣,只做一次
            if (needPickDetail.getRefDetailId().equals(0L) && taskInfo.getSubType().equals(PickConstant.SHELF_TASK_TYPE)) {
                List<WaveDetail> splitWaveDetails = new ArrayList<WaveDetail>();
                Long lastOrder = needPickDetail.getPickOrder();
                for (WaveDetail pickDetail: pickDetails) {
                    // 判断是否少拣
                    if (pickDetail.getAllocQty().compareTo(pickDetail.getPickQty()) == 1) {
                        BigDecimal subQty = pickDetail.getAllocQty().subtract(pickDetail.getPickQty());
                        splitWaveDetails.add(waveService.splitShelfWaveDetail(pickDetail, subQty, lastOrder));
                        lastOrder++;
                    }
                }
                if (!splitWaveDetails.isEmpty()) {
                    pickDone = false;
                    result.put("next_detail", pickTaskService.renderResult(BeanMapTransUtils.Bean2map(splitWaveDetails.get(0)), "allocPickLocation", "allocPickLocationCode"));
                } else {
                    pickDone = true;
                    result.put("next_detail", pickTaskService.renderResult(BeanMapTransUtils.Bean2map(pickTaskService.getPickTaskHead(taskIds.get(0))), "allocCollectLocation", "allocCollectLocationCode")); // 返回第一个任务的头信息用于集货位分配
                }
            } else {
                pickDone = true;
                result.put("next_detail", pickTaskService.renderResult(BeanMapTransUtils.Bean2map(pickTaskService.getPickTaskHead(taskIds.get(0))), "allocCollectLocation", "allocCollectLocationCode")); // 返回第一个任务的头信息用于集货位分配
            }
        } else {
            result.put("next_detail", pickTaskService.renderResult(BeanMapTransUtils.Bean2map(nextPickDetail), "allocPickLocation", "allocPickLocationCode"));
        }
        result.put("pick_done", pickDone);
        result.put("done", false);
        return JsonUtils.SUCCESS(result);
    }

    /**
     * 回溯拣货状态
     * @return
     * @throws BizCheckedException
     */
    @POST
    @Path("restore")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
    public String restore() throws BizCheckedException {
        Map<String, Object> mapQuery = RequestUtils.getRequest();
        // Long staffId = Long.valueOf(mapQuery.get("operator").toString());
        Long staffId = Long.valueOf(RequestUtils.getHeader("uid"));
        // 判断用户是否存在
        SysUser sysUser = iSysUserRpcService.getSysUserById(staffId);
        if (sysUser == null) {
            throw new BizCheckedException("2000003");
        }
        Map<String, Object> result = pickTaskService.restore(staffId, null);
        if (result == null) {
            result = new HashMap<String, Object>();
            result.put("response", false);
        }
        return JsonUtils.SUCCESS(result);
    }

    /**
     * 更新托盘,分裂出新的拣货任务
     * @return
     * @throws BizCheckedException
     */
    @POST
    @Path("splitNewPickTask")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
    public String splitNewPickTask() throws BizCheckedException {
        Map<String, Object> mapQuery = RequestUtils.getRequest();
        Long staffId = Long.valueOf(RequestUtils.getHeader("uid"));
        Long taskId = Long.valueOf(mapQuery.get("taskId").toString());
        Long containerId = Long.valueOf(mapQuery.get("containerId").toString());
        // 判断用户是否存在
        SysUser sysUser = iSysUserRpcService.getSysUserById(staffId);
        if (sysUser == null) {
            throw new BizCheckedException("2000003");
        }
        TaskInfo oriTaskInfo = baseTaskService.getTaskInfoById(taskId);
        if (!oriTaskInfo.getType().equals(TaskConstant.TYPE_PICK)) {
            throw new BizCheckedException("2060003");
        }
        if (!oriTaskInfo.getStatus().equals(TaskConstant.Assigned)) {
            throw new BizCheckedException("2060004");
        }
        if (!oriTaskInfo.getOperator().equals(staffId)) {
            throw new BizCheckedException("2060014");
        }
        BaseinfoContainer container = containerService.getContainer(containerId);
        if (container == null) {
            throw new BizCheckedException("2000002");
        }
        // 检查container是否可用
        if (containerService.isContainerInUse(containerId)) {
            throw new BizCheckedException("2000002");
        }
        List<Long> taskIds = new ArrayList<Long>();
        taskIds.add(taskId);
        List<WaveDetail> unfinishedDetails = new ArrayList<WaveDetail>();
        List<WaveDetail> pickDetails = waveService.getOrderedDetailsByPickTaskIds(taskIds);
        // 获取未完成的detail
        for (WaveDetail pickDetail: pickDetails) {
            Long pickAt = pickDetail.getPickAt();
            if (pickAt == null || pickAt.equals(0L) || pickAt.equals("")) {
                unfinishedDetails.add(pickDetail);
            }
        }
        if (unfinishedDetails.size() < 1) {
            throw new BizCheckedException("2060015");
        }
        //unfinishedDetails = this.calcPickOrder(unfinishedDetails); // 重新排序
        PickTaskHead oriTaskHead = pickTaskService.getPickTaskHead(taskId);
        TaskEntry newTaskEntry = new TaskEntry();
        TaskInfo newTaskInfo = new TaskInfo();
        PickTaskHead newTaskHead = new PickTaskHead();
        newTaskInfo.setTaskName(oriTaskInfo.getTaskName());
        newTaskInfo.setPlanId(oriTaskInfo.getPlanId());
        newTaskInfo.setWaveId(oriTaskInfo.getWaveId());
        newTaskInfo.setOrderId(oriTaskInfo.getOrderId());
        newTaskInfo.setType(TaskConstant.TYPE_PICK);
        newTaskInfo.setSubType(oriTaskInfo.getSubType());
        newTaskInfo.setTransPlan(oriTaskInfo.getTransPlan());
        newTaskHead.setWaveId(oriTaskHead.getWaveId());
        newTaskHead.setPickType(oriTaskHead.getPickType());
        newTaskHead.setDeliveryId(oriTaskHead.getDeliveryId());
        newTaskHead.setAllocCollectLocation(oriTaskHead.getAllocCollectLocation());
        newTaskEntry.setTaskInfo(newTaskInfo);
        newTaskEntry.setTaskHead(newTaskHead);
        Long newTaskId = iTaskRpcService.create(TaskConstant.TYPE_PICK, newTaskEntry);
        iTaskRpcService.assign(newTaskId, staffId, containerId); // 直接分配给该用户
        for (WaveDetail unfinishedDetail: unfinishedDetails) {
            unfinishedDetail.setPickTaskId(newTaskId);
        }
        waveService.updateDetails(unfinishedDetails); // 关联未完成的waveDetail到新的拣货任务上
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("taskId", newTaskId);
        return JsonUtils.SUCCESS(result);
    }
}
