package com.lsh.wms.api.service.inhouse;

import java.util.Map;

/**
 * Created by mali on 16/7/14.
 */
public interface IStockTakingRestService {
    String create(String stockTakingInfo);
//    String getList(Map<String, Object> queryMap);
    String view(Long stockTakingId);
    String genId();
    String getLocationList(int locationNum);
//    String getCount(Map<String, Object> mapQuery);
}
