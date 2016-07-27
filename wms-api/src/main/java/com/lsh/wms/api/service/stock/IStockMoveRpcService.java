package com.lsh.wms.api.service.stock;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by mali on 16/7/27.
 */
public interface IStockMoveRpcService {
    String create(Map<String, Object> mapInput);
    String done(Long moveId, BigDecimal qtyDone);
}
