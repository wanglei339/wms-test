package com.lsh.wms.api.service.csi;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.model.csi.CsiCategory;
import com.lsh.wms.model.csi.CsiOwner;
import com.lsh.wms.model.csi.CsiSku;
import com.lsh.wms.model.csi.CsiSupplier;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.util.List;
import java.util.Map;

/**
 * Created by zengwenjun on 16/7/8.
 */
public interface ICsiRestService {
    public String getCatInfo(long iCatId);
    public String  getCatFull(long iCatId);
    public String getCatChilds(long iCatId);

    public String getOwner(long iOwnerId);

    public String getSku(long iSkuId);
    public String getSkuByCode(int iCodeType, String sCode);

    public String getSupplier(long iSupplierId);

    public String updateSku(CsiSku sku);

    public String insertSupplier(CsiSupplier supplier);

    String insertSuppliers(CsiSupplier supplier);

    String updateSupplier(CsiSupplier supplier);

    String insertOwner(CsiOwner owner);
    String updateOwner(CsiOwner owner);

    String insertCategory(CsiCategory category);
    String updateCategory(CsiCategory category);
    String insertSku(CsiSku sku) throws BizCheckedException;

    String getOwnerList(Map<String,Object> mapQuery);
    String getOwnerCount(Map<String,Object> mapQuery);

    String getSupplierList(Map<String, Object> mapQuery);
    String getSupplierCount(Map<String, Object> mapQuery);

    public String getCustomerList(Map<String, Object> mapQuery);

    public String getCustomerCount(Map<String, Object> mapQuery);

    String getCustomerByCustomerCode(Map<String, Object> mapQuery);

}
