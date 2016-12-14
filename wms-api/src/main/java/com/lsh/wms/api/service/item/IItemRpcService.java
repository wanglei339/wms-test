package com.lsh.wms.api.service.item;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.baseinfo.BaseinfoItemLocation;
import com.lsh.wms.model.csi.CsiSku;

import java.math.BigDecimal;
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
    public BaseinfoItem getItemsBySkuCode(long iOwnerId, String sSkuCode);
    public List<BaseinfoItem> searchItem(Map<String, Object> mapQuery);
    BaseinfoItem insertItem(BaseinfoItem item);

    BaseinfoItem updateItem(BaseinfoItem item);

    List<BaseinfoItemLocation> getItemLocationList(long iItemId);
    List<BaseinfoItemLocation> getItemLocationByLocationID(long iLocationId);
    List<BaseinfoItemLocation> getItemLocation(Map<String,Object> queryMap);

    BaseinfoItemLocation insertItemLocation(BaseinfoItemLocation itemLocation);
    void updateItemLocation(BaseinfoItemLocation itemLocation);

    BaseinfoItem getItem(long itemId);
    BigDecimal getPackUnit(String str) throws BizCheckedException;

    void updateBarcode(Long itemId,String barcode) throws BizCheckedException;
}
