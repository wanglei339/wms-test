package com.lsh.wms.api.service.inhouse;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.model.transfer.StockTransferPlan;

import java.util.List;

/**
 * Created by mali on 16/8/1.
 */
public interface IStockTransferProviderRestService {
    String addPlan(StockTransferPlan plan) throws BizCheckedException;
    String cancelPlan(Long taskId) throws BizCheckedException;
}
