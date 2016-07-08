package com.lsh.wms.api.service.item;

import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.csi.CsiSku;

import java.util.List;
import java.util.Map;

/**
 * Created by zengwenjun on 16/7/8.
 */
public interface IItemService {
    public BaseinfoItem getItem(long iOwnerId, long iSkuId);
    public CsiSku getBaseInfo(long iSkuId);
    public CsiSku getSkuByCode(int iCodeType, String sCode);
    public List<BaseinfoItem> getItemsBySkuCode(long iOwnerId, String sSkuCode);
    public List<BaseinfoItem> searchItem(Map<String, Object> mapQuery);
}
