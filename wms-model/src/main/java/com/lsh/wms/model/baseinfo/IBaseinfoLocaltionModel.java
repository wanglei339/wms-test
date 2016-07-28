package com.lsh.wms.model.baseinfo;

import java.math.BigDecimal;

/**
 * 这是个父类 只对外提供id
 * Created by zengwenjun on 16/7/23.
 */
public class IBaseinfoLocaltionModel {

    /** 位置id */
    protected Long locationId;

    /**位置的code*/
    protected String code;

    /**
     * 是否可用
     */
    protected String isUsed;

    public String getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(String isUsed) {
        this.isUsed = isUsed;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public Long getLocationId() {
        return locationId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
