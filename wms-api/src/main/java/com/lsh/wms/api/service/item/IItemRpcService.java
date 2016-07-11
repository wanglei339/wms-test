package com.lsh.wms.api.service.item;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.csi.CsiSku;

import java.util.List;
import java.util.Map;

/**
 * Created by zengwenjun on 16/7/8.
 */
@Service(protocol = "dubbo")
public interface IItemRpcService {
    public BaseinfoItem getItem(long iOwnerId, long iSkuId);
    public CsiSku getSku(long iSkuId);
    public CsiSku getSkuByCode(int iCodeType, String sCode);
    public List<BaseinfoItem> getItemsBySkuCode(long iOwnerId, String sSkuCode);
    public List<BaseinfoItem> searchItem(Map<String, Object> mapQuery);
    public BaseinfoItem insertItem(BaseinfoItem item);

    int updateItem(BaseinfoItem item);
}
