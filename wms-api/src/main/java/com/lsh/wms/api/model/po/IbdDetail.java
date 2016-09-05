package com.lsh.wms.api.model.po;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by mali on 16/9/2.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class IbdDetail implements Serializable {

    /** 物美码 */
    @NotNull
    private String skuCode;

    /** 国条码 */
    //@NotBlank
    private String barCode;

    /** 进货数 */
    @NotNull
    private BigDecimal orderQty;

    /** 包装单位 */
    @NotNull
    private BigDecimal packUnit;

    /** 包装名称 */
    @NotNull
    private String packName;

    /** 价格 */
    @NotNull
    private BigDecimal price;

    public IbdDetail() {}

    public IbdDetail(String skuCode, String barCode, BigDecimal orderQty, BigDecimal packUnit, String packName, BigDecimal price) {
        this.skuCode = skuCode;
        this.barCode = barCode;
        this.orderQty = orderQty;
        this.packUnit = packUnit;
        this.packName = packName;
        this.price = price;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
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

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
