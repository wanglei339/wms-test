package com.lsh.wms.model.taking;

/**
 * Created by wuhao on 16/7/29.
 */
public class LocationListRequest {
    /** 商品id */
    private Long itemId = 0L;
    /** 库区id */
    private Long AreaId = 0L;
    /**供应商id */
    private Long supplierId = 0L;
    /** 货架id */
    private Long storageId = 0L;
    /** 获取库位的数量*/
    private int locationNum = 0;

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getAreaId() {
        return AreaId;
    }

    public void setAreaId(Long areaId) {
        AreaId = areaId;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public Long getStorageId() {
        return storageId;
    }

    public void setStorageId(Long storageId) {
        this.storageId = storageId;
    }

    public int getLocationNum() {
        return locationNum;
    }

    public void setLocationNum(int locationNum) {
        this.locationNum = locationNum;
    }
}
