package com.lsh.wms.api.service.inhouse;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.model.transfer.StockTransferPlan;

/**
 * Created by mali on 16/8/1.
 */
public interface IStockTransferProviderRestService {
    String addPlan(StockTransferPlan plan) throws BizCheckedException;
    String updatePlan(StockTransferPlan plan) throws BizCheckedException;
    String cancelPlan() throws BizCheckedException;
}
