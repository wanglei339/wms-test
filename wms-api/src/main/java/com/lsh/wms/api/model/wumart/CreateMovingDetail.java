package com.lsh.wms.api.model.wumart;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by lixin-mac on 2016/12/7.
 */
public class CreateMovingDetail implements Serializable{
    private String fromLocation;
    private String toLocation;
    private String skuCode;
    private BigDecimal qty;
    private String packName;

    public CreateMovingDetail() {
    }

    public CreateMovingDetail(String fromLocation, String packName, BigDecimal qty, String skuCode, String toLocation) {
        this.fromLocation = fromLocation;
        this.packName = packName;
        this.qty = qty;
        this.skuCode = skuCode;
        this.toLocation = toLocation;
    }

    public String getFromLocation() {
        return fromLocation;
    }

    public void setFromLocation(String fromLocation) {
        this.fromLocation = fromLocation;
    }

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public String getToLocation() {
        return toLocation;
    }

    public void setToLocation(String toLocation) {
        this.toLocation = toLocation;
    }
}
