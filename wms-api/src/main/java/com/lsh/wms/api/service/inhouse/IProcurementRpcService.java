package com.lsh.wms.api.service.inhouse;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.model.baseinfo.BaseinfoItemLocation;
import com.lsh.wms.model.procurement.NeedAndOutQty;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.transfer.StockTransferPlan;

/**
 * Created by mali on 16/7/30.
 */
public interface IProcurementRpcService {
    boolean needProcurement(Long locationId, Long itemId,Boolean checkMax) throws BizCheckedException;
    boolean needProcurementForLoft(Long locationId, Long itemId,Boolean checkMax) throws BizCheckedException;
    NeedAndOutQty returnNeedAndOutQty(Long locationId, Long itemId,Boolean checkMax) throws BizCheckedException;
    NeedAndOutQty returnNeedAndOutQtyForShelf(Long locationId, Long itemId,Boolean checkMax) throws BizCheckedException;
    TaskEntry addProcurementPlan(StockTransferPlan plan) throws BizCheckedException;
}
