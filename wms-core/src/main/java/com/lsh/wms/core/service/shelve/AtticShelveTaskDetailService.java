package com.lsh.wms.core.service.shelve;

import com.alibaba.fastjson.JSON;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.core.constant.BinUsageConstant;
import com.lsh.wms.core.constant.StockConstant;
import com.lsh.wms.core.dao.shelve.AtticShelveTaskDetailDao;
import com.lsh.wms.core.service.item.ItemLocationService;
import com.lsh.wms.core.service.location.BaseinfoLocationService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.stock.StockMoveService;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.core.service.stock.StockSummaryService;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.model.baseinfo.BaseinfoItemLocation;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.shelve.AtticShelveTaskDetail;
import com.lsh.wms.model.stock.StockDelta;
import com.lsh.wms.model.stock.StockMove;
import com.lsh.wms.model.stock.StockQuant;
import com.lsh.wms.model.task.TaskInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wuhao on 16/8/16.
 */
@Component
@Transactional(readOnly = true)
public class AtticShelveTaskDetailService {
    @Autowired
    private AtticShelveTaskDetailDao detailDao;
    @Autowired
    private StockMoveService moveService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private BaseTaskService baseTaskService;
    @Autowired
    private StockSummaryService stockSummaryService;
    @Autowired
    private StockQuantService stockQuantService;
    @Autowired
    private ItemLocationService itemLocationService;

    @Transactional(readOnly = false)
    public void create(AtticShelveTaskDetail detail) {
        //锁库位
        locationService.lockLocation(detail.getAllocLocationId());

        detail.setCreatedAt(DateUtils.getCurrentSeconds());
        detailDao.insert(detail);
    }
    @Transactional(readOnly = false)
    public void batchCreate(List<AtticShelveTaskDetail> details) {
        for(AtticShelveTaskDetail detail:details) {
            detail.setCreatedAt(DateUtils.getCurrentSeconds());
            detail.setUpdatedAt(DateUtils.getCurrentSeconds());
            detailDao.insert(detail);
        }
    }

    @Transactional(readOnly = false)
    public void assign(Long taskId, Long staffId) {
        Map<String,Object> queryMap = new HashMap<String, Object>();
        queryMap.put("taskId", taskId);
        List<AtticShelveTaskDetail> details = detailDao.getAtticShelveTaskDetailList(queryMap);
        for(AtticShelveTaskDetail detail:details) {
            detail.setOperator(staffId);
            detail.setUpdatedAt(DateUtils.getCurrentSeconds());
            detailDao.update(detail);
        }
    }

    public List<AtticShelveTaskDetail> getShelveTaskDetail(Long taskId) {
        HashMap<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("taskId", taskId);
        List<AtticShelveTaskDetail> taskDetails = detailDao.getAtticShelveTaskDetailList(mapQuery);

        return taskDetails;
    }
    public AtticShelveTaskDetail getShelveTaskDetail(Long taskId,Long status) {
        HashMap<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("taskId", taskId);
        mapQuery.put("status",status);
        List<AtticShelveTaskDetail> taskDetails = detailDao.getAtticShelveTaskDetailList(mapQuery);
        if(taskDetails ==null ||taskDetails.size()==0){
            return null;
        }else {
            return taskDetails.get(0);
        }

    }
    @Transactional(readOnly = false)
    public void updateDetail(AtticShelveTaskDetail detail) {
        detail.setUpdatedAt(DateUtils.getCurrentSeconds());
        detailDao.update(detail);

    }
    @Transactional(readOnly = false)
    public void doneDetail(AtticShelveTaskDetail detail,StockMove move) {

        TaskInfo info = baseTaskService.getTaskByTaskId(detail.getTaskId());

        moveService.move(move);
        locationService.unlockLocation(detail.getAllocLocationId());
        detail.setUpdatedAt(DateUtils.getCurrentSeconds());
        detailDao.update(detail);

    }

    public AtticShelveTaskDetail getDetailById(Long id) {
       return detailDao.getAtticShelveTaskDetailById(id);

    }
    public AtticShelveTaskDetail getDetailByTaskIdAndStatus(Long taskId,Long status) {
        HashMap<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("taskId", taskId);
        mapQuery.put("status",status);
        List<AtticShelveTaskDetail> taskDetails = detailDao.getAtticShelveTaskDetailList(mapQuery);
        if(taskDetails ==null ||taskDetails.size()==0){
            return null;
        }else {
            return taskDetails.get(0);
        }
    }
    @Transactional(readOnly = false)
    public void remove(AtticShelveTaskDetail detail) {
        detailDao.remove(detail.getId());
    }

    /**
     * 获取下一个上架货架位并记录
     * @param taskId
     * @return
     */
    @Transactional(readOnly = false)
    public BaseinfoLocation getNextAllocLocation(Long taskId) {
        TaskInfo info = baseTaskService.getTaskInfoById(taskId);
        if (info == null) {
            throw new BizCheckedException("2030009");
        }
        Long containerId = info.getContainerId();
        List<StockQuant> quants = stockQuantService.getQuantsByContainerId(containerId);
        if (quants.size() < 1) {
            throw new BizCheckedException("2030001");
        }
        StockQuant quant = quants.get(0);
        Long itemId = quant.getItemId();
        List<BaseinfoItemLocation> itemLocations = itemLocationService.getItemLocationList(itemId);
        if (itemLocations==null || itemLocations.size() < 1) {
            throw new BizCheckedException("2030010");
        }
        Long pickLocationId = itemLocations.get(0).getPickLocationid();
        BaseinfoLocation pickingLocation = locationService.getLocation(pickLocationId);
        // 是否是拣货位
        if (!pickingLocation.getBinUsage().equals(BinUsageConstant.BIN_UASGE_PICK)) {
            throw new BizCheckedException("2030002");
        }
        List<Long> calcLocationIds = JsonUtils.json2Obj(info.getExt8(), List.class);
        if (calcLocationIds == null) {
            calcLocationIds = new ArrayList<Long>();
        }
        BaseinfoLocation nextLocation = locationService.getNearestStorageByPicking(pickingLocation, calcLocationIds);
        if(nextLocation==null){
            throw new BizCheckedException("2880020");
        }
        calcLocationIds.add(nextLocation.getLocationId());
        info.setExt8(JSON.toJSONString(calcLocationIds));
        baseTaskService.update(info);
        return nextLocation;
    }
}
