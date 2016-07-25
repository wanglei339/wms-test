package com.lsh.wms.api.model.location;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 前端校验
 * @Author 马启迪 maqidi@lsh123.com
 * @Date 16/7/24 下午8:16
 */
public class LocationDetailRequest implements Serializable {
    /** 地址id */
    @NotNull
    private Long locationId;
    /** 地址类型 */
    @NotNull
    private Integer type;

    public LocationDetailRequest(Long locationId, Integer type) {
        this.locationId = locationId;
        this.type = type;
    }

    public Long getLocationId() {
        return locationId;
    }

    public Integer getType() {
        return type;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
