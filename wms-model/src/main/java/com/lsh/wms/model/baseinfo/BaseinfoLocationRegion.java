package com.lsh.wms.model.baseinfo;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

public class BaseinfoLocationRegion implements Serializable,IBaseinfoLocaltionModel {

    /**  */
    private Long id;
    /**
     * 位置id
     */
    private Long locationId;
    /**
     * 长度默认单位 米
     */
    private BigDecimal length;
    /**
     * 宽度默认单位 米
     */
    private BigDecimal width;
    /**
     * 创建日期
     */
    private Long createdAt;
    /**
     * 更新日期
     */
    private Long updatedAt;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLocationId() {
        return this.locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public BigDecimal getLength() {
        return this.length;
    }

    public void setLength(BigDecimal length) {
        this.length = length;
    }

    public BigDecimal getWidth() {
        return this.width;
    }

    public void setWidth(BigDecimal width) {
        this.width = width;
    }

    public Long getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }


}
