package com.lsh.wms.rf.service.outbound;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.alibaba.fastjson.JSON;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.service.merge.IMergeRfRestService;
import com.lsh.wms.api.service.merge.IMergeRpcService;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.constant.ContainerConstant;
import com.lsh.wms.core.constant.ItemConstant;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.service.container.ContainerService;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.merge.MergeService;
import com.lsh.wms.core.service.so.SoOrderService;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.core.service.utils.IdGenerator;
import com.lsh.wms.core.service.utils.PackUtil;
import com.lsh.wms.core.service.wave.WaveService;
import com.lsh.wms.model.baseinfo.BaseinfoContainer;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.so.ObdHeader;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.model.wave.WaveDetail;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fengkun on 2016/10/11.
 */
@Service(protocol = "rest")
@Path("outbound/merge")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class MergeRfRestService implements IMergeRfRestService {
    private static Logger logger = LoggerFactory.getLogger(MergeRfRestService.class);

    @Autowired
    private MergeService mergeService;
    @Autowired
    private WaveService waveService;
    @Autowired
    private SoOrderService soOrderService;
    @Autowired
    private IdGenerator idGenerator;
    @Autowired
    private BaseTaskService baseTaskService;

    @Reference
    private IMergeRpcService iMergeRpcService;
    @Reference
    private ITaskRpcService iTaskRpcService;
    @Autowired
    private ItemService itemService;

    /**
     * 扫描托盘码进行合板
     *
     * @return
     * @throws BizCheckedException
     */
    @POST
    @Path("mergeContainers")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON})
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
    public String mergeContainers() throws BizCheckedException {
        Map<String, Object> mapQuery = RequestUtils.getRequest();
        Long staffId = Long.valueOf(RequestUtils.getHeader("uid"));
        List<Long> containerIds = new ArrayList<Long>();
        List<Long> queryContainerIds = new ArrayList<Long>();
        queryContainerIds = JSON.parseArray(mapQuery.get("containerIds").toString(), Long.class);
        BigDecimal taskBoardQty = BigDecimal.ONE;
        if (mapQuery.get("taskBoardQty") != null) {
            taskBoardQty = new BigDecimal(Integer.valueOf(mapQuery.get("taskBoardQty").toString())); // 板数校正值
        }
        for (Object objContainerId : queryContainerIds) {
            Long containerId = Long.valueOf(objContainerId.toString());
            if (!containerIds.contains(containerId)) {
                containerIds.add(containerId);
            }
        }
        if (containerIds.size() < 1) {
            throw new BizCheckedException("2870005");
        }

//        //判断贵品,贵品不合板
//        List<WaveDetail> waveDetails = new ArrayList<WaveDetail>();
//        for (Long containerId : containerIds) {
//            List<WaveDetail> details = waveService.getAliveDetailsByContainerId(containerId);
//            if (null == details || details.size() < 1) {
//                throw new BizCheckedException("2870041");
//            }
//            waveDetails.addAll(details);
//        }
//        for (WaveDetail detail : waveDetails) {
//            BaseinfoItem item = itemService.getItem(detail.getOwnerId(), detail.getItemId());
//            if (item != null) {
//                if (item.getIsValuable().equals(ItemConstant.TYPE_IS_VALUABLE)) {
//                    throw new BizCheckedException("2870042");
//                }
//            }
//        }

        String idKey = "task_" + TaskConstant.TYPE_MERGE.toString();
        Long mergeTaskId = idGenerator.genId(idKey, true, true);
        // 合板
        Long mergedContainerId = mergeService.mergeContainers(containerIds, staffId, mergeTaskId);
        // 写合板taskInfo,用于做记录绩效
        Map<String, Object> taskQuery = new HashMap<String, Object>();
        taskQuery.put("containerId", mergedContainerId);
        taskQuery.put("type", TaskConstant.TYPE_MERGE);
        List<TaskInfo> mergeTaskList = baseTaskService.getTaskInfoList(taskQuery);
        TaskEntry taskEntry = new TaskEntry();
        if (mergeTaskList.size() > 0) {
            TaskInfo mergeTaskInfo = mergeTaskList.get(0);
            mergeTaskInfo.setTaskBoardQty(taskBoardQty); // 一板多托
            taskEntry.setTaskInfo(mergeTaskInfo);
            iTaskRpcService.update(TaskConstant.TYPE_MERGE, taskEntry);
        } else {
            TaskInfo mergeTaskInfo = new TaskInfo();
            mergeTaskInfo.setTaskId(mergeTaskId);
            mergeTaskInfo.setType(TaskConstant.TYPE_MERGE);
            mergeTaskInfo.setTaskName("合板任务[" + mergedContainerId + "]");
            mergeTaskInfo.setContainerId(mergedContainerId);
            mergeTaskInfo.setOperator(staffId);
            mergeTaskInfo.setBusinessMode(TaskConstant.MODE_DIRECT);
            mergeTaskInfo.setTaskBoardQty(taskBoardQty); // 一板多托
            taskEntry.setTaskInfo(mergeTaskInfo);
            iTaskRpcService.create(TaskConstant.TYPE_MERGE, taskEntry);
            // 直接完成
            iTaskRpcService.done(mergeTaskId);
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("response", true);
        return JsonUtils.SUCCESS(result);
    }

    /**
     * 检查合板托盘并返回明细
     *
     * @return
     * @throws BizCheckedException
     */
    @POST
    @Path("checkMergeContainers")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON})
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
    public String checkMergeContainers() throws BizCheckedException {
        Map<String, Object> mapQuery = RequestUtils.getRequest();
        List<Long> containerIds = new ArrayList<Long>();
        List<Long> queryContainerIds = new ArrayList<Long>();
        queryContainerIds = JSON.parseArray(mapQuery.get("containerIds").toString(), Long.class);
        for (Object queryContainerId : queryContainerIds) {
            Long containerId = Long.valueOf(queryContainerId.toString());
            if (!containerIds.contains(containerId)) {
                containerIds.add(containerId);
            }
        }
        if (containerIds.size() < 1) {
            throw new BizCheckedException("2870005");
        }

//        //判断贵品,贵品不合板
//        List<WaveDetail> detailArrayList = new ArrayList<WaveDetail>();
//        for (Long containerId : containerIds) {
//            List<WaveDetail> details = waveService.getAliveDetailsByContainerId(containerId);   //肯定能查到
//            if (null == details || details.size() < 1) {
//                throw new BizCheckedException("2870041");
//            }
//            detailArrayList.addAll(details);
//        }
//        for (WaveDetail detail : detailArrayList) {
//            BaseinfoItem item = itemService.getItem(detail.getOwnerId(), detail.getItemId());
//            if (item != null) {
//                if (item.getIsValuable().equals(ItemConstant.TYPE_IS_VALUABLE)) {
//                    throw new BizCheckedException("2870042");
//                }
//            }
//        }

        Long mergedContainerId = 0L;
        String deliveryCode = "";
        String deliveryName = "";
        Integer containerCount = 0; // 合板总托盘数
        BigDecimal packCount = BigDecimal.ZERO; // 总箱数
        BigDecimal turnoverBoxCount = BigDecimal.ZERO; // 总周转箱箱数
        List<Object> resultDetails = new ArrayList<Object>();
        List<Long> countedContainerIds = new ArrayList<Long>();
        for (Long containerId : containerIds) {
            List<WaveDetail> waveDetails = waveService.getAliveDetailsByContainerId(containerId);
            if (waveDetails == null) {
                throw new BizCheckedException("2870002");
            }
            Map<String, Object> resultDetail = new HashMap<String, Object>();
            resultDetail.put("containerId", containerId);
            resultDetail.put("packCount", BigDecimal.ZERO);
            resultDetail.put("turnoverBoxCount", BigDecimal.ZERO);
            resultDetail.put("isMerged", false);
            for (WaveDetail waveDetail : waveDetails) {
                // 已分别合过板的托盘不能合在一起
                if (!waveDetail.getMergedContainerId().equals(0L)) {
                    if (!mergedContainerId.equals(0L) && !waveDetail.getMergedContainerId().equals(mergedContainerId)) {
                        throw new BizCheckedException("2870004");
                    }
                    // 已合过的
                    resultDetail.put("isMerged", true);
                    List<WaveDetail> mergedWaveDetails = waveService.getWaveDetailsByMergedContainerId(waveDetail.getMergedContainerId());
                    for (WaveDetail mergedWaveDetail : mergedWaveDetails) {
                        Map<String, BigDecimal> qcCounts = iMergeRpcService.getQcCountsByWaveDetail(mergedWaveDetail);
                        if (!countedContainerIds.contains(mergedWaveDetail.getContainerId())) {
                            countedContainerIds.add(mergedWaveDetail.getContainerId());
                            containerCount++;
                            packCount = packCount.add(qcCounts.get("packCount"));
                            turnoverBoxCount = turnoverBoxCount.add(qcCounts.get("turnoverBoxCount"));
                        }
                        resultDetail.put("packCount", new BigDecimal(Double.valueOf(resultDetail.get("packCount").toString())).add(qcCounts.get("packCount")));
                        resultDetail.put("turnoverBoxCount", new BigDecimal(Double.valueOf(resultDetail.get("turnoverBoxCount").toString())).add(qcCounts.get("turnoverBoxCount")));
                    }
                    mergedContainerId = waveDetail.getMergedContainerId();
                } else if (!countedContainerIds.contains(containerId)) {
                    // 未合过的
                    countedContainerIds.add(containerId);
                    containerCount++;
                    Map<String, BigDecimal> qcCounts = iMergeRpcService.getQcCountsByWaveDetail(waveDetail);
                    packCount = packCount.add(qcCounts.get("packCount"));
                    turnoverBoxCount = turnoverBoxCount.add(qcCounts.get("turnoverBoxCount"));
                    resultDetail.put("packCount", new BigDecimal(Double.valueOf(resultDetail.get("packCount").toString())).add(qcCounts.get("packCount")));
                    resultDetail.put("turnoverBoxCount", new BigDecimal(Double.valueOf(resultDetail.get("turnoverBoxCount").toString())).add(qcCounts.get("turnoverBoxCount")));
                }
                // 判断托盘是否归属于同一门店
                Long orderId = waveDetail.getOrderId();
                ObdHeader obdHeader = soOrderService.getOutbSoHeaderByOrderId(orderId);
                if (obdHeader == null) {
                    throw new BizCheckedException("2870006");
                }
                if (deliveryCode.equals("")) {
                    deliveryCode = obdHeader.getDeliveryCode();
                    deliveryName = obdHeader.getDeliveryName();
                }
                if (!deliveryCode.equals(obdHeader.getDeliveryCode())) {
                    throw new BizCheckedException("2870007");
                }
            }
            resultDetails.add(resultDetail);
        }
        Map<String, Object> taskQuery = new HashMap<String, Object>();
        taskQuery.put("containerId", mergedContainerId);
        taskQuery.put("type", TaskConstant.TYPE_MERGE);
        List<TaskInfo> mergeTaskList = baseTaskService.getTaskInfoList(taskQuery);
        BigDecimal taskBoardQty = BigDecimal.ONE;
        if (mergeTaskList.size() > 0) {
            TaskInfo mergeTask = mergeTaskList.get(0);
            taskBoardQty = mergeTask.getTaskBoardQty();
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("deliveryCode", deliveryCode);
        result.put("deliveryName", deliveryName);
        result.put("containerCount", containerCount);
        result.put("packCount", packCount);
        result.put("turnoverBoxCount", turnoverBoxCount);
        result.put("details", resultDetails);
        result.put("taskBoardQty", taskBoardQty);
        return JsonUtils.SUCCESS(result);
    }
}
