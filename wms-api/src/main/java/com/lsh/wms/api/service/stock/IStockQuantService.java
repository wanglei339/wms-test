package com.lsh.wms.api.service.stock;

/**
 * Created by mali on 16/6/29.
 */
public interface IStockQuantService {

    public String getOnhandQty(Long skuId, Long locationId, Long containerId, Long ownerId, Long supplierId);
}
