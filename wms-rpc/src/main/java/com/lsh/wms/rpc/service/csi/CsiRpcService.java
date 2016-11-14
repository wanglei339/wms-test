package com.lsh.wms.rpc.service.csi;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.wms.api.service.csi.ICsiRpcService;
import com.lsh.wms.core.service.csi.*;
import com.lsh.wms.model.csi.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created by zengwenjun on 16/7/8.
 */
@Service(protocol = "dubbo")
public class CsiRpcService implements ICsiRpcService {
    private static Logger logger = LoggerFactory.getLogger(CsiRpcService.class);

    @Autowired
    private CsiCategoryService categoryService;
    @Autowired
    private CsiSkuService skuService;
    @Autowired
    private CsiOwnerService ownerService;
    @Autowired
    private CsiSupplierService supplierService;
    @Autowired
    private CsiCustomerService customerService;

    public CsiCategory getCatInfo(long iCatId) {
        return categoryService.getCatInfo(iCatId);
    }

    public List<CsiCategory> getCatFull(long iCatId) {
        return categoryService.getFullCatInfo(iCatId);
    }

    public List<CsiCategory> getCatChilds(long iCatId) {
        return categoryService.getChilds(iCatId);
    }

    public CsiOwner getOwner(long iOwnerId) {
        return ownerService.getOwner(iOwnerId);
    }

    public CsiSku getSku(long iSkuId) {
        return skuService.getSku(iSkuId);
    }

    public CsiSku getSkuByCode(int iCodeType, String sCode) {
        return skuService.getSkuByCode(iCodeType, sCode);
    }

    public CsiSku insertSku(CsiSku sku) {
        skuService.insertSku(sku);
        return sku;
    }

    public CsiSupplier getSupplier(long iSupplierId) {
        return supplierService.getSupplier(iSupplierId);
    }

    public CsiSupplier getSupplier(String supplierCode,Long ownerId) {
        return supplierService.getSupplier(supplierCode,ownerId);
    }


    public void updateSku(CsiSku sku){
        skuService.updateSku(sku);
    }


    public void insertSupplier(CsiSupplier supplier){
        supplierService.insertSupplier(supplier);

    }
    public void updateSupplier(CsiSupplier supplier){
        supplierService.updateSupplier(supplier);
    }

    public void insertCategory(CsiCategory category) {
        categoryService.insertCategory(category);
    }

    public void updateCategory(CsiCategory category) {
        categoryService.updateCategory(category);
    }

    public void insertOwner(CsiOwner owner) {
        ownerService.insertOwner(owner);
    }

    public void updateOwner(CsiOwner owner) {
        ownerService.updateOwner(owner);
    }

    public CsiCustomer getCustomer(long customerId){
        return customerService.getCustomerByCustomerId(customerId);
    }

    public CsiCustomer getCustomerByCode(long ownerId, String customerCode){
        return customerService.getCustomerByCustomerCode(ownerId, customerCode);
    }

}
