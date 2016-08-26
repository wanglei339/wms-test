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
    private String madein;

    /** 实际收货数 */
    @NotNull
    private BigDecimal inboundQty = new BigDecimal(0);

    /** 到货数 */
    @NotNull
    private BigDecimal arriveNum= new BigDecimal(0);

    /** 残次数 */
    private BigDecimal defectNum;

    /** 生产日期 */
    @NotNull
    private Date proTime = new Date();

    /** 拒收原因 */
    @Size(max=100)
    private String refuseReason;

    /**包装名称*/
    private String packName;

    public ReceiptItem() {

    }

    public ReceiptItem(Long skuId,String lotNum,  String skuName, String barCode, BigDecimal packUnit,
                       String madein, BigDecimal inboundQty, BigDecimal arriveNum, BigDecimal defectNum, Date proTime, String refuseReason,String packName) {
        this.skuId = skuId;
        this.lotNum = lotNum;
        this.skuName = skuName;
        this.barCode = barCode;
        this.packUnit = packUnit;
        this.madein = madein;
        this.inboundQty = inboundQty;
        this.arriveNum = arriveNum;
        this.defectNum = defectNum;
        this.proTime = proTime;
        this.refuseReason = refuseReason;
        this.packName = packName;
    }

    public String getLotNum(){
        return this.lotNum;
    }

    public void setLotNum(String lotNum){
        this.lotNum = lotNum;
    }


    public String getSkuName(){
        return this.skuName;
    }

    public void setSkuName(String skuName){
        this.skuName = skuName;
    }

    public String getBarCode(){
        return this.barCode;
    }

    public void setBarCode(String barCode){
        this.barCode = barCode;
    }

    public BigDecimal getPackUnit(){
        return this.packUnit;
    }

    public void setPackUnit(BigDecimal packUnit){
        this.packUnit = packUnit;
    }

    public String getMadein(){
        return this.madein;
    }

    public void setMadein(String madein){
        this.madein = madein;
    }

    public BigDecimal getInboundQty(){
        return this.inboundQty;
    }

    public void setInboundQty(BigDecimal inboundQty){
        this.inboundQty = inboundQty;
    }

    public BigDecimal getArriveNum(){
        return this.arriveNum;
    }

    public void setArriveNum(BigDecimal arriveNum){
        this.arriveNum = arriveNum;
    }

    public BigDecimal getDefectNum(){
        return this.defectNum;
    }

    public void setDefectNum(BigDecimal defectNum){
        this.defectNum = defectNum;
    }

    public Date getProTime() { return proTime; }

    public void setProTime(Date proTime) { this.proTime = proTime; }

    public String getRefuseReason() { return refuseReason; }

    public void setRefuseReason(String refuseReason) { this.refuseReason = refuseReason; }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    public String getPackName() {
        return packName;
    }
}
