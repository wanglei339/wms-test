package com.lsh.wms.core.service.stock;

import com.google.common.collect.Maps;
import com.lsh.wms.model.stock.StockQuant;
import com.lsh.wms.core.dao.stock.StockQuantDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by mali on 16/6/29.
 */
@Component
@Transactional(readOnly = true)
public class StockQuantService {
    private static final Logger logger = LoggerFactory.getLogger(StockQuantService.class);

    @Autowired
    private StockQuantDao stockQuantDao;

    public List<StockQuant> getQuants(Integer skuId, Integer locationId, Integer containerId) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("skuId", skuId);
        params.put("locationId", locationId);
        params.put("containerId", containerId);
        return stockQuantDao.getQuants(params);
    }

}
