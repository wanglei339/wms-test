package com.lsh.wms.api.service.csi;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.wms.model.csi.*;

import java.util.List;

/**
 * Created by zengwenjun on 16/7/8.
 */
@Service(protocol = "dubbo")
public interface ICsiRpcService {
     CsiCategory getCatInfo(long iCatId);
     List<CsiCategory> getCatFull(long iCatId);
     List<CsiCategory> getCatChilds(long iCatId);

     CsiOwner getOwner(long iOwnerId);

     CsiSku getSku(long iSkuId);
     CsiSku getSkuByCode(int iCodeType, String sCode);
    CsiSku insertSku(CsiSku sku);

     CsiSupplier getSupplier(long iSupplierId);

    CsiSupplier getSupplier(String supplierCode,Long ownerId);

    void updateSku(CsiSku sku);

    void insertSupplier(CsiSupplier supplier);
    void updateSupplier(CsiSupplier supplier);

    void insertCategory(CsiCategory category);
    void updateCategory(CsiCategory category);

    void insertOwner(CsiOwner owner);
    void updateOwner(CsiOwner owner);


    public CsiCustomer getCustomer(long customerId);

    public CsiCustomer getCustomerByCode(long ownerId, String customerCode);

}
