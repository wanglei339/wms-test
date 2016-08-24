package com.lsh.wms.api.service.inhouse;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.model.baseinfo.BaseinfoItemLocation;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.transfer.StockTransferPlan;

/**
 * Created by mali on 16/7/30.
 */
public interface IProcurementRpcService {
    boolean needProcurement(Long locationId, Long itemId) throws BizCheckedException;
    TaskEntry addProcurementPlan(StockTransferPlan plan) throws BizCheckedException;
}
