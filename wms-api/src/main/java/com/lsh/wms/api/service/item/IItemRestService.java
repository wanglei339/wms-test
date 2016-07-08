package com.lsh.wms.api.service.item;

import java.util.Map;

/**
 * Created by zengwenjun on 16/7/8.
 */
public interface IItemRestService {
    public String getItem(long iOwnerId, long iSkuId);
    public String getBaseInfo(long iSkuId);
    public String getSkuByCode(int iCodeType, String sCode);
    public String getItemsBySkuCode(long iOwnerId, String sSkuCode);
    public String searchItem(Map<String, Object> mapQuery);
}
