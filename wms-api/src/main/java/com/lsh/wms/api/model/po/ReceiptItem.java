package com.lsh.wms.api.model.po;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by panxudong on 16/7/15.
 */
public class ReceiptItem implements Serializable {

    /** 批次号 */
    @NotBlank
    @Size(max=64)
    private String lotNum = "";

    /**orderId*/
    private Long orderId = 0l;

    /** skuId */
    private Long skuId;

    /** 商品名称 */
    @Size(max=50)
    private String skuName;

    /** 国条码 */
    @NotBlank
    @Size(max=64)
    private String barCode;

    /** 包装单位 */
    private BigDecimal packUnit;

    /** 产地 */
    @Size(max=100)
    private String madein = "";

    /** 实际收货数 */
    @NotNull
    private BigDecimal inboundQty = new BigDecimal(0);

    /** 实际散件收货数 */
    @NotNull
    private BigDecimal unitQty = new BigDecimal(0);

    /** 到货数 */
    @NotNull
    private BigDecimal arriveNum= new BigDecimal(0);

    /** 残次数 */
    private BigDecimal defectNum = BigDecimal.ZERO;

    /** 生产日期 */
    @NotNull
    private Date proTime = new Date();

    /** 拒收原因 */
    @Size(max=100)
    private String refuseReason ="";

    /**包装名称*/
    private String packName;

    public ReceiptItem() {

    }

    public ReceiptItem(BigDecimal unitQty, BigDecimal arriveNum, String barCode, BigDecimal defectNum, BigDecimal inboundQty, String lotNum, String madein, Long orderId, String packName, BigDecimal packUnit, Date proTime, String refuseReason, Long skuId, String skuName) {
        this.unitQty = unitQty;
        this.arriveNum = arriveNum;
        this.barCode = barCode;
        this.defectNum = defectNum;
        this.inboundQty = inboundQty;
        this.lotNum = lotNum;
        this.madein = madein;
        this.orderId = orderId;
        this.packName = packName;
        this.packUnit = packUnit;
        this.proTime = proTime;
        this.refuseReason = refuseReason;
        this.skuId = skuId;
        this.skuName = skuName;
    }

    public BigDecimal getArriveNum() {
        return arriveNum;
    }

    public void setArriveNum(BigDecimal arriveNum) {
        this.arriveNum = arriveNum;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public BigDecimal getDefectNum() {
        return defectNum;
    }

    public void setDefectNum(BigDecimal defectNum) {
        this.defectNum = defectNum;
    }

    public BigDecimal getInboundQty() {
        return inboundQty;
    }

    public void setInboundQty(BigDecimal inboundQty) {
        this.inboundQty = inboundQty;
    }

    public String getLotNum() {
        return lotNum;
    }

    public void setLotNum(String lotNum) {
        this.lotNum = lotNum;
    }

    public String getMadein() {
        return madein;
    }

    public void setMadein(String madein) {
        this.madein = madein;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
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

    public Date getProTime() {
        return proTime;
    }

    public void setProTime(Date proTime) {
        this.proTime = proTime;
    }

    public String getRefuseReason() {
        return refuseReason;
    }

    public void setRefuseReason(String refuseReason) {
        this.refuseReason = refuseReason;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public BigDecimal getUnitQty() {
        return unitQty;
    }

    public void setUnitQty(BigDecimal unitQty) {
        this.unitQty = unitQty;
    }
}
