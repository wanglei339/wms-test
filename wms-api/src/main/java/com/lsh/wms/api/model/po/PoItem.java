package com.lsh.wms.api.model.po;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * Created by panxudong on 16/7/15.
 */
public class PoItem {

    /** 商品ID */
    @NotNull
    private Long skuId;

    /** 商品名称 */
    @Size(max=50)
    private String skuName;

    /** 国条码 */
    @NotBlank
    @Size(max=64)
    private String barCode;

    /** 进货数 */
    @NotNull
    private Long orderQty;

    /** 包装单位 */
    private Long packUnit;

    /** 产地 */
    @Size(max=100)
    private String madein;

    /** 实际收货数 */
    private Long inboundQty;

    public PoItem() {

    }

    public PoItem(Long skuId, String skuName, String barCode, Long orderQty,
                  Long packUnit, String madein, Long inboundQty) {
        this.skuId = skuId;
        this.skuName = skuName;
        this.barCode = barCode;
        this.orderQty = orderQty;
        this.packUnit = packUnit;
        this.madein = madein;
        this.inboundQty = inboundQty;
    }

    public Long getSkuId(){
        return this.skuId;
    }

    public void setSkuId(Long skuId){
        this.skuId = skuId;
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

    public Long getOrderQty(){
        return this.orderQty;
    }

    public void setOrderQty(Long orderQty){
        this.orderQty = orderQty;
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

}
