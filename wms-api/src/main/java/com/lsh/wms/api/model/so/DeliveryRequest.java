package com.lsh.wms.api.model.so;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by panxudong on 16/7/14.
 */
public class DeliveryRequest implements Serializable {

    /** 仓库ID */
    @NotNull
    private Long warehouseId;

    /** 集货区编码 */
    @NotBlank
    @Size(max=100)
    private String shippingAreaCode;

    /** 波次号 */
    @NotNull
    private Long waveId;

    /** TMS线路 */
    @Size(max=64)
    private String transPlan;

    /** 交货时间 */
    private Date transTime;

    /** 出库单号 */
    @NotBlank
    @Size(max=100)
    private String deliveryCode;

    /** 出库人员 */
    @NotBlank
    @Size(max=64)
    private String deliveryUser;

    /** 出库时间 */
    private Date deliveryTime;

    /** 商品 */
    @Valid
    @Size(min=1)
    private List<DeliveryItem> items;

    public DeliveryRequest() {
    }

    public DeliveryRequest(Long warehouseId, String shippingAreaCode, Long waveId, String transPlan, Date transTime,
                           String deliveryCode, String deliveryUser, Date deliveryTime, List<DeliveryItem> items) {
        this.warehouseId = warehouseId;
        this.shippingAreaCode = shippingAreaCode;
        this.waveId = waveId;
        this.transPlan = transPlan;
        this.transTime = transTime;
        this.deliveryCode = deliveryCode;
        this.deliveryUser = deliveryUser;
        this.deliveryTime = deliveryTime;
        this.items = items;
    }

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getShippingAreaCode() {
        return shippingAreaCode;
    }

    public void setShippingAreaCode(String shippingAreaCode) {
        this.shippingAreaCode = shippingAreaCode;
    }

    public Long getWaveId() {
        return waveId;
    }

    public void setWaveId(Long waveId) {
        this.waveId = waveId;
    }

    public String getTransPlan() {
        return transPlan;
    }

    public void setTransPlan(String transPlan) {
        this.transPlan = transPlan;
    }

    public Date getTransTime() {
        return transTime;
    }

    public void setTransTime(Date transTime) {
        this.transTime = transTime;
    }

    public String getDeliveryCode() {
        return deliveryCode;
    }

    public void setDeliveryCode(String deliveryCode) {
        this.deliveryCode = deliveryCode;
    }

    public String getDeliveryUser() {
        return deliveryUser;
    }

    public void setDeliveryUser(String deliveryUser) {
        this.deliveryUser = deliveryUser;
    }

    public Date getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Date deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public List<DeliveryItem> getItems() {
        return items;
    }

    public void setItems(List<DeliveryItem> items) {
        this.items = items;
    }
}
