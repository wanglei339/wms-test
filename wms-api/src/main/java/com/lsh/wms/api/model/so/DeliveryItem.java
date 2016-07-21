package com.lsh.wms.api.model.so;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Created by panxudong on 16/7/15.
 */
public class DeliveryItem implements Serializable {

    /** 订单ID */
    @NotNull
    private Long orderId;

    /** 商品ID */
    @NotNull
    private Long itemId;

    /** 仓库商品编码 */
    @NotNull
    private Long skuId;

    /** 商品名称 */
    @Size(max=50)
    private String skuName;

    /** 国条码 */
    @NotBlank
    @Size(max=64)
    private String barCode;

    /** 包装单位 */
    private Long packUnit;

    /** 批次号 */
    @Size(max=64)
    private String lotNum;

    /** 出货数 */
    @NotNull
    private Long deliveryNum;

    public DeliveryItem() {

    }

    public DeliveryItem(Long orderId, Long itemId, Long skuId, String skuName, String barCode,
                        Long packUnit, String lotNum, Long deliveryNum) {
        this.orderId = orderId;
        this.itemId = itemId;
        this.skuId = skuId;
        this.skuName = skuName;
        this.barCode = barCode;
        this.packUnit = packUnit;
        this.lotNum = lotNum;
        this.deliveryNum = deliveryNum;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
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

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public Long getPackUnit() {
        return packUnit;
    }

    public void setPackUnit(Long packUnit) {
        this.packUnit = packUnit;
    }

    public String getLotNum() {
        return lotNum;
    }

    public void setLotNum(String lotNum) {
        this.lotNum = lotNum;
    }

    public Long getDeliveryNum() {
        return deliveryNum;
    }

    public void setDeliveryNum(Long deliveryNum) {
        this.deliveryNum = deliveryNum;
    }
}
