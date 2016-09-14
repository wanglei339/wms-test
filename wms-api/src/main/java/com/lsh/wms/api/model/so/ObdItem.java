package com.lsh.wms.api.model.so;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by lixin-mac on 16/9/8.
 */
public class ObdItem implements Serializable{
    /**上游商品编码*/
    private String materialNo;
    /**购买数量*/
    private BigDecimal quantity;
    /**实际发货数量*/
    private BigDecimal sendQuantity;
    /**采购订单的计量单位,如 EA,KG*/
    private String measuringUnit;
    /**商品单价,未税*/
    private BigDecimal price;

    public ObdItem(){}

    public ObdItem(String materialNo, String measuringUnit, BigDecimal price, BigDecimal quantity, BigDecimal sendQuantity) {
        this.materialNo = materialNo;
        this.measuringUnit = measuringUnit;
        this.price = price;
        this.quantity = quantity;
        this.sendQuantity = sendQuantity;
    }

    public String getMaterialNo() {
        return materialNo;
    }

    public void setMaterialNo(String materialNo) {
        this.materialNo = materialNo;
    }

    public String getMeasuringUnit() {
        return measuringUnit;
    }

    public void setMeasuringUnit(String measuringUnit) {
        this.measuringUnit = measuringUnit;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getSendQuantity() {
        return sendQuantity;
    }

    public void setSendQuantity(BigDecimal sendQuantity) {
        this.sendQuantity = sendQuantity;
    }
}
