package com.lsh.wms.api.service.inhouse;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.model.taking.LocationListRequest;
import com.lsh.wms.model.taking.StockTakingDetail;
import com.lsh.wms.model.taking.StockTakingHead;
import com.lsh.wms.model.taking.StockTakingRequest;

import javax.ws.rs.QueryParam;
import java.util.List;
import java.util.Map;

/**
 * Created by mali on 16/7/14.
 */
public interface IStockTakingProviderRpcService {
    void create(Long locationId,Long uid) throws BizCheckedException;
    void createTask(StockTakingHead head, List<StockTakingDetail> detailList,Long round,Long dueTime) throws BizCheckedException;
    List<StockTakingDetail> prepareDetailList(StockTakingHead head);
    void createPlanWarehouse(List<Long> zoneIds, Long planer) throws BizCheckedException;
    void createPlanSales(List<Long> zoneIds, Long planer) throws BizCheckedException;
}
