package com.lsh.wms.api.model.so;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by panxudong on 16/7/15.
 */
public class SoItem implements Serializable {
    /** 上游细单Id */
    @NotNull
    private String detailOtherId;

    /** 外部商品编码 */
    @NotNull
    private String skuCode;

    /** 商品名称 */
    @Size(max=50)
    private String skuName;

    /** 订货数 */
    @NotNull
    private BigDecimal orderQty;
    /** 基本单位数量 */
    private String unitQty;

    /** 包装单位 */
    private BigDecimal packUnit;

    /** 包装名称 */
    private String packName;
    /** 基本单位名称 */
    private String unitName;
    /** 批次号 */
    @Size(max=64)
    private String lotCode;

    /** 商品单价,未税 */
    private BigDecimal price;

    public SoItem() {

    }

    public SoItem(String unitQty, String detailOtherId, String lotCode, BigDecimal orderQty, String packName, BigDecimal packUnit, BigDecimal price, String skuCode, String skuName, String unitName) {
        this.unitQty = unitQty;
        this.detailOtherId = detailOtherId;
        this.lotCode = lotCode;
        this.orderQty = orderQty;
        this.packName = packName;
        this.packUnit = packUnit;
        this.price = price;
        this.skuCode = skuCode;
        this.skuName = skuName;
        this.unitName = unitName;
    }

    public String getDetailOtherId() {
        return detailOtherId;
    }

    public void setDetailOtherId(String detailOtherId) {
        this.detailOtherId = detailOtherId;
    }

    public String getLotCode() {
        return lotCode;
    }

    public void setLotCode(String lotCode) {
        this.lotCode = lotCode;
    }

    public BigDecimal getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(BigDecimal orderQty) {
        this.orderQty = orderQty;
    }

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    public BigDecimal getPackUnit() {
        return packUnit;
    }

    public void setPackUnit(BigDecimal packUnit) {
        this.packUnit = packUnit;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getUnitQty() {
        return unitQty;
    }

    public void setUnitQty(String unitQty) {
        this.unitQty = unitQty;
    }
}
