package com.lsh.wms.rpc.service.csi;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.wms.api.service.csi.ICsiRpcService;
import com.lsh.wms.core.service.csi.CsiCategoryService;
import com.lsh.wms.core.service.csi.CsiOwnerService;
import com.lsh.wms.core.service.csi.CsiSkuService;
import com.lsh.wms.core.service.csi.CsiSupplierService;
import com.lsh.wms.model.csi.CsiCategory;
import com.lsh.wms.model.csi.CsiOwner;
import com.lsh.wms.model.csi.CsiSku;
import com.lsh.wms.model.csi.CsiSupplier;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by zengwenjun on 16/7/8.
 */
@Service(protocol = "dubbo")
public class CsiRpcService implements ICsiRpcService {
    @Autowired
    private CsiCategoryService categoryService;
    @Autowired
    private CsiSkuService skuService;
    @Autowired
    private CsiOwnerService ownerService;
    @Autowired
    private CsiSupplierService supplierService;

    public CsiCategory getCatInfo(int iCatId) {
        return categoryService.getCatInfo(iCatId);
    }

    public List<CsiCategory> getCatFull(int iCatId) {
        return categoryService.getFullCatInfo(iCatId);
    }

    public List<CsiCategory> getCatChilds(int iCatId) {
        return categoryService.getChilds(iCatId);
    }

    public CsiOwner getOwner(int iOwnerId) {
        return ownerService.getOwner(iOwnerId);
    }

    public CsiSku getSku(long iSkuId) {
        return skuService.getSku(iSkuId);
    }

    public CsiSku getSkuByCode(int iCodeType, String sCode) {
        return skuService.getSkuByCode(iCodeType, sCode);
    }

    public CsiSku insertSku(CsiSku sku) {
        return skuService.insertSku(sku);
    }

    public CsiSupplier getSupplier(int iSupplierId) {
        return supplierService.getSupplier(iSupplierId);
    }

    public int updateSku(CsiSku sku){
        return skuService.updateSku(sku);
    }


    public CsiSupplier insertSupplier(CsiSupplier supplier){
        return supplierService.insertSupplier(supplier);

    }
    public int updateSupplier(CsiSupplier supplier){
        return supplierService.updateSupplier(supplier);
    }

    public CsiCategory insertCategory(CsiCategory category) {
        return categoryService.insertCategory(category);
    }

    public int updateCategory(CsiCategory category) {
        return categoryService.updateCategory(category);
    }

    public CsiOwner insertOwner(CsiOwner owner) {
        return ownerService.insertOwner(owner);
    }

    public int updateOwner(CsiOwner owner) {
        return ownerService.updateOwner(owner);
    }


}
