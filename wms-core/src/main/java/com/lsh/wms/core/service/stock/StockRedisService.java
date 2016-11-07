package com.lsh.wms.core.service.stock;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsh.base.common.utils.StrUtils;
import com.lsh.wms.api.service.inventory.ISynInventory;
import com.lsh.wms.core.constant.RedisKeyConstant;
import com.lsh.wms.core.dao.redis.RedisStringDao;
import com.lsh.wms.core.service.inventory.InventoryRedisService;
import com.lsh.wms.model.wave.WaveDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * Created by mali on 16/9/7.
 */
@Component
public class StockRedisService {
    @Autowired
    private RedisStringDao redisDao;

    @Autowired
    private InventoryRedisService inventoryRedisService;

    @Autowired
    private SynStockService synStockService;

    private static Logger logger = LoggerFactory.getLogger(StockRedisService.class);

    private String getRedisKey(Long skuId) {
        String warehouseName = "DC37";
        String redisKey = StrUtils.formatString(RedisKeyConstant.WAREHOUSE_SKU_INVENTORY_QTY, warehouseName, skuId);
        return redisKey;
    }

    public Double getSkuQty(Long itemId){
        String redistKey = getRedisKey(itemId);
        String val = redisDao.get(redistKey);
        return null == val ?  0.0 :new Double(val);
    }

    public void inBound(Long itemId, BigDecimal qty) {
        String redisKey = getRedisKey(itemId);
        redisDao.increase(redisKey, new Double(qty.toString()));
        double availableQty =    inventoryRedisService.getAvailableSkuQty(itemId);
        synStockService.synStock(itemId,availableQty);

    }

    public void outBound(Long skuId, BigDecimal qty) {
        String redisKey = getRedisKey(skuId);
        redisDao.decrease(redisKey, new Double(qty.toString()));
        double availableQty =  inventoryRedisService.getAvailableSkuQty(skuId);
        synStockService.synStock(skuId,availableQty);
    }

}
