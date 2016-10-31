package com.lsh.wms.api.model.wumart;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by lixin-mac on 2016/10/28.
 */
public class CreateObdDetail implements Serializable{
    private String refDoc;

    private String refItem;
    private BigDecimal dlvQty;
    private String salesUnit;

    /**物料号*/
    private String material;

    public CreateObdDetail(){}

    public CreateObdDetail(BigDecimal dlvQty, String material, String refDoc, String refItem, String salesUnit) {
        this.dlvQty = dlvQty;
        this.material = material;
        this.refDoc = refDoc;
        this.refItem = refItem;
        this.salesUnit = salesUnit;
    }

    public BigDecimal getDlvQty() {
        return dlvQty;
    }

    public void setDlvQty(BigDecimal dlvQty) {
        this.dlvQty = dlvQty;
    }

    public String getRefDoc() {
        return refDoc;
    }

    public void setRefDoc(String refDoc) {
        this.refDoc = refDoc;
    }

    public String getRefItem() {
        return refItem;
    }

    public void setRefItem(String refItem) {
        this.refItem = refItem;
    }

    public String getSalesUnit() {
        return salesUnit;
    }

    public void setSalesUnit(String salesUnit) {
        this.salesUnit = salesUnit;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }
}
