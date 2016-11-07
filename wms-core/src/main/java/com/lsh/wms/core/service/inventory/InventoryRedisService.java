package com.lsh.wms.core.service.inventory;

import com.lsh.base.common.utils.StrUtils;
import com.lsh.wms.core.constant.RedisKeyConstant;
import com.lsh.wms.core.dao.redis.RedisSortedSetDao;
import com.lsh.wms.core.service.so.SoOrderRedisService;
import com.lsh.wms.core.service.stock.StockRedisService;
import com.lsh.wms.model.wave.WaveDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    @Autowired
    private StockRedisService stockRedisService;
    private static Logger logger = LoggerFactory.getLogger(InventoryRedisService.class);

    public double soOrderSkuQty(Long itemId){
        Set<ZSetOperations.TypedTuple<String>>  skuQtys=  soOrderRedisService.getSoSkuQty(itemId);
        double qty = 0.0;
        for (ZSetOperations.TypedTuple typeTuple:skuQtys) {
            qty  = qty + typeTuple.getScore();
        }
        return qty;
    }

    public double getAvailableSkuQty(Long itemId) {
        return stockRedisService.getSkuQty(itemId) - this.soOrderSkuQty(itemId);
    }

    public Map<String, BigDecimal> getInventoryInfo(Long itemId) {
        BigDecimal total = new BigDecimal(stockRedisService.getSkuQty(itemId));
        BigDecimal soQty =  new BigDecimal(soOrderSkuQty(itemId));
        Map<String, BigDecimal> inventoryInfo = new HashMap<String, BigDecimal>();
        inventoryInfo.put("total", total);
        inventoryInfo.put("soQty", soQty);
        inventoryInfo.put("available", total.subtract(soQty));
        return inventoryInfo;
    }

    @Transactional(readOnly = false)
    public void onDelivery(List<WaveDetail> waveDetailList) {
        for (WaveDetail detail : waveDetailList) {
            soOrderRedisService.delSoRedis(detail.getOrderId(), detail.getItemId());
            stockRedisService.outBound(detail.getItemId(), detail.getQcQty());
        }
    }
}
