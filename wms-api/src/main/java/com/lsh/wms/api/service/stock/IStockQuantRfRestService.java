package com.lsh.wms.api.service.stock;

import com.lsh.base.common.exception.BizCheckedException;

/**
 * Created by mali on 16/8/2.
 */
public interface IStockQuantRfRestService {
    String getItemByLocation() throws BizCheckedException;
}
