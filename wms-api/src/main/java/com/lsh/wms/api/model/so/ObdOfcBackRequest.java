package com.lsh.wms.api.model.so;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lixin-mac on 16/9/18.
 */
public class ObdOfcBackRequest implements Serializable{
    /**wms类型*/
    private Integer wms;

    /**	SO单号*/
    private String soCode;
    /**obd单号*/
    private String obdCode;
    /**发货时间*/
    private String deliveryTime;

    private List<ObdOfcItem> details;

    public ObdOfcBackRequest(){}

    public ObdOfcBackRequest(String deliveryTime, List<ObdOfcItem> details, String obdCode, String soCode, Integer wms) {
        this.deliveryTime = deliveryTime;
        this.details = details;
        this.obdCode = obdCode;
        this.soCode = soCode;
        this.wms = wms;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public List<ObdOfcItem> getDetails() {
        return details;
    }

    public void setDetails(List<ObdOfcItem> details) {
        this.details = details;
    }

    public String getObdCode() {
        return obdCode;
    }

    public void setObdCode(String obdCode) {
        this.obdCode = obdCode;
    }

    public String getSoCode() {
        return soCode;
    }

    public void setSoCode(String soCode) {
        this.soCode = soCode;
    }

    public Integer getWms() {
        return wms;
    }

    public void setWms(Integer wms) {
        this.wms = wms;
    }
}
