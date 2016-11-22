package com.lsh.wms.model.stock;

import com.lsh.base.common.utils.DateUtils;

import java.math.BigDecimal;

/**
 * Created by mali on 16/11/22.
 */
public class StockSummary {
    private Long id;
    private Long itemId;
    private String skuCode;
    private Long ownerId;
    private BigDecimal inhouseQty = BigDecimal.ZERO;
    private BigDecimal allocQty = BigDecimal.ZERO;
    private BigDecimal availQty;
    private Long createdAt = DateUtils.getCurrentSeconds();
    private Long updatedAt = DateUtils.getCurrentSeconds();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public BigDecimal getInhouseQty() {
        return inhouseQty;
    }

    public void setInhouseQty(BigDecimal inhouseQty) {
        this.inhouseQty = inhouseQty;
    }

    public BigDecimal getAllocQty() {
        return allocQty;
    }

    public void setAllocQty(BigDecimal allocQty) {
        this.allocQty = allocQty;
    }

    public BigDecimal getAvailQty() {
        return availQty;
    }

    public void setAvailQty(BigDecimal availQty) {
        this.availQty = availQty;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }
}
