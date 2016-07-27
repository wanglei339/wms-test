package com.lsh.wms.api.service.inhouse;

import com.lsh.base.common.exception.BizCheckedException;

import javax.ws.rs.QueryParam;
import java.util.Map;

/**
 * Created by mali on 16/7/14.
 */
public interface IStockTakingRestService {
    String create(String stockTakingInfo) throws BizCheckedException;
    String genId();
    String getLocationList(int locationNum);
    String getDetail(long takingId) throws BizCheckedException;
    String getCount(Map<String,Object> mapQuery);
    String getList(Map<String,Object> mapQuery) throws BizCheckedException;
}
