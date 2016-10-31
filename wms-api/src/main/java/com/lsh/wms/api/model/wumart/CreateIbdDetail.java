package com.lsh.wms.api.model.wumart;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by lixin-mac on 2016/10/28.
 */
public class CreateIbdDetail implements Serializable{

    /**物料号*/
    private String material;

    /**实际出库数量*/
    private BigDecimal deliveQty;

    /**采购订单的计量单位 */
    private String unit;

    /**采购凭证号*/
    private String poNumber;

    /**行项目号*/
    private String poItme;

    public CreateIbdDetail(){}

    public CreateIbdDetail(BigDecimal deliveQty, String material, String poItme, String poNumber, String unit) {
        this.deliveQty = deliveQty;
        this.material = material;
        this.poItme = poItme;
        this.poNumber = poNumber;
        this.unit = unit;
    }

    public BigDecimal getDeliveQty() {
        return deliveQty;
    }

    public void setDeliveQty(BigDecimal deliveQty) {
        this.deliveQty = deliveQty;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getPoItme() {
        return poItme;
    }

    public void setPoItme(String poItme) {
        this.poItme = poItme;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
