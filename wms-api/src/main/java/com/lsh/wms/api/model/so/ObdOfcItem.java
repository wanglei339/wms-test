package com.lsh.wms.api.model.so;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by lixin-mac on 16/9/19.
 */
public class ObdOfcItem implements Serializable{
    /**skuCode*/
    private String supplySkuCode;
    /**出库数量 ea*/
    private BigDecimal skuQty;
    /**packUnit*/
    private BigDecimal packNum;

    public ObdOfcItem(){}

    public ObdOfcItem(BigDecimal packNum, BigDecimal skuQty, String supplySkuCode) {
        this.packNum = packNum;
        this.skuQty = skuQty;
        this.supplySkuCode = supplySkuCode;
    }

    public BigDecimal getPackNum() {
        return packNum;
    }

    public void setPackNum(BigDecimal packNum) {
        this.packNum = packNum;
    }

    public BigDecimal getSkuQty() {
        return skuQty;
    }

    public void setSkuQty(BigDecimal skuQty) {
        this.skuQty = skuQty;
    }

    public String getSupplySkuCode() {
        return supplySkuCode;
    }

    public void setSupplySkuCode(String supplySkuCode) {
        this.supplySkuCode = supplySkuCode;
    }
}
