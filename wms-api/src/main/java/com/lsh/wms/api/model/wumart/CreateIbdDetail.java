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

    /**
     * wms单号
     */
    private String vendMat;

    /**订单类型*/
    private Integer orderType;


    public CreateIbdDetail(){}

    public CreateIbdDetail(BigDecimal deliveQty, String material, Integer orderType, String poItme, String poNumber, String unit, String vendMat) {
        this.deliveQty = deliveQty;
        this.material = material;
        this.orderType = orderType;
        this.poItme = poItme;
        this.poNumber = poNumber;
        this.unit = unit;
        this.vendMat = vendMat;
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

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
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

    public String getVendMat() {
        return vendMat;
    }

    public void setVendMat(String vendMat) {
        this.vendMat = vendMat;
    }
}
