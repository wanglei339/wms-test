package com.lsh.wms.core.service.inventory;

import com.lsh.wms.core.dao.redis.RedisSortedSetDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    private RedisSortedSetDao redisSortedSetDao;
    private static Logger logger = LoggerFactory.getLogger(InventoryRedisService.class);




}
