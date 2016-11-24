package com.lsh.wms.model.stock;

import com.lsh.base.common.utils.DateUtils;

import java.math.BigDecimal;

/**
 * Created by mali on 16/11/23.
 */
public class StockAllocDetail {
    /**  */
    private Long id;
    /** 内部商品ID */
    private Long itemId;
    private Long obdId;
    private String obdDetailId;
    /** 库存数量变化值 */
    private BigDecimal qty;
    /** 锁定数量变化值 */
    private int status;
    /**  */
    private Long createdAt;
    /**  */
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

    public Long getObdId() {
        return obdId;
    }

    public void setObdId(Long obdId) {
        this.obdId = obdId;
    }

    public String getObdDetailId() {
        return obdDetailId;
    }

    public void setObdDetailId(String obdDetailId) {
        this.obdDetailId = obdDetailId;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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
