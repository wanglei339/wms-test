package com.lsh.wms.api.model.po;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by panxudong on 16/7/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PoItem {

    /** 物美码 */
    @NotNull
    private String skuCode;

    /** 商品名称 */
    @Size(max=50)
    private String skuName;

    /** 国条码 */
    //@NotBlank
    @Size(max=64)
    private String barCode;

    /** 进货数 */
    @NotNull
    private BigDecimal orderQty;

    /** 包装单位 */
    private BigDecimal packUnit;

    /** 包装名称 */
    private String packName;

    /** 价格 */
    private BigDecimal price;

    /** 产地 */
    @Size(max=100)
    private String madein;

    /** 批次号 */
    @Size(max=64)
    private String lotNum;

    public String getLotNum() {
        return lotNum;
    }

    public void setLotNum(String lotNum) {
        this.lotNum = lotNum;
    }

    public PoItem() {

    }

    public PoItem(String skuCode, String skuName, String barCode, BigDecimal orderQty,
                  BigDecimal packUnit,BigDecimal price, String madein, String lotNum, String packName) {
        this.skuCode = skuCode;
        this.skuName = skuName;
        this.barCode = barCode;
        this.orderQty = orderQty;
        this.packUnit = packUnit;
        this.price = price;
        this.madein = madein;
        this.lotNum = lotNum;
        this.packName = packName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
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

    public BigDecimal getOrderQty(){
        return this.orderQty;
    }

    public void setOrderQty(BigDecimal orderQty){
        this.orderQty = orderQty;
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

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }
}
