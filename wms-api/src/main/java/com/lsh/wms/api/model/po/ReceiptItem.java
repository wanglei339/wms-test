package com.lsh.wms.api.model.po;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by panxudong on 16/7/15.
 */
public class ReceiptItem implements Serializable {

    /** 批次号 */
    @NotBlank
    @Size(max=64)
    private String lotNum;

    /** 商品名称 */
    @Size(max=50)
    private String skuName;

    /** 国条码 */
    @NotBlank
    @Size(max=64)
    private String barCode;

    /** 包装单位 */
    private Long packUnit;

    /** 产地 */
    @Size(max=100)
    private String madein;

    /** 实际收货数 */
    @NotNull
    private Long inboundQty;

    /** 到货数 */
    @NotNull
    private Long arriveNum;

    /** 残次数 */
    private Long defectNum;

    /** 生产日期 */
    @NotNull
    private Date proTime;

    /** 拒收原因 */
    @Size(max=100)
    private String refuseReason;

    public ReceiptItem() {

    }

    public ReceiptItem(String lotNum,  String skuName, String barCode, Long packUnit,
                       String madein, Long inboundQty, Long arriveNum, Long defectNum, Date proTime, String refuseReason) {
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

    public Long getPackUnit(){
        return this.packUnit;
    }

    public void setPackUnit(Long packUnit){
        this.packUnit = packUnit;
    }

    public String getMadein(){
        return this.madein;
    }

    public void setMadein(String madein){
        this.madein = madein;
    }

    public Long getInboundQty(){
        return this.inboundQty;
    }

    public void setInboundQty(Long inboundQty){
        this.inboundQty = inboundQty;
    }

    public Long getArriveNum(){
        return this.arriveNum;
    }

    public void setArriveNum(Long arriveNum){
        this.arriveNum = arriveNum;
    }

    public Long getDefectNum(){
        return this.defectNum;
    }

    public void setDefectNum(Long defectNum){
        this.defectNum = defectNum;
    }

    public Date getProTime() { return proTime; }

    public void setProTime(Date proTime) { this.proTime = proTime; }

    public String getRefuseReason() { return refuseReason; }

    public void setRefuseReason(String refuseReason) { this.refuseReason = refuseReason; }

}
