package com.lsh.wms.api.model.po;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by panxudong on 16/7/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PoItem implements Serializable {

    /**
     * 物美码
     */
    @NotNull
    private String skuCode;

    /**
     * 上游细单Id
     */
    @NotNull
    private String detailOtherId;

    /**
     * 商品名称
     */
    @Size(max = 50)
    private String skuName;


    /**
     * 进货数
     */
    @NotNull
    private BigDecimal orderQty;

    /**
     * 包装单位
     */
    private BigDecimal packUnit;

    /**
     * 包装名称
     */
    private String packName;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 基本单位名称
     */
    private String unitName;

    /**
     * 基本单位数量
     */
    private BigDecimal unitQty;


    public PoItem() {

    }

    public PoItem(String detailOtherId, BigDecimal orderQty, String packName, BigDecimal packUnit, BigDecimal price, String skuCode, String skuName, String unitName, BigDecimal unitQty) {
        this.detailOtherId = detailOtherId;
        this.orderQty = orderQty;
        this.packName = packName;
        this.packUnit = packUnit;
        this.price = price;
        this.skuCode = skuCode;
        this.skuName = skuName;
        this.unitName = unitName;
        this.unitQty = unitQty;
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

    public BigDecimal getUnitQty() {
        return unitQty;
    }

    public void setUnitQty(BigDecimal unitQty) {
        this.unitQty = unitQty;
    }
}