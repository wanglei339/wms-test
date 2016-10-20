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


    /** 上游细单Id */
    @NotNull
    private String detailOtherId;

    /** 国条码 */
    //@NotBlank
    private String barCode;

    /** 进货数 */
    @NotNull
    private BigDecimal orderQty = BigDecimal.ZERO;

    /** 包装单位 */
    @NotNull
    private BigDecimal packUnit = BigDecimal.ZERO;

    /** 包装名称 */
    @NotNull
    private String packName = "";

    /** 价格 */
    @NotNull
    private BigDecimal price;

    /** 基本单位名称 */
    private String unitName = "";

    /** 基本单位数量 */
    private BigDecimal unitQty = BigDecimal.ZERO;

    public IbdDetail() {}

    public IbdDetail(String barCode, String detailOtherId, BigDecimal orderQty, String packName, BigDecimal packUnit, BigDecimal price, String skuCode, String unitName, BigDecimal unitQty) {
        this.barCode = barCode;
        this.detailOtherId = detailOtherId;
        this.orderQty = orderQty;
        this.packName = packName;
        this.packUnit = packUnit;
        this.price = price;
        this.skuCode = skuCode;
        this.unitName = unitName;
        this.unitQty = unitQty;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getDetailOtherId() {
        return detailOtherId;
    }

    public void setDetailOtherId(String detailOtherId) {
        this.detailOtherId = detailOtherId;
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

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public BigDecimal getUnitQty() {
        return unitQty;
    }

    public void setUnitQty(BigDecimal unitQty) {
        this.unitQty = unitQty;
    }
}
