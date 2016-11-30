package com.lsh.wms.core.service.pick;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.BeanMapTransUtils;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.core.constant.PickConstant;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.dao.wave.WaveDetailDao;
import com.lsh.wms.core.dao.pick.PickTaskHeadDao;
import com.lsh.wms.core.dao.wave.WaveDetailDao;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.stock.StockMoveService;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.core.service.utils.PackUtil;
import com.lsh.wms.core.service.wave.WaveService;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.pick.PickTaskHead;
import com.lsh.wms.model.stock.StockQuant;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.model.wave.WaveDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zengwenjun on 16/7/15.
 */
@Component
@Transactional(readOnly = true)
public class PickTaskService {
    private static final Logger logger = LoggerFactory.getLogger(PickTaskService.class);

    @Autowired
    private PickTaskHeadDao taskHeadDao;
    @Autowired
    private WaveDetailDao taskDetailDao;
    @Autowired
    private StockMoveService moveService;
    @Autowired
    private StockQuantService stockQuantService;
    @Autowired
    private WaveService waveService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private BaseTaskService baseTaskService;
    @Autowired
    private ItemService itemService;

    @Transactional(readOnly = false)
    public Boolean createPickTask(PickTaskHead head, List<WaveDetail> details){
        List<PickTaskHead> heads = new ArrayList<PickTaskHead>();
        heads.add(head);
        return this.createPickTasks(heads, details);
    }

    @Transactional(readOnly = false)
    public Boolean createPickTasks(List<PickTaskHead> heads, List<WaveDetail> details){
        for(PickTaskHead head : heads){
            head.setCreatedAt(DateUtils.getCurrentSeconds());
            head.setUpdatedAt(DateUtils.getCurrentSeconds());
            taskHeadDao.insert(head);
        }
        if (details != null) {
            for (WaveDetail detail : details) {
                detail.setCreatedAt(DateUtils.getCurrentSeconds());
                detail.setUpdatedAt(DateUtils.getCurrentSeconds());
                taskDetailDao.insert(detail);
            }
        }
        return true;
    }

    @Transactional(readOnly = false)
    public void update(PickTaskHead taskHead) {
        taskHead.setUpdatedAt(DateUtils.getCurrentSeconds());
        taskHeadDao.update(taskHead);
    }


    public PickTaskHead getPickTaskHead(Long taskId){
        HashMap<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("taskId", taskId);
        List<PickTaskHead> pickTaskHeadList = taskHeadDao.getPickTaskHeadList(mapQuery);
        return pickTaskHeadList.size() == 0 ? null : pickTaskHeadList.get(0);
    }

    /**
     * 更新拣货详情并移动库存
     * @param pickDetail
     * @param locationId
     * @param containerId
     * @param qty
     * @param staffId
     */
    @Transactional(readOnly = false)
    public void pickOne(WaveDetail pickDetail, Long locationId, Long containerId, BigDecimal qty, Long staffId) {
        Long taskId = pickDetail.getPickTaskId();
        Long itemId = pickDetail.getItemId();
        if (qty.compareTo(new BigDecimal(0)) == 1) {
            Map<String, Object> quantParams = new HashMap<String, Object>();
            quantParams.put("locationId", locationId);
            quantParams.put("itemId", itemId);
            List<StockQuant> quants = stockQuantService.getQuants(quantParams);
            if (quants.size() < 1) {
                throw new BizCheckedException("2550009");
            }
            StockQuant quant = quants.get(0);
            Long fromContainerId = quant.getContainerId();
            // 移动库存
            moveService.moveToContainer(itemId, staffId, fromContainerId, containerId, locationService.getWarehouseLocation().getLocationId(), qty);
        }
        // 更新wave_detail
        pickDetail.setContainerId(containerId);
        pickDetail.setRealPickLocation(locationId);
        pickDetail.setPickQty(qty);
        pickDetail.setPickUid(staffId);
        pickDetail.setPickAt(DateUtils.getCurrentSeconds());
        waveService.updateDetail(pickDetail);
    }

    /**
     * 回溯拣货状态
     * @param staffId
     * @return
     */
    public Map<String, Object> restore (Long staffId, List<Map> taskList) {
        Map<String, Object> result = new HashMap<String, Object>();
        // 获取分配给操作人员的所有拣货任务
        List<TaskInfo> taskInfos = baseTaskService.getAssignedTaskByOperator(staffId, TaskConstant.TYPE_PICK);
        if (taskInfos.isEmpty()) {
            return null;
        }
        // 取taskId
        List<Long> taskIds = new ArrayList<Long>();

        for (TaskInfo taskInfo: taskInfos) {
            taskIds.add(taskInfo.getTaskId());
        }

        if (taskList != null && !taskList.isEmpty()) {
            for (Map<Long, Object> task: taskList) {
                if (!taskIds.contains(Long.valueOf(task.get("taskId").toString()))) {
                    throw new BizCheckedException("2060011");
                }
            }
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
            // 获取下一个拣货位id
            PickTaskHead nextTaskHead = this.getPickTaskHead(taskInfos.get(0).getTaskId());
            result.put("next_detail", renderResult(BeanMapTransUtils.Bean2map(nextTaskHead), "allocCollectLocation", "allocCollectLocationCode"));
            result.put("done", false);
            result.put("pick_done", true);
        } else {
            result.put("next_detail", renderResult(BeanMapTransUtils.Bean2map(needPickDetail), "allocPickLocation", "allocPickLocationCode"));
            result.put("done", false);
            result.put("pick_done", false);
        }
        return result;
    }

    /**
     * 设置结果
     * @param result
     * @param locationKey
     * @param resultKey
     * @return
     */
    public Map<String, Object> renderResult(Map<String, Object> result, String locationKey, String resultKey) {
        BaseinfoLocation location = locationService.getLocation(Long.valueOf(result.get(locationKey).toString()));
        Long taskId = 0L;
        if (result.get("pickTaskId") != null) {
            taskId = Long.valueOf(result.get("pickTaskId").toString());
        } else {
            taskId = Long.valueOf(result.get("taskId").toString());
            result.put("pickTaskId", taskId);
        }
        TaskInfo taskInfo = baseTaskService.getTaskInfoById(taskId);
        result.put(resultKey, location.getLocationCode());
        result.put("taskSubType", taskInfo.getSubType());
        // 按照箱规转换数量
        if (result.get("allocQty") != null && !result.get("allocQty").equals(BigDecimal.ZERO)) {
            BigDecimal allocQty = new BigDecimal(result.get("allocQty").toString());
            result.put("allocQty", PackUtil.EAQty2UomQty(allocQty, result.get("allocUnitName").toString()));
        }
        if (taskInfo.getSubType().equals(PickConstant.SHELF_TASK_TYPE)) {
            result.put("unitName", "箱");
        } else if (taskInfo.getSubType().equals(PickConstant.SHELF_PALLET_TASK_TYPE)) {
            // 整托拣货,固定为只捡一托
            result.put("allocQty", 1);
            result.put("unitName", "托");
        } else {
            result.put("unitName", "EA");
        }
        if (result.get("itemId") != null) {
            BaseinfoItem item = itemService.getItem(Long.valueOf(result.get("itemId").toString()));
            result.put("skuName", item.getSkuName());
        }
        result.put("containerId", taskInfo.getContainerId().toString());
        return result;
    }
}
