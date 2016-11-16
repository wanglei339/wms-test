package com.lsh.wms.api.service.inhouse;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.stock.StockQuant;
import com.lsh.wms.model.transfer.StockTransferPlan;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by mali on 16/8/1.
 */
public interface IProcurementProveiderRpcService {
    boolean addProcurementPlan (StockTransferPlan plan) throws  BizCheckedException;
    boolean updateProcurementPlan(StockTransferPlan plan)  throws BizCheckedException;
    void createProcurement() throws BizCheckedException;
    void scanFromLocation(Map<String, Object> params) throws BizCheckedException;
    void scanToLocation(Map<String, Object> params) throws  BizCheckedException;
    Long assign(Long staffId) throws BizCheckedException;
    boolean checkAndFillPlan(StockTransferPlan plan) throws BizCheckedException;
    boolean checkPlan(StockTransferPlan plan) throws BizCheckedException;
    Set<Long> getOutBoundLocation(Long itemId,Long locationId);
    BigDecimal getQty();
}
