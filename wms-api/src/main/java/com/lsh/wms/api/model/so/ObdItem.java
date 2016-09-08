package com.lsh.wms.api.model.so;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by lixin-mac on 16/9/8.
 */
public class ObdItem implements Serializable{
    /**上游商品编码*/
    private String materiaNo;
    /**购买数量*/
    private String quantity;
    /**实际发货数量*/
    private String sendQuantity;
    /**采购订单的计量单位,如 EA,KG*/
    private String measuringUnit;
    /**商品单价,未税*/
    private BigDecimal price;

    public ObdItem(){}

    public ObdItem(String materiaNo, String measuringUnit, BigDecimal price, String quantity, String sendQuantity) {
        this.materiaNo = materiaNo;
        this.measuringUnit = measuringUnit;
        this.price = price;
        this.quantity = quantity;
        this.sendQuantity = sendQuantity;
    }

    public String getMateriaNo() {
        return materiaNo;
    }

    public void setMateriaNo(String materiaNo) {
        this.materiaNo = materiaNo;
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

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getSendQuantity() {
        return sendQuantity;
    }

    public void setSendQuantity(String sendQuantity) {
        this.sendQuantity = sendQuantity;
    }
}
