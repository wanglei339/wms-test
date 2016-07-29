package com.lsh.wms.api.service.stock;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by mali on 16/7/11.
 */
public interface IStockMoveRestService {
    String done(Long moveId, BigDecimal qtyDone);
    String create(Map<String, Object> mapInput);
    public String getHistory(Long move_id);
}
