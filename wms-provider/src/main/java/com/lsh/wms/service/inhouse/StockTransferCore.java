package com.lsh.wms.service.inhouse;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.wms.api.service.item.IItemRpcService;
import com.lsh.wms.api.service.location.ILocationRpcService;
import com.lsh.wms.api.service.stock.IStockMoveRpcService;
import com.lsh.wms.api.service.stock.IStockQuantRpcService;
import com.lsh.wms.api.service.system.ISysUserRpcService;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.constant.ContainerConstant;
import com.lsh.wms.core.constant.LocationConstant;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.dao.baseinfo.BaseinfoLocationDao;
import com.lsh.wms.core.dao.task.TaskInfoDao;
import com.lsh.wms.core.service.container.ContainerService;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.location.BaseinfoLocationBinService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.baseinfo.BaseinfoLocationBin;
import com.lsh.wms.model.stock.StockMove;
import com.lsh.wms.model.stock.StockQuant;
import com.lsh.wms.model.stock.StockQuantCondition;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.model.transfer.StockTransferPlan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by mali on 16/7/30.
 */
@Component
public class StockTransferCore {

    private static Logger logger = LoggerFactory.getLogger(StockTransferCore.class);

    @Reference
    private IItemRpcService itemRpcService;

    @Reference
    private IStockQuantRpcService stockQuantRpcService;

    @Reference
    private IStockMoveRpcService moveRpcService;

    @Reference
    private ITaskRpcService taskRpcService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private TaskInfoDao taskInfoDao;

    @Reference
    private ISysUserRpcService iSysUserRpcService;

    @Autowired
    private ContainerService containerService;

    @Autowired
    private BaseinfoLocationDao locationDao;

    @Reference
    private ILocationRpcService locationRpcService;

    @Autowired
    private BaseinfoLocationBinService locationBinService;

    @Autowired
    private ItemService itemService;

    public void fillTransferPlan(StockTransferPlan plan) throws BizCheckedException {
        StockQuantCondition condition = new StockQuantCondition();
        condition.setLocationId(plan.getFromLocationId());
        condition.setItemId(plan.getItemId());
        List<StockQuant> quants = stockQuantRpcService.getQuantList(condition);
        if (quants == null || quants.size() == 0) {
            logger.error("error plan:" + plan.toString());
            return;
        }
        StockQuant quant = quants.get(0);
        plan.setPackUnit(quant.getPackUnit());
        plan.setPackName(quant.getPackName());
        plan.setQty(plan.getUomQty());
        if (plan.getSubType().compareTo(1L) == 0) {
            BigDecimal total = stockQuantRpcService.getQty(condition);
            plan.setQty(total.divide(quant.getPackUnit(), 0, BigDecimal.ROUND_DOWN));
        } else if (plan.getSubType().compareTo(3L) == 0) {
            plan.setPackName("EA");
        }
    }

    public void outbound(Map<String, Object> params) throws BizCheckedException {
        Long uid = 0L;
        Long taskId = Long.valueOf(params.get("taskId").toString().trim());
        String locationCode = params.get("locationCode").toString().trim();
        Long fromLocationId = locationRpcService.getLocationIdByCode(locationCode);
        try {
            uid = iSysUserRpcService.getSysUserById(Long.valueOf(params.get("uid").toString())).getUid();
        } catch (Exception e) {
            throw new BizCheckedException("2550013");
        }
        TaskEntry taskEntry = taskRpcService.getTaskEntryById(taskId);
        if (taskEntry == null) {
            throw new BizCheckedException("2550005");
        }
        if (!taskEntry.getTaskInfo().getOperator().equals(uid)) {
            throw new BizCheckedException("2550031");
        }
        StockQuantCondition condition = new StockQuantCondition();
        condition.setLocationId(fromLocationId);
        condition.setItemId(taskEntry.getTaskInfo().getItemId());
        List<StockQuant> quants = stockQuantRpcService.getQuantList(condition);

        TaskInfo taskInfo = taskEntry.getTaskInfo();
        if (taskInfo.getType().compareTo(TaskConstant.TYPE_PROCUREMENT) == 0) {
            if (quants == null || quants.size() == 0) {
                throw new BizCheckedException("2550008");
            }
            StockQuant quant = quants.get(0);
            if (quant.getItemId().compareTo(taskInfo.getItemId()) != 0) {
                throw new BizCheckedException("2040005");
            }
        } else {
            if (taskInfo.getFromLocationId().compareTo(fromLocationId) != 0) {
                throw new BizCheckedException("2040005");
            }
        }
        Long containerId = taskInfo.getContainerId();
        Long toLocationId = locationService.getWarehouseLocationId();
        if (taskInfo.getSubType().compareTo(1L) == 0) {
            moveRpcService.moveWholeContainer(containerId, taskId, uid, fromLocationId, toLocationId);
        } else {
            BigDecimal qtyDone = new BigDecimal(params.get("uomQty").toString().trim());
            if (qtyDone.compareTo(BigDecimal.ZERO) <= 0 ||
                    qtyDone.setScale(0, BigDecimal.ROUND_DOWN).compareTo(qtyDone) != 0) {
                throw new BizCheckedException("2550034");
            }
            BigDecimal total = stockQuantRpcService.getQty(condition);
            if (total.compareTo(qtyDone) < 0) {
                throw new BizCheckedException("2550008");
            }
            StockMove move = new StockMove();
            ObjUtils.bean2bean(taskInfo, move);
            if (taskInfo.getSubType().compareTo(2L) == 0) {
                move.setQty(qtyDone.multiply(quants.get(0).getPackUnit()).setScale(0, BigDecimal.ROUND_HALF_UP));
            } else if (taskInfo.getSubType().compareTo(3L) == 0) {
                move.setQty(qtyDone);
            }
            move.setFromLocationId(fromLocationId);
            move.setToLocationId(toLocationId);
            move.setFromContainerId(quants.get(0).getContainerId());
            move.setToContainerId(containerId);
            move.setSkuId(taskInfo.getSkuId());
            move.setOwnerId(taskInfo.getOwnerId());
            List<StockMove> moveList = new ArrayList<StockMove>();
            moveList.add(move);
            moveRpcService.move(moveList);
            taskInfo.setQtyDone(qtyDone);
        }
        //taskInfo.setStatus(TaskConstant.Doing);
        taskInfo.setStep(2);
        taskInfoDao.update(taskInfo);
    }

    public void inbound(Map<String, Object> params) throws BizCheckedException {
        Long uid = 0L;
        Long taskId = Long.valueOf(params.get("taskId").toString().trim());
        String locationCode = params.get("locationCode").toString().trim();
        Long toLocationId = locationRpcService.getLocationIdByCode(locationCode);
        try {
            uid = iSysUserRpcService.getSysUserById(Long.valueOf(params.get("uid").toString())).getUid();
        } catch (Exception e) {
            throw new BizCheckedException("2550013");
        }
        TaskEntry taskEntry = taskRpcService.getTaskEntryById(taskId);
        if (taskEntry == null) {
            throw new BizCheckedException("2550005");
        }
        if (!taskEntry.getTaskInfo().getOperator().equals(uid)) {
            throw new BizCheckedException("2550031");
        }
        TaskInfo taskInfo = taskEntry.getTaskInfo();
        if(taskInfo.getType().compareTo(113l)!=0) {
            if (taskInfo.getToLocationId().compareTo(toLocationId) != 0) {
                throw new BizCheckedException("2040007");
            }
        }
        Long containerId = taskInfo.getContainerId();
        Long fromLocationId = locationService.getWarehouseLocationId();

        if (taskInfo.getSubType().compareTo(1L) == 0) {
            moveRpcService.moveWholeContainer(containerId, taskId, uid, fromLocationId, toLocationId);
        } else {
            BigDecimal qtyDone = new BigDecimal(params.get("uomQty").toString().trim());
            if (qtyDone.compareTo(BigDecimal.ZERO) <= 0 ||
                    qtyDone.setScale(0, BigDecimal.ROUND_DOWN).compareTo(qtyDone) != 0) {
                throw new BizCheckedException("2550034");
            }
            if (taskInfo.getQtyDone().compareTo(qtyDone) != 0) {
                throw new BizCheckedException("2550014");
            }
            if (taskInfo.getType().compareTo(TaskConstant.TYPE_STOCK_TRANSFER) == 0) {
                BaseinfoLocation toLocation = locationService.getLocation(toLocationId);
                if (toLocation.getType().equals(LocationConstant.SHELF_STORE_BIN) ||
                        toLocation.getType().equals(LocationConstant.SHELF_PICKING_BIN) ||
                        toLocation.getType().equals(LocationConstant.SPLIT_SHELF_BIN)
                        ) {
                    BigDecimal freeVolume = this.getThreshold(toLocationId, taskInfo.getItemId(), taskInfo.getSubType(), qtyDone);
                    if (freeVolume.compareTo(BigDecimal.ZERO) < 0) {
                        throw new BizCheckedException("2550006");
                    }
                }
            }
            StockMove move = new StockMove();
            ObjUtils.bean2bean(taskInfo, move);
            if (taskInfo.getSubType().compareTo(2L) == 0) {
                move.setQty(qtyDone.multiply(taskInfo.getPackUnit()).setScale(0, BigDecimal.ROUND_HALF_UP));
            } else if (taskInfo.getSubType().compareTo(3L) == 0) {
                move.setQty(qtyDone);
            }
            move.setFromLocationId(fromLocationId);
            move.setToLocationId(toLocationId);
            Long newContainerId = containerService.createContainerByType(ContainerConstant.PALLET).getContainerId();
            Long toContainerId= containerService.getContaierIdByLocationId(toLocationId);
            Long locationType = locationService.getLocation(toLocationId).getType();
            if (toContainerId.equals(0L)) {
                toContainerId = newContainerId;
            } else if (locationType.equals(LocationConstant.BACK_AREA) || locationType.equals(LocationConstant.DEFECTIVE_AREA)){
                toContainerId = newContainerId;
            }
            move.setFromContainerId(containerId);
            move.setToContainerId(toContainerId);
            move.setSkuId(taskInfo.getSkuId());
            move.setOwnerId(taskInfo.getOwnerId());
            List<StockMove> moveList = new ArrayList<StockMove>();
            moveList.add(move);
            moveRpcService.move(moveList);
            taskInfo.setQtyDone(qtyDone);
            taskInfoDao.update(taskInfo);
        }
        taskRpcService.done(taskId);
    }

    public void sortOutbound(List<TaskEntry> entryList) {
        Collections.sort(entryList, new Comparator<TaskEntry>() {
            public int compare(TaskEntry entry1, TaskEntry entry2) {
                try {
                    TaskInfo info1 = entry1.getTaskInfo(), info2 = entry2.getTaskInfo();
                    //sort fromLocationId
                    return info1.getFromLocationId().compareTo(info2.getFromLocationId());
                } catch (Exception e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        });
        Long order = 1L;
        for (TaskEntry entry : entryList) {
            entry.getTaskInfo().setExt1(order);
            order++;
            taskInfoDao.update(entry.getTaskInfo());
        }
    }

    public void sortInbound(List<TaskEntry> entryList) {
        Collections.sort(entryList, new Comparator<TaskEntry>() {
            public int compare(TaskEntry entry1, TaskEntry entry2) {
                try {
                    TaskInfo info1 = entry1.getTaskInfo(), info2 = entry2.getTaskInfo();
                    //sort toLocationId
                    return info1.getToLocationId().compareTo(info2.getToLocationId());
                } catch (Exception e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        });
        Long order = 1L;
        for (TaskEntry entry : entryList) {
            entry.getTaskInfo().setExt2(order);
            order++;
            taskInfoDao.update(entry.getTaskInfo());
        }
    }

    public Long getFirstOutbound(Long staffId) {
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("status", TaskConstant.Assigned);
        mapQuery.put("operator", staffId);
        mapQuery.put("ext1", 1);
        List<TaskEntry> entryList = taskRpcService.getTaskList(TaskConstant.TYPE_STOCK_TRANSFER, mapQuery);
        return entryList.get(0).getTaskInfo().getTaskId();
    }

    public Long getFirstInbound(Long staffId) {
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("operator", staffId);
        mapQuery.put("status", TaskConstant.Assigned);
        mapQuery.put("ext2", 1);
        List<TaskEntry> entryList = taskRpcService.getTaskList(TaskConstant.TYPE_STOCK_TRANSFER, mapQuery);
        return entryList.get(0).getTaskInfo().getTaskId();
    }


    public Long getNextOutbound(TaskEntry entry) {
        Long ext1 = entry.getTaskInfo().getExt1();
        Long operator = entry.getTaskInfo().getOperator();
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("status", TaskConstant.Assigned);
        mapQuery.put("ext1", ext1 + 1);
        mapQuery.put("operator", operator);
        List<TaskEntry> entryList = taskRpcService.getTaskList(TaskConstant.TYPE_STOCK_TRANSFER, mapQuery);
        if (entryList.isEmpty()) {
            return 0L;
        }
        return entryList.get(0).getTaskInfo().getTaskId();
    }

    public Long getNextInbound(TaskEntry entry) {
        Long ext2 = entry.getTaskInfo().getExt2();
        Long operator = entry.getTaskInfo().getOperator();
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("status", TaskConstant.Assigned);
        mapQuery.put("operator", operator);
        mapQuery.put("ext2", ext2 + 1);
        List<TaskEntry> entryList = taskRpcService.getTaskList(TaskConstant.TYPE_STOCK_TRANSFER, mapQuery);
        if (entryList.isEmpty()) {
            return 0L;
        }
        return entryList.get(0).getTaskInfo().getTaskId();
    }

    //TODO
    public BaseinfoLocation getNearestLocation(BaseinfoLocation currentLocation) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("leftRange", currentLocation.getLeftRange());
        params.put("rightRange", currentLocation.getRightRange());
        params.put("canStore", LocationConstant.CAN_STORE);
        params.put("isValid", LocationConstant.IS_VALID);
        //params.put("canUse", LocationConstant.CAN_USE);
        params.put("isLocked", LocationConstant.UNLOCK);
        List<BaseinfoLocation> locationList = locationDao.getChildrenLocationList(params);
        if (locationList != null && !locationList.isEmpty()) {
            return locationList.get(0);
        }
        return currentLocation;
    }

    public List<Long> sortTaskByLocation(List<TaskEntry> entryList) {
        TaskInfo taskInfo = entryList.get(0).getTaskInfo();
        List<Long> taskList = new ArrayList<Long>();
        taskList.add(taskInfo.getTaskId());
        Long fromLocationId = taskInfo.getFromLocationId(), toLocationId = taskInfo.getToLocationId();
        List<TaskEntry> list = new ArrayList<TaskEntry>();
        boolean isFirst = true;
        for (TaskEntry entry : entryList) {
            if (isFirst) {
                isFirst = false;
                continue;
            }
            TaskInfo info = entry.getTaskInfo();
            Long newFromLocaiton = java.lang.Math.abs(info.getFromLocationId() - fromLocationId),
                    newToLocation = java.lang.Math.abs(info.getToLocationId() - toLocationId);
            info.setFromLocationId(newFromLocaiton);
            info.setToLocationId(newToLocation);
            list.add(entry);
        }
        // sort by locationId
        Collections.sort(list, new Comparator<TaskEntry>() {
            public int compare(TaskEntry entry1, TaskEntry entry2) {
                try {
                    TaskInfo info1 = entry1.getTaskInfo(), info2 = entry2.getTaskInfo();
                    if (info1.getFromLocationId().compareTo(info2.getFromLocationId()) == 0) {
                        return info1.getToLocationId().compareTo(info2.getToLocationId());
                    }
                    return info1.getFromLocationId().compareTo(info2.getFromLocationId());
                } catch (Exception e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        });
        // get other 4 tasks
        int idx = 0;
        for (TaskEntry entry : list) {
            taskList.add(entry.getTaskInfo().getTaskId());
            idx++;
            if (idx == 4) {
                break;
            }
        }
        return taskList;
    }

    //TODO
    public List<TaskEntry> getMoreTasks(TaskEntry entry) {
        Long fromLocationId = entry.getTaskInfo().getFromLocationId(),
                toLocationId = entry.getTaskInfo().getToLocationId(),
                fromPassage = locationService.getFatherIdByType(fromLocationId, LocationConstant.PASSAGE),
                toPassage = locationService.getFatherIdByType(toLocationId, LocationConstant.PASSAGE);
        List<Long> fromLocationIdList = locationService.getStoreLocationIds(fromPassage),
                toLocationIdList = locationService.getStoreLocationIds(toPassage);
        List<TaskEntry> taskList = new ArrayList<TaskEntry>();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("status", TaskConstant.Draft);
        params.put("fromLocationList", fromLocationIdList);
        params.put("toLocationList", toLocationIdList);
        taskList = taskRpcService.getTaskList(TaskConstant.TYPE_STOCK_TRANSFER, params);
        if (taskList == null || taskList.isEmpty()) {
            params.clear();
            params.put("status", TaskConstant.Draft);
            params.put("fromLocationList", fromLocationIdList);
            taskList = taskRpcService.getTaskList(TaskConstant.TYPE_STOCK_TRANSFER, params);
            if (taskList == null || taskList.isEmpty()) {
                params.clear();
                params.put("status", TaskConstant.Draft);
                params.put("toLocationList", toLocationIdList);
                taskList = taskRpcService.getTaskList(TaskConstant.TYPE_STOCK_TRANSFER, params);
            }
        }
        return taskList == null ? new ArrayList<TaskEntry>() : taskList;
    }

    public BigDecimal getThreshold(Long locationId, Long itemId, Long subType, BigDecimal qty) {
        BaseinfoLocationBin bin = (BaseinfoLocationBin) locationBinService.getBaseinfoItemLocationModelById(locationId);
        BigDecimal pickVolume = bin.getVolume();
        //取容积的80%
        pickVolume = pickVolume.multiply(new BigDecimal(0.8)).setScale(0, BigDecimal.ROUND_HALF_UP);
        BaseinfoItem item = itemService.getItem(itemId);

        BigDecimal bulk = BigDecimal.ONE;
        if (subType.equals(2L)) {
            //计算包装单位的体积
            bulk = bulk.multiply(item.getPackLength());
            bulk = bulk.multiply(item.getPackHeight());
            bulk = bulk.multiply(item.getPackWidth());
        } else if (subType.equals(3L)) {
            //计算EA单位的体积
            bulk = bulk.multiply(item.getLength());
            bulk = bulk.multiply(item.getHeight());
            bulk = bulk.multiply(item.getWidth());
        }
        StockQuantCondition condition = new StockQuantCondition();
        condition.setLocationId(locationId);
        BigDecimal reservedQty = stockQuantRpcService.getQty(condition);
        //计算库位能存多少商品
        BigDecimal num = pickVolume.divide(bulk, 0, BigDecimal.ROUND_UP);
        return num.subtract(qty).subtract(reservedQty);
    }
}
