package com.lsh.wms.rpc.service.stock;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.api.service.stock.IStockQuantRpcService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.stock.StockMoveService;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.model.stock.StockMove;
import com.lsh.wms.model.stock.StockQuant;
import com.lsh.wms.model.stock.StockQuantCondition;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mali on 16/7/28.
 */

@Service(protocol = "dubbo")
public class StockQuantRpcService implements IStockQuantRpcService {
    private static Logger logger = LoggerFactory.getLogger(StockQuantRpcService.class);

    @Autowired
    private StockQuantService quantService;

    @Autowired
    private StockMoveService moveService;

    @Autowired
    private LocationService locationService;

    private Map<String, Object> getQueryCondtion(StockQuantCondition condition) throws BizCheckedException {
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        try {
            if (condition.getLocationId() != null) {
                List<Long> locationList = locationService.getStoreLocationIds(condition.getLocationId());
                condition.setLocationId(0L);
                condition.setLocationList(locationList);
            }
            mapQuery = PropertyUtils.describe(condition);
        } catch (Exception e) {
            logger.error(e.getCause().getMessage());
            throw new BizCheckedException("3040001");
        }
        return mapQuery;
    }

    public BigDecimal getQty(StockQuantCondition condition) throws BizCheckedException {
        Map<String, Object> mapQuery = this.getQueryCondtion(condition);
        BigDecimal total =  quantService.getQty(mapQuery);
        return total;
    }


    public List<StockQuant> getQuantList(StockQuantCondition condition) throws BizCheckedException {
        Map<String, Object> mapQuery = this.getQueryCondtion(condition);
        List<StockQuant> quantList =  quantService.getQuants(mapQuery);
        return quantList;
    }

    public List<StockQuant> reserve(StockQuantCondition condition, Long taskId, BigDecimal requiredQty) throws BizCheckedException {
        Map<String, Object> mapQuery = this.getQueryCondtion(condition);
        BigDecimal total = this.getQty(condition);
        if (total.compareTo(requiredQty) < 0) {
            throw new BizCheckedException("2550001");
        }
        return quantService.reserve(mapQuery, taskId, requiredQty);
    }

    public void unReserve(Long taskId) {
        quantService.unReserve(taskId);
    }

    public void reserveByContainer(Long containerId) {
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("containerId", containerId);
        List<StockQuant> quantList = quantService.getQuants(mapQuery);
    }

    public void move(Long moveId) throws BizCheckedException {

    }
}
