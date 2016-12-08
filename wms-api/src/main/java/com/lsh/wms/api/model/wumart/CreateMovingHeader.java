package com.lsh.wms.api.model.wumart;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lixin-mac on 2016/12/7.
 */
public class CreateMovingHeader implements Serializable{
    private List<CreateMovingDetail> details;

    public CreateMovingHeader() {
    }

    public CreateMovingHeader(List<CreateMovingDetail> details) {
        this.details = details;
    }

    public List<CreateMovingDetail> getDetails() {
        return details;
    }

    public void setDetails(List<CreateMovingDetail> details) {
        this.details = details;
    }
}
