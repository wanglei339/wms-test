package com.lsh.wms.core.service.shelve;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.core.constant.BinUsageConstant;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.dao.shelve.ShelveTaskHeadDao;
import com.lsh.wms.core.service.item.ItemLocationService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.model.baseinfo.BaseinfoItemLocation;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.shelve.ShelveTaskHead;
import com.lsh.wms.model.stock.StockQuant;
import com.lsh.wms.model.task.TaskInfo;
import com.alibaba.fastjson.JSON;
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
 * Created by fengkun on 16/7/25.
 */
@Component
@Transactional(readOnly = true)
public class ShelveTaskService extends BaseTaskService {
    private static final Logger logger = LoggerFactory.getLogger(ShelveTaskService.class);

    @Autowired
    private ShelveTaskHeadDao taskHeadDao;
    @Autowired
    private LocationService locationService;
    @Autowired
    private ItemLocationService itemLocationService;
    @Autowired
    private StockQuantService stockQuantService;

    @Transactional(readOnly = false)
    public void create(ShelveTaskHead taskHead) {
        taskHead.setCreatedAt(DateUtils.getCurrentSeconds());
        taskHead.setUpdatedAt(DateUtils.getCurrentSeconds());
        taskHeadDao.insert(taskHead);
    }

    @Transactional(readOnly = false)
    public void assign(Long taskId, Long staffId, Long locationId) {
        ShelveTaskHead taskHead = taskHeadDao.getShelveTaskHeadByTaskId(taskId);
        List<Long> calcLocationIds = new ArrayList<Long>();
        calcLocationIds.add(locationId);
        taskHead.setOperator(staffId);
        taskHead.setAllocLocationId(locationId);
        taskHead.setUpdatedAt(DateUtils.getCurrentSeconds());
        taskHead.setCalcLocationIds(JSON.toJSONString(calcLocationIds));
        taskHeadDao.update(taskHead);
    }

    @Transactional(readOnly = false)
    public void done(Long taskId, Long locationId) {
        ShelveTaskHead taskHead = taskHeadDao.getShelveTaskHeadByTaskId(taskId);
        taskHead.setRealLocationId(locationId);
        taskHead.setShelveAt(DateUtils.getCurrentSeconds());
        taskHeadDao.update(taskHead);
    }

    public ShelveTaskHead getShelveTaskHead(Long taskId) {
        HashMap<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("taskId", taskId);
        List<ShelveTaskHead> taskHeads = taskHeadDao.getShelveTaskHeadList(mapQuery);
        if (taskHeads.size() != 1) {
            return null;
        }
        return taskHeads.get(0);
    }

    /**
     * 获取下一个上架货架位并记录
     * @param taskId
     * @return
     */
    @Transactional(readOnly = false)
    public BaseinfoLocation getNextAllocLocation(Long taskId) {
        ShelveTaskHead taskHead = taskHeadDao.getShelveTaskHeadByTaskId(taskId);
        if (taskHead == null) {
            throw new BizCheckedException("2030009");
        }
        Long containerId = taskHead.getContainerId();
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
        List<Long> calcLocationIds = JsonUtils.json2Obj(taskHead.getCalcLocationIds(), List.class);
        if (calcLocationIds == null) {
            calcLocationIds = new ArrayList<Long>();
        }
        BaseinfoLocation nextLocation = locationService.getNearestStorageByPicking(pickingLocation, calcLocationIds);
        if (nextLocation != null) {
            calcLocationIds.add(nextLocation.getLocationId());
            taskHead.setCalcLocationIds(JSON.toJSONString(calcLocationIds));
            taskHeadDao.update(taskHead);
        }
        return nextLocation;
    }
}
