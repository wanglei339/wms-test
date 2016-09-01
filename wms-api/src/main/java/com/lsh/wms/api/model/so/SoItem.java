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

    /** 外部商品编码 */
    @NotNull
    private String skuCode;

    /** 商品名称 */
    @Size(max=50)
    private String skuName;

    /** 国条码 */
    @NotBlank
    @Size(max=64)
    private String barCode;

    /** 订货数 */
    @NotNull
    private BigDecimal orderQty;

    /** 包装单位 */
    private BigDecimal packUnit;

    /** 批次号 */
    @Size(max=64)
    private String lotNum;

    public SoItem() {

    }

    public SoItem(String skuCode, String skuName, String barCode, BigDecimal orderQty, BigDecimal packUnit, String lotNum) {
        this.skuCode = skuCode;
        this.skuName = skuName;
        this.barCode = barCode;
        this.orderQty = orderQty;
        this.packUnit = packUnit;
        this.lotNum = lotNum;
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

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public BigDecimal getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(BigDecimal orderQty) {
        this.orderQty = orderQty;
    }

    public BigDecimal getPackUnit() {
        return packUnit;
    }

    public void setPackUnit(BigDecimal packUnit) {
        this.packUnit = packUnit;
    }

    public String getLotNum() {
        return lotNum;
    }

    public void setLotNum(String lotNum) {
        this.lotNum = lotNum;
    }
}
