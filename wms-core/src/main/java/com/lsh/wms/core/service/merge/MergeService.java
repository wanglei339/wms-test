package com.lsh.wms.core.service.merge;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.dao.so.ObdHeaderDao;
import com.lsh.wms.core.dao.task.TaskInfoDao;
import com.lsh.wms.core.dao.wave.WaveDetailDao;
import com.lsh.wms.core.service.pick.PickTaskService;
import com.lsh.wms.core.service.so.SoDeliveryService;
import com.lsh.wms.core.service.so.SoOrderService;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.core.service.wave.WaveService;
import com.lsh.wms.model.so.ObdHeader;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.model.wave.WaveDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fengkun on 2016/10/11.
 */
@Component
@Transactional(readOnly = true)
public class MergeService {
    private static final Logger logger = LoggerFactory.getLogger(PickTaskService.class);

    @Autowired
    private TaskInfoDao taskInfoDao;
    @Autowired
    private WaveService waveService;
    @Autowired
    private WaveDetailDao waveDetailDao;
    @Autowired
    private SoOrderService soOrderService;

    @Transactional(readOnly = false)
    public Long mergeContainers(List<Long> containerIds, Long staffId, Long mergeTaskId) throws BizCheckedException {
        Long mergedContainerId = 0L;
        String deliveryCode = "";
        List<WaveDetail> waveDetails = new ArrayList<WaveDetail>();
        List<TaskInfo> taskInfos = new ArrayList<TaskInfo>();
        for(Long containerId: containerIds) {
            List<WaveDetail> details = waveService.getAliveDetailsByContainerId(containerId);
            if (details == null) {
                throw new BizCheckedException("2870002");
            }
            for (WaveDetail detail: details) {
                Long qcTaskId = detail.getQcTaskId();
                // 已分别合过板的托盘不能合在一起
                if (!detail.getMergedContainerId().equals(0L)) {
                    if (!mergedContainerId.equals(0L) && !detail.getMergedContainerId().equals(mergedContainerId)) {
                        throw new BizCheckedException("2870004");
                    }
                    mergedContainerId = detail.getMergedContainerId();
                } else {
                    waveDetails.add(detail);
                }
                // 判断托盘是否归属于同一门店
                Long orderId = detail.getOrderId();
                ObdHeader obdHeader = soOrderService.getOutbSoHeaderByOrderId(orderId);
                if (obdHeader == null) {
                    throw new BizCheckedException("2870006");
                }
                if (deliveryCode.equals("")) {
                    deliveryCode = obdHeader.getDeliveryCode();
                }
                if (!deliveryCode.equals(obdHeader.getDeliveryCode())) {
                    throw new BizCheckedException("2870007");
                }

                // TODO: 判断品项类型,是否能合板
                Map<String, Object> taskMapQuery = new HashMap<String, Object>();
                taskMapQuery.put("taskId", qcTaskId);
                taskMapQuery.put("type", TaskConstant.TYPE_QC);
                taskMapQuery.put("status", TaskConstant.Done);
//                taskMapQuery.put("businessMode", TaskConstant.MODE_DIRECT); //todo 在库打板子
                List<TaskInfo> qcTaskInfos = taskInfoDao.getTaskInfoList(taskMapQuery);
                if (qcTaskInfos.size() == 0) {
                    throw new BizCheckedException("2870003");
                }
                TaskInfo taskInfo = qcTaskInfos.get(0);
                if (!taskInfos.contains(taskInfo)) {
                    taskInfos.add(taskInfo);
                }
            }
        }
        if (mergedContainerId.equals(0L)) {
            mergedContainerId = RandomUtils.genId(); // 生成合板后的container_id
        }
        // 更新关联信息
        for (WaveDetail waveDetail: waveDetails) {
            waveDetail.setMergedContainerId(mergedContainerId);
            waveDetail.setMergeTaskId(mergeTaskId);
            waveDetail.setMergeUid(staffId);
            waveDetail.setMergeAt(DateUtils.getCurrentSeconds());
            waveDetailDao.update(waveDetail);
        }
        Boolean isShow = true;
        for (TaskInfo taskInfo: taskInfos) {
            taskInfo.setMergedContainerId(mergedContainerId);
            if (isShow) {
                taskInfo.setIsShow(1);
                isShow = false;
            } else {
                taskInfo.setIsShow(0);
            }
            taskInfoDao.update(taskInfo);
        }
        return mergedContainerId;
    }

    public Map<String, Object> getPackCountByContainerId (Long containerId) {
        Map<String, Object> result = new HashMap<String, Object>();
        return result;
    }
}
