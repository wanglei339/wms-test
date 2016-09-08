package com.lsh.wms.api.model.so;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by mali on 16/9/2.
 */
@JsonIgnoreProperties(ignoreUnknown = false)
public class ObdDetail implements Serializable {
    /** 上游细单Id */
    @NotNull
    private String detailOtherId;

    @NotNull
    private String skuCode;

    /** 国条码 */
    //@NotBlank
    private String barCode;

    /** 进货数 */
    @NotNull
    private BigDecimal orderQty;

    /** 包装单位 */
    private BigDecimal packUnit;

    /** 包装名称 */
    private String packName;

    /** 批次信息 */
    private String lotNum;
    /** 商品单价,未税 */
    private BigDecimal price;

    public ObdDetail() {
    }

    public ObdDetail(String detailOtherId,String skuCode, String barCode, BigDecimal orderQty, BigDecimal packUnit, String packName, String lotNum ,BigDecimal price) {
        this.detailOtherId = detailOtherId;
        this.skuCode = skuCode;
        this.barCode = barCode;
        this.orderQty = orderQty;
        this.packUnit = packUnit;
        this.packName = packName;
        this.lotNum = lotNum;
        this.price = price;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public BigDecimal getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(BigDecimal orderQty) {
        this.orderQty = orderQty;
    }

    public BigDecimal getPackUnit() {
        return packUnit;
    }

    public void setPackUnit(BigDecimal packUnit) {
        this.packUnit = packUnit;
    }

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    public String getLotNum() {
        return lotNum;
    }

    public void setLotNum(String lotNum) {
        this.lotNum = lotNum;
    }

    public String getDetailOtherId() {
        return detailOtherId;
    }

    public void setDetailOtherId(String detailOtherId) {
        this.detailOtherId = detailOtherId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
