package com.lsh.wms.model.taking;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by wuhao on 16/7/29.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationListRequest implements Serializable {
    /** 商品id */
    private Long itemId = 0L;
    /** 库区id */
    private Long areaId = 0L;
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



    public Long getSupplierId() {
        return supplierId;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
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

    public LocationListRequest() {
    }

    public LocationListRequest(Long itemId, Long supplierId, Long areaId, Long storageId, int locationNum) {
        this.itemId = itemId;
        this.supplierId = supplierId;
        this.areaId = areaId;
        this.storageId = storageId;
        this.locationNum = locationNum;
    }

}
