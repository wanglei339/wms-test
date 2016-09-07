package com.lsh.wms.core.service.stock;

import com.lsh.base.common.utils.StrUtils;
import com.lsh.wms.core.constant.RedisKeyConstant;
import com.lsh.wms.core.dao.redis.RedisStringDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Set;

/**
 * Created by mali on 16/9/7.
 */
@Component
public class StockRedisService {
    @Autowired
    private RedisStringDao redisDao;
    private static Logger logger = LoggerFactory.getLogger(StockRedisService.class);

    private String getRedisKey(Long skuId) {
        String warehouseName = "DC37";
        String redisKey = StrUtils.formatString(RedisKeyConstant.WAREHOUSE_SKU_INVENTORY_QTY, warehouseName, skuId);
        return redisKey;
    }

    public Double getSkuQty(Long skuId){
        String redistKey = getRedisKey(skuId);
        String val = redisDao.get(redistKey);
        return null == val ? new Double(val) : 0.0;
    }

    public void inBound(Long skuId, BigDecimal qty) {
        String redisKey = getRedisKey(skuId);
        redisDao.increase(redisKey, new Double(qty.toString()));
    }

    public void oubBound(Long skuId, BigDecimal qty) {
        String redisKey = getRedisKey(skuId);
        redisDao.decrease(redisKey, new Double(qty.toString()));
    }

    @Transactional(readOnly = false)
    public void onDelivery() {

    }
}
