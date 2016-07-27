package com.lsh.wms.rpc.service.shelve;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.api.service.shelve.IShelveRpcService;
import com.lsh.wms.core.service.item.ItemLocationService;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.model.baseinfo.BaseinfoContainer;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.baseinfo.BaseinfoItemLocation;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.stock.StockQuant;
import com.lsh.wms.rpc.service.location.LocationRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.parsing.Location;

import java.util.ArrayList;
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

    private static final Float SHELF_LIFE_THRESHOLD = 0.3f; // 保质期差额阈值

    /**
     * 分配上架容器
     * @param container
     * @return
     * @throws BizCheckedException
     */
    public BaseinfoLocation assginShelveLocation(BaseinfoContainer container) throws BizCheckedException {
        BaseinfoLocation targetLocation = new BaseinfoLocation();
        Long containerId = container.getContainerId();
        // 获取托盘上stockQuant信息
        List<StockQuant> quants = stockQuantService.getQuantsByContainerId(containerId);
        if (quants.size() < 1) {
            throw new BizCheckedException("2030001");
        }
        StockQuant quant = quants.get(0);
        Long itemId = quant.getItemId();
        BaseinfoItem item = itemService.getItem(itemId);
        // 是否允许地堆堆放
        Integer floorAvailable = item.getFloorAvailable();
        // 允许地堆
        if (floorAvailable == 1) {
            BaseinfoLocation floorLocation = locationRpcService.assignFloor();
            // 地堆无空间,上拣货位
            if (floorLocation == null) {
                targetLocation = assignPickingLocation(container);
            }
            targetLocation = floorLocation;
        } else { // 不允许地堆
            // 上拣货位
            targetLocation = assignPickingLocation(container);
        }
        return targetLocation;
    }

    /**
     * 分配拣货位
     * @param container
     * @return
     * @throws BizCheckedException
     */
    public BaseinfoLocation assignPickingLocation(BaseinfoContainer container) throws BizCheckedException {
        Long containerId = container.getContainerId();
        // 获取托盘上stockQuant信息
        List<StockQuant> quants = stockQuantService.getQuantsByContainerId(containerId);
        if (quants.size() < 1) {
            throw new BizCheckedException("2030001");
        }
        StockQuant quant = quants.get(0);
        Long itemId = quant.getItemId();
        List<BaseinfoItemLocation> itemLocations = itemLocationService.getItemLocationList(itemId);
        for (BaseinfoItemLocation itemLocation : itemLocations) {
            Long pickingLocationId = itemLocation.getPickLocationid();
            BaseinfoLocation pickingLocation = locationService.getLocation(pickingLocationId);
            // 是否是拣货位
            if (!pickingLocation.getType().equals(locationService.LOCATION_TYPE.get("picking"))) {
                throw new BizCheckedException("2030002");
            }
            // TODO: 判断该拣货位是否符合拣货标准
            if (true) {
                // 对比保质期差额阈值
                if (true) {
                    return pickingLocation;
                } else {
                    // 查找补货任务
                    if (true) {
                        // 补货任务已领取
                        if (true) {
                            // 上货架位
                            return assignShelfLocation(container, pickingLocation);
                        } else {
                            // 取消补货任务
                            return pickingLocation;
                        }
                    }
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
}
