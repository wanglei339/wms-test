package com.lsh.wms.api.service.inhouse;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.model.taking.LocationListRequest;
import com.lsh.wms.model.taking.StockTakingRequest;

import javax.ws.rs.QueryParam;
import java.util.List;
import java.util.Map;

/**
 * Created by mali on 16/7/14.
 */
public interface IStockTakingRestService {
    String create(StockTakingRequest request) throws BizCheckedException;
    String update(StockTakingRequest request) throws BizCheckedException;
    String genId();
    String getLocationList(LocationListRequest request);
    String getDetail(long takingId) throws BizCheckedException;
    String getCount(Map<String,Object> mapQuery);
    String getList(Map<String,Object> mapQuery) throws BizCheckedException;
    String getItemList(Long supplierId);
    String getSupplierList(Long itemId);
}
