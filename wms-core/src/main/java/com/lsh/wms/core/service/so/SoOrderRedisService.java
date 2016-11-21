package com.lsh.wms.core.service.so;

import com.lsh.base.common.utils.StrUtils;
import com.lsh.wms.core.constant.RedisKeyConstant;
import com.lsh.wms.core.dao.redis.RedisSortedSetDao;
import com.lsh.wms.core.service.inventory.InventoryRedisService;
import com.lsh.wms.core.service.stock.StockRedisService;
import com.lsh.wms.core.service.stock.SynStockService;
import com.lsh.wms.model.so.ObdDetail;
import com.lsh.wms.model.so.ObdHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * Project Name: lsh-wms
 * Created by fuhao
 * Date: 16/8/31
 * Time: 16/8/31.
 * 北京链商电子商务有限公司
 * Package name:com.lsh.wms.core.service.so.
 * desc:类功能描述
 */
@Component
public class SoOrderRedisService {

    @Autowired
    private RedisSortedSetDao redisSortedSetDao;
    @Autowired
    private SynStockService synStockService;
    @Autowired
    private InventoryRedisService inventoryRedisService;

    private static Logger logger = LoggerFactory.getLogger(SoOrderRedisService.class);

    public void insertSoRedis(ObdHeader obdHeader, List<ObdDetail> obdDetailList){
        Long orderId = obdHeader.getOrderId();
        for (ObdDetail obdDetail : obdDetailList) {
            //将skuId改为itemId
            String redistKey = StrUtils.formatString(RedisKeyConstant.SO_SKU_INVENTORY_QTY, obdDetail.getItemId());
            redisSortedSetDao.add(redistKey, orderId.toString(), obdDetail.getOrderQty().doubleValue());
            Long itemId = obdDetail.getItemId();
            synStockService.synStock(itemId, inventoryRedisService.getAvailableSkuQty(itemId));
            logger.info(StrUtils.formatString("SO[{0}] inserted, reserve itemId[{1}] with qty is {2}", orderId, itemId, obdDetail.getOrderQty()));
        }
    }

    public void delSoRedis(Long orderId, Long itemId, BigDecimal deliverQty){
        String redistKey = StrUtils.formatString(RedisKeyConstant.SO_SKU_INVENTORY_QTY, itemId);
        logger.info(StrUtils.formatString("SO[{0}] ship out, qty is {1}", orderId, deliverQty));
        if ( redisSortedSetDao.decrease(redistKey, orderId.toString(), deliverQty.doubleValue()) <= 0) {
            redisSortedSetDao.remove(redistKey, orderId.toString());
        }
    }

    public Set<ZSetOperations.TypedTuple<String>> getSoSkuQty(Long itemId){
        String redistKey = StrUtils.formatString(RedisKeyConstant.SO_SKU_INVENTORY_QTY, itemId);
        return  redisSortedSetDao.rangeWithScores(redistKey,0,-1);
    }

}
