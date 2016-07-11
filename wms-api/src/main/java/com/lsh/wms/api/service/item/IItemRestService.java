package com.lsh.wms.api.service.item;

import com.lsh.wms.model.baseinfo.BaseinfoItem;

import java.util.Map;

/**
 * Created by zengwenjun on 16/7/8.
 */
public interface IItemRestService {
    public String getItem(long iOwnerId, long iSkuId);
    public String getSku(long iSkuId);
    public String getSkuByCode(int iCodeType, String sCode);
    public String getItemsBySkuCode(long iOwnerId, String sSkuCode);
    public String searchItem(Map<String, Object> mapQuery);
    public String insertItem(BaseinfoItem item);
}