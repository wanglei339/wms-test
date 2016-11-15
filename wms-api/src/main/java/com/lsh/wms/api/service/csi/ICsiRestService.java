package com.lsh.wms.api.service.csi;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.model.csi.*;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.util.List;
import java.util.Map;

/**
 * Created by zengwenjun on 16/7/8.
 */
public interface ICsiRestService {
    public String getCatInfo(long iCatId)throws BizCheckedException;

    public String  getCatFull(long iCatId)throws BizCheckedException;

    public String getCatChilds(long iCatId)throws BizCheckedException;

    public String getOwner(long iOwnerId)throws BizCheckedException;

    public String getSku(long iSkuId)throws BizCheckedException;

    public String getSkuByCode(int iCodeType, String sCode)throws BizCheckedException;

    public String getSupplier(long iSupplierId)throws BizCheckedException;

    public String updateSku(CsiSku sku)throws BizCheckedException;

    public String insertSupplier(CsiSupplier supplier)throws BizCheckedException;

    String insertSuppliers(CsiSupplier supplier)throws BizCheckedException;

    String updateSupplier(CsiSupplier supplier)throws BizCheckedException;

    String insertOwner(CsiOwner owner)throws BizCheckedException;

    String updateOwner(CsiOwner owner)throws BizCheckedException;

    String insertCategory(CsiCategory category)throws BizCheckedException;

    String updateCategory(CsiCategory category)throws BizCheckedException;

    String insertSku(CsiSku sku) throws BizCheckedException;

    String getOwnerList(Map<String,Object> mapQuery)throws BizCheckedException;

    String getOwnerCount(Map<String,Object> mapQuery)throws BizCheckedException;

    String getSupplierList(Map<String, Object> mapQuery)throws BizCheckedException;

    String getSupplierCount(Map<String, Object> mapQuery)throws BizCheckedException;

    public String getCustomerList(Map<String, Object> mapQuery)throws BizCheckedException;

    public String getCustomerCount(Map<String, Object> mapQuery)throws BizCheckedException;

    String getCustomerByCustomerCode(Map<String, Object> mapQuery)throws BizCheckedException;

    String updateCustomer(CsiCustomer csiCustomer)throws BizCheckedException;

    public String getCustomerByCustomerId(Long customerId)throws BizCheckedException;

}
