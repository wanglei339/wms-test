package com.lsh.wms.core.service.pick;

import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.core.dao.wave.WaveDetailDao;
import com.lsh.wms.core.dao.pick.PickTaskHeadDao;
import com.lsh.wms.core.dao.wave.WaveDetailDao;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.stock.StockMoveService;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.core.service.wave.WaveService;
import com.lsh.wms.model.pick.PickTaskHead;
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
    private WaveService waveService;
    @Autowired
    private LocationService locationService;

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
        for(WaveDetail detail : details){
            detail.setUpdatedAt(DateUtils.getCurrentSeconds());
            taskDetailDao.insert(detail);
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

    public List<WaveDetail> getPickTaskDetails(Long taskId){
        HashMap<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("pickTaskId", taskId);
        return taskDetailDao.getWaveDetailList(mapQuery);
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
        Long fromContainerId = pickDetail.getContainerId();
        Long itemId = pickDetail.getItemId();
        // 更新wave_detail
        pickDetail.setContainerId(containerId);
        pickDetail.setRealPickLocation(locationId);
        pickDetail.setPickQty(qty);
        pickDetail.setPickUid(staffId);
        pickDetail.setPickAt(DateUtils.getCurrentSeconds());
        waveService.updateDetail(pickDetail);
        // 移动库存
        moveService.moveToContainer(itemId, staffId, fromContainerId, containerId, locationService.getWarehouseLocationId(), qty);
    }
}
