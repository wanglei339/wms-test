package com.lsh.wms.api.service.stock;

import java.math.BigDecimal;

/**
 * Created by mali on 16/6/29.
 */
public interface IStockQuantRestService {

    public String getOnhandQty(Integer skuId, Integer locationId, Integer containerId);
}
