package com.lsh.wms.api.service.stock;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by mali on 16/6/29.
 */
public interface IStockQuantRestService {

    String getOnhandQty(Long skuId, Long locationId, Long ownerId);
    String getList(Map<String, Object> mapQuery);
    String create(Map<String, Object> mapInput);
    String freeze(Map<String, Object> mapCondition);
    String unFreeze(Map<String, Object> mapCondition);
}
