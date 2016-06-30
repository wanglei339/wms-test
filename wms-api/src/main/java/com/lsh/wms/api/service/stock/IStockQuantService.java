package com.lsh.wms.api.service.stock;

/**
 * Created by mali on 16/6/29.
 */
public interface IStockQuantService {

    public String getOnhandQty(Integer skuId, Integer locationId, Integer containerId);
}
