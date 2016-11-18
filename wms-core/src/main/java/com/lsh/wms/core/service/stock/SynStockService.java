package com.lsh.wms.core.service.stock;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.config.PropertyUtils;
import com.lsh.base.common.utils.StrUtils;
import com.lsh.wms.api.service.inventory.ISynStockInventory;
import com.lsh.wms.core.constant.RedisKeyConstant;
import com.lsh.wms.core.dao.redis.RedisSortedSetDao;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Project Name: lsh-wms
 * Created by fuhao
 * Date: 16/9/7
 * Time: 16/9/7.
 * 北京链商电子商务有限公司
 * Package name:com.lsh.wms.core.service.stock.
 * desc:类功能描述
 */
@Component
@Service(protocol = "dubbo")
public class SynStockService implements ISynStockInventory{

    private static Logger logger = LoggerFactory.getLogger(SynStockService.class);

    @Autowired
    private RedisSortedSetDao redisSortedSetDao;

    @Autowired
    private ItemService itemService;

    private String getRedisKey(Long ownerId) {
        String warehouseCode = PropertyUtils.getString("warehouseId");
        String zoneCode = PropertyUtils.getString("zone");
        String redisKey = StrUtils.formatString(RedisKeyConstant.WAREHOUSE_SKU_AVAILABLE_QTY,  zoneCode, warehouseCode, ownerId);
        return redisKey;
    }

    public void synStock(Long itemId, Double avQty) {
        BaseinfoItem item = itemService.getItem(itemId);
        if (item == null) {
            logger.warn(StrUtils.formatString("invalid item_id {0}", itemId));
            return;
        }
        String redisKey = this.getRedisKey(item.getOwnerId());
        logger.info(StrUtils.formatString("Item[{0}] avQty had change to {1}", itemId, avQty));
        redisSortedSetDao.add(redisKey, item.getSkuCode(), avQty.doubleValue());
    }
}
