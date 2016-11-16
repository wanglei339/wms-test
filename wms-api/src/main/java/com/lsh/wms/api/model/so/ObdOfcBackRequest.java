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

    /**运单号*/
    private String waybillCode;
    /**箱数*/
    private Integer boxNum;
    /**周转箱数*/
    private Integer turnoverBoxNum;

    private List<ObdOfcItem> details;

    public ObdOfcBackRequest(){}

    public ObdOfcBackRequest(Integer boxNum, String deliveryTime, List<ObdOfcItem> details, String obdCode, String soCode, Integer turnoverBoxNum, String waybillCode, Integer wms) {
        this.boxNum = boxNum;
        this.deliveryTime = deliveryTime;
        this.details = details;
        this.obdCode = obdCode;
        this.soCode = soCode;
        this.turnoverBoxNum = turnoverBoxNum;
        this.waybillCode = waybillCode;
        this.wms = wms;
    }

    public Integer getBoxNum() {
        return boxNum;
    }

    public void setBoxNum(Integer boxNum) {
        this.boxNum = boxNum;
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

    public Integer getTurnoverBoxNum() {
        return turnoverBoxNum;
    }

    public void setTurnoverBoxNum(Integer turnoverBoxNum) {
        this.turnoverBoxNum = turnoverBoxNum;
    }

    public String getWaybillCode() {
        return waybillCode;
    }

    public void setWaybillCode(String waybillCode) {
        this.waybillCode = waybillCode;
    }

    public Integer getWms() {
        return wms;
    }

    public void setWms(Integer wms) {
        this.wms = wms;
    }
}
