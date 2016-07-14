package com.lsh.wms.api.service.csi;

import com.lsh.wms.model.csi.CsiCategory;
import com.lsh.wms.model.csi.CsiOwner;
import com.lsh.wms.model.csi.CsiSku;
import com.lsh.wms.model.csi.CsiSupplier;

import java.util.List;

/**
 * Created by zengwenjun on 16/7/8.
 */
public interface ICsiRestService {
    public String getCatInfo(int iCatId);
    public String  getCatFull(int iCatId);
    public String getCatChilds(int iCatId);

    public String getOwner(int iOwnerId);

    public String getSku(long iSkuId);
    public String getSkuByCode(int iCodeType, String sCode);

    public String getSupplier(int iSupplierId);

    public String updateSku(CsiSku sku);

    public String insertSupplier(CsiSupplier supplier);
    String updateSupplier(CsiSupplier supplier);

    String insertOwner(CsiOwner owner);
    String updateOwner(CsiOwner owner);

    String insertCategory(CsiCategory category);
    String updateCategory(CsiCategory category);
    String insertSku(CsiSku sku);
}
