package com.lsh.wms.core.service.so;

import com.lsh.base.common.utils.StrUtils;
import com.lsh.wms.core.constant.RedisKeyConstant;
import com.lsh.wms.core.dao.redis.RedisSortedSetDao;
import com.lsh.wms.model.so.ObdDetail;
import com.lsh.wms.model.so.ObdHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

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
    private static Logger logger = LoggerFactory.getLogger(SoOrderRedisService.class);

    public void insertSoRedis(ObdHeader obdHeader, List<ObdDetail> obdDetailList){
        Long orderId = obdHeader.getOrderId();
        for (ObdDetail obdDetail : obdDetailList) {
            String redistKey = StrUtils.formatString(RedisKeyConstant.SO_SKU_INVENTORY_QTY, obdDetail.getSkuId());
            redisSortedSetDao.add(redistKey,orderId.toString(), obdDetail.getOrderQty().doubleValue());
        }
    }

    public void delSoRedis(Long orderId,Long skuId){
        String redistKey = StrUtils.formatString(RedisKeyConstant.SO_SKU_INVENTORY_QTY, skuId);
        redisSortedSetDao.remove(redistKey,orderId.toString());
    }

    public Set<ZSetOperations.TypedTuple<String>> getSoSkuQty(Long skuId){
        String redistKey = StrUtils.formatString(RedisKeyConstant.SO_SKU_INVENTORY_QTY, skuId);
        return  redisSortedSetDao.rangeWithScores(redistKey,0,-1);
    }

}
