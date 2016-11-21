package com.lsh.wms.core.service.inventory;

import com.lsh.base.common.utils.StrUtils;
import com.lsh.wms.core.constant.RedisKeyConstant;
import com.lsh.wms.core.constant.SoConstant;
import com.lsh.wms.core.dao.redis.RedisSortedSetDao;
import com.lsh.wms.core.service.so.SoOrderRedisService;
import com.lsh.wms.core.service.so.SoOrderService;
import com.lsh.wms.core.service.stock.StockRedisService;
import com.lsh.wms.model.so.ObdHeader;
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
    private static Logger logger = LoggerFactory.getLogger(InventoryRedisService.class);

    @Autowired
    private SoOrderRedisService soOrderRedisService;
    @Autowired
    private StockRedisService stockRedisService;
    @Autowired
    private SoOrderService soOrderService;

    public double soOrderSkuQty(Long itemId){
        Set<ZSetOperations.TypedTuple<String>>  skuQtys=  soOrderRedisService.getSoSkuQty(itemId);
        double qty = 0.0;
        for (ZSetOperations.TypedTuple typeTuple:skuQtys) {
            qty  = qty + typeTuple.getScore();
        }
        return qty;
    }

    public double getAvailableSkuQty(Long itemId) {
        logger.error(StrUtils.formatString("fuck sku order is {0}", stockRedisService.getSkuQty(itemId)));
        logger.error(StrUtils.formatString("fuck sku so order is {0}", this.soOrderSkuQty(itemId)));
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
            ObdHeader header = soOrderService.getOutbSoHeaderByOrderId(detail.getOrderId());
            if (header == null) {
                continue;
            }
            if (header.getOrderType().equals(SoConstant.ORDER_TYPE_SO) || header.getOrderType().equals(SoConstant.ORDER_TYPE_STO)) {
                logger.error(StrUtils.formatString("xxxx fuck onDelivery orderId{0}, itemId{1}, qty{2}", detail.getOrderId(), detail.getItemId(), detail.getQcQty()));
                soOrderRedisService.delSoRedis(detail.getOrderId(), detail.getItemId(), detail.getQcQty());
                stockRedisService.outBound(detail.getItemId(), detail.getQcQty());
            }
        }
    }
}
