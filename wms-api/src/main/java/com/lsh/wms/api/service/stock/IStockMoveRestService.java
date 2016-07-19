package com.lsh.wms.api.service.stock;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by mali on 16/7/11.
 */
public interface IStockMoveRestService {
    String getInboundQty(Long locationId, Long skuId);
    String getOutboundQty(Long locationId, Long skuId);
    String assign(Long moveId);
    String done(Long moveId, BigDecimal qtyDone);
    String allocate(Long moveId, Long operator);
    String create(Map<String, Object> mapInput);
    String cancel(Long moveId);
    public String getHistory(Long move_id);
}
