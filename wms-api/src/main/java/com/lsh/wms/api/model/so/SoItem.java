package com.lsh.wms.api.model.so;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Created by panxudong on 16/7/15.
 */
public class SoItem implements Serializable {

    /** 商品ID */
    @NotNull
    private Long itemId;

    /** 商品名称 */
    @Size(max=50)
    private String skuName;

    /** 国条码 */
    @NotBlank
    @Size(max=64)
    private String barCode;

    /** 订货数 */
    @NotNull
    private Long orderQty;

    /** 包装单位 */
    private Long packUnit;

    /** 批次号 */
    @Size(max=64)
    private String lotNum;

    public SoItem() {

    }

    public SoItem(Long itemId, String skuName, String barCode, Long orderQty, Long packUnit, String lotNum) {
        this.itemId = itemId;
        this.skuName = skuName;
        this.barCode = barCode;
        this.orderQty = orderQty;
        this.packUnit = packUnit;
        this.lotNum = lotNum;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
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

    public Long getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(Long orderQty) {
        this.orderQty = orderQty;
    }

    public Long getPackUnit() {
        return packUnit;
    }

    public void setPackUnit(Long packUnit) {
        this.packUnit = packUnit;
    }

    public String getLotNum() {
        return lotNum;
    }

    public void setLotNum(String lotNum) {
        this.lotNum = lotNum;
    }
}
