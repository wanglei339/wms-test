package com.lsh.wms.core.service.inventory;

import com.lsh.wms.core.dao.redis.RedisSortedSetDao;
import com.lsh.wms.core.service.so.SoOrderRedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Project Name: lsh-wms
 * Created by fuhao
 * Date: 16/8/31
 * Time: 16/8/31.
 * 北京链商电子商务有限公司
 * Package name:com.lsh.wms.core.service.inventory.
 * desc:类功能描述
 */
@Component
public class InventoryRedisService {
    @Autowired
    private SoOrderRedisService soOrderRedisService;
    private static Logger logger = LoggerFactory.getLogger(InventoryRedisService.class);

    public double soOrderSkuQty(Long sku_id){
        Set<ZSetOperations.TypedTuple<String>>  skuQtys=  soOrderRedisService.getSoSkuQty(sku_id);
        double qty = 0.0;
        for (ZSetOperations.TypedTuple typeTuple:skuQtys) {
            qty  = qty + typeTuple.getScore();
        }
        return qty;
    }


}
