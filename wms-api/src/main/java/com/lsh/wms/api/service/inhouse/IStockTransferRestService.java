package com.lsh.wms.api.service.inhouse;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.model.transfer.StockTransferPlan;

/**
 * Created by mali on 16/7/26.
 */
public interface IStockTransferRestService {
    String addPlan(StockTransferPlan plan)  throws BizCheckedException;
    String scanToLocation() throws BizCheckedException;
    String scanFromLocation() throws BizCheckedException;
}
