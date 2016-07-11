package com.lsh.wms.api.service.csi;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.wms.model.csi.CsiCategory;
import com.lsh.wms.model.csi.CsiOwner;
import com.lsh.wms.model.csi.CsiSku;
import com.lsh.wms.model.csi.CsiSupplier;

import java.util.List;

/**
 * Created by zengwenjun on 16/7/8.
 */
@Service(protocol = "dubbo")
public interface ICsiRpcService {
    public CsiCategory getCatInfo(int iCatId);
    public List<CsiCategory> getCatFull(int iCatId);
    public List<CsiCategory> getCatChilds(int iCatId);

    public CsiOwner getOwner(int iOwnerId);

    public CsiSku getSku(long iSkuId);
    public CsiSku getSkuByCode(int iCodeType, String sCode);
    public CsiSku insertSku(CsiSku sku);

    public CsiSupplier getSupplier(int iSupplierId);

    public int updateSku(CsiSku sku);

    public CsiSupplier insertSupplier(CsiSupplier supplier);
    int updateSupplier(CsiSupplier supplier);

    CsiCategory insertCategory(CsiCategory category);
    int updateCategory(CsiCategory category);

    CsiOwner insertOwner(CsiOwner owner);
    int updateOwner(CsiOwner owner);

}
