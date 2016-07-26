package com.lsh.wms.model.baseinfo;

import java.math.BigDecimal;

/**
 * 这是个父类 只对外提供id
 * Created by zengwenjun on 16/7/23.
 */
public class IBaseinfoLocaltionModel {

    /** 位置id */
    private Long locationId;

    /**
     * 是否可用
     */
    protected String isUsed;
    /**
     * 数量
     */
    protected BigDecimal qty;

    public String getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(String isUsed) {
        this.isUsed = isUsed;
    }

    public BigDecimal getQty() {
        return qty;
    }


    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public Long getLocationId() {
        return locationId;
    }
}
