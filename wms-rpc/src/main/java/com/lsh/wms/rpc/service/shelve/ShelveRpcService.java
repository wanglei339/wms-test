package com.lsh.wms.rpc.service.shelve;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.api.service.shelve.IShelveRpcService;
import com.lsh.wms.core.constant.BinUsageConstant;
import com.lsh.wms.core.constant.CsiConstan;
import com.lsh.wms.core.constant.LocationConstant;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.service.item.ItemLocationService;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.core.service.task.MessageService;
import com.lsh.wms.model.baseinfo.BaseinfoContainer;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.baseinfo.BaseinfoItemLocation;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.stock.StockQuant;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.model.task.TaskMsg;
import com.lsh.wms.rpc.service.inhouse.ProcurementRpcService;
import com.lsh.wms.rpc.service.location.LocationRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by fengkun on 16/7/15.
 */

@Service(protocol = "dubbo")
public class ShelveRpcService implements IShelveRpcService {
    private static Logger logger = LoggerFactory.getLogger(ShelveRpcService.class);

    @Autowired
    private LocationService locationService;
    @Autowired
    private StockQuantService stockQuantService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private ItemLocationService itemLocationService;
    @Autowired
    private LocationRpcService locationRpcService;
    @Autowired
    private ProcurementRpcService procurementRpcService;
    @Autowired
    private BaseTaskService baseTaskService;
    @Autowired
    private MessageService messageService;

    private static final Float SHELF_LIFE_THRESHOLD = 0.3f; // 保质期差额阈值

    /**
     * 分配上架位置
     * @param container
     * @return
     * @throws BizCheckedException
     */
    public BaseinfoLocation assginShelveLocation(BaseinfoContainer container, Long subType, Long taskId) throws BizCheckedException {
        BaseinfoLocation targetLocation = new BaseinfoLocation();
        Long containerId = container.getContainerId();
        // 获取托盘上stockQuant信息
        List<StockQuant> quants = stockQuantService.getQuantsByContainerId(containerId);
        if (quants.size() < 1) {
            throw new BizCheckedException("2030001");
        }
        // 目前一个入库托盘上只能有一种商品
        StockQuant quant = quants.get(0);
        Long itemId = quant.getItemId();
        BaseinfoItem item = itemService.getItem(itemId);
        // 是否允许地堆堆放
        Integer floorAvailable = item.getFloorAvailable();
        // 允许地堆
        if (floorAvailable.equals(1)) {
            BaseinfoLocation floorLocation = locationRpcService.assignFloor(quant);
            // 地堆无空间,上拣货位
            if (floorLocation == null) {
                targetLocation = assignPickingLocation(container, taskId);
            } else {
                targetLocation = floorLocation;
            }
        } else { // 不允许地堆
            // 上拣货位
            targetLocation = assignPickingLocation(container, taskId);
        }
        return targetLocation;
    }

    /**
     * 分配拣货位
     * @param container
     * @return
     * @throws BizCheckedException
     */
    public BaseinfoLocation assignPickingLocation(BaseinfoContainer container, Long taskId) throws BizCheckedException {
        Long containerId = container.getContainerId();
        // 获取托盘上stockQuant信息
        List<StockQuant> quants = stockQuantService.getQuantsByContainerId(containerId);
        if (quants.size() < 1) {
            throw new BizCheckedException("2030001");
        }
        StockQuant quant = quants.get(0);
        Long itemId = quant.getItemId();
        List<BaseinfoItemLocation> itemLocations = itemLocationService.getItemLocationList(itemId);
        if (itemLocations.size() < 1) {
            throw new BizCheckedException("2030010");
        }
        for (BaseinfoItemLocation itemLocation : itemLocations) {
            Long pickingLocationId = itemLocation.getPickLocationid();
            BaseinfoLocation pickingLocation = locationService.getLocation(pickingLocationId);
            // 是否是拣货位
            if (!pickingLocation.getBinUsage().equals(BinUsageConstant.BIN_UASGE_PICK)) {
                throw new BizCheckedException("2030002");
            }
            // 判断该拣货位是否符合拣货标准
            // TODO 不找拣货位了,调度器创建任务时传过来
            if (procurementRpcService.needProcurement(pickingLocationId, itemId)) {
                // 对比保质期差额阈值
                if (this.checkShelfLifeThreshold(quant, pickingLocation,BinUsageConstant.BIN_UASGE_STORE)) {
                    return pickingLocation;
                } else {
                    // 查找补货任务
                    List<TaskInfo> procurementTaskList = baseTaskService.getIncompleteTaskByLocation(pickingLocationId, TaskConstant.TYPE_PROCUREMENT);
                    if (0 != procurementTaskList.size()) {
                        TaskInfo procurementTask = procurementTaskList.get(0);
                        // 补货任务已领取
                        if (procurementTask.getStatus().equals(TaskConstant.Assigned)) {
                            // 上货架位
                            return assignShelfLocation(container, pickingLocation);
                        } else {
                            // 取消补货任务
                            TaskMsg msg = new TaskMsg();
                            msg.setType(TaskConstant.EVENT_PROCUREMENT_CANCEL);
                            msg.setSourceTaskId(taskId);
                            Map<String, Object> body = new HashMap<String, Object>();
                            body.put("taskId", procurementTask.getTaskId());
                            msg.setMsgBody(body);
                            messageService.sendMessage(msg);
                            return pickingLocation;
                        }
                    }
                    return pickingLocation;
                }
            } else {
                // 上货架位
                return assignShelfLocation(container, pickingLocation);
            }
        }
        return null;
    }

    /**
     * 分配货架位
     * @param container
     * @return
     * @throws BizCheckedException
     */
    public BaseinfoLocation assignShelfLocation(BaseinfoContainer container, BaseinfoLocation pickingLocation) throws BizCheckedException {
        BaseinfoLocation targetLocation = locationService.getNearestStorageByPicking(pickingLocation);
        if (targetLocation == null) {
            throw new BizCheckedException("2030003");
        }
        return targetLocation;
    }

    /**
     * 判断上架商品是否达到该区域存储位商品保质期的差额阈值
     * @param quant
     * @param location
     * @return
     */
    public Boolean checkShelfLifeThreshold (StockQuant quant, BaseinfoLocation location,Integer binUsage) {
        Long expireDate = quant.getExpireDate();
        Map<String, Object> params = new HashMap<String, Object>();
        // 获取到拣货位的库区id
        BaseinfoLocation areaLocation = locationService.getFatherByClassification(location.getLocationId());
        // 获取该库区下所有的货架位
        List<BaseinfoLocation> storeLocations = locationService.getBinsByIdAndTypeUsage(areaLocation.getLocationId(), LocationConstant.BIN, binUsage);
        if (storeLocations.size() < 1) {
            return false;
        }
        params.put("itemId", quant.getItemId());
        params.put("locationList", storeLocations);
        // 获取货架位上的该商品库存
        List<StockQuant> storedQuants = stockQuantService.getQuants(params);
        for (StockQuant storedQuant : storedQuants) {
            // 达到差额阈值
            if ((expireDate - storedQuant.getExpireDate()) < CsiConstan.SHELF_LIFE_THRESHOLD) {
                return true;
            }
        }
        return false;
    }
}
