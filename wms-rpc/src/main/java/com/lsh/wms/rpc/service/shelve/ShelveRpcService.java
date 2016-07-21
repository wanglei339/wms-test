package com.lsh.wms.rpc.service.shelve;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.api.service.shelve.IShelveRpcService;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.model.baseinfo.BaseinfoContainer;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
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
public class ShelveRpcService implements IShelveRpcService {
    private static Logger logger = LoggerFactory.getLogger(ShelveRpcService.class);

    @Autowired
    private LocationService locationService;
    @Autowired
    private StockQuantService stockQuantService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private LocationRpcService locationRpcService;

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
                targetLocation = assignPickingLocation(quant);
            }
            targetLocation = floorLocation;
        } else { // 不允许地堆
            targetLocation = assignPickingLocation(quant);
        }
        return targetLocation;
    }

    /**
     * 分配拣货位
     * @param quant
     * @return
     * @throws BizCheckedException
     */
    public BaseinfoLocation assignPickingLocation(StockQuant quant) throws BizCheckedException {

    }
}
