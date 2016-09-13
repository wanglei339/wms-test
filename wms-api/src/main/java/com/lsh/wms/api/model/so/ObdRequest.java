package com.lsh.wms.api.model.so;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by mali on 16/9/2.
 */
@JsonIgnoreProperties(ignoreUnknown = false)
public class ObdRequest implements Serializable {
    /** 仓库ID */
    @NotNull
    private String warehouseCode;

    @NotBlank
    @Size(max=100)
    private String orderOtherId;


    @Size(max=100)
    private String orderOtherRefId = "";

    /** 售达方编码 */
    @NotBlank
    @Size(max=64)
    private String orderUserCode;

    /** 下单客户（售达方名称） */
    @NotBlank
    @Size(max=64)
    private String orderUser;

    /** 送达方名称 */
    @NotBlank
    @Size(max=64)
    private String deliveryName;

    /** 送达方编码 */
    @NotBlank
    @Size(max=64)
    private String deliveryCode;

    /** 货主 */
    @NotNull
    private Long ownerUid;

    /** 1SO单，2供商退货单，3调拨出库单 */
    @NotNull
    private Integer orderType;

    /** 发货时间 */
    private Date transTime = new Date();

    /** 收货地址 */
    @Size(max=1000)
    private String deliveryAddrs = "";

    @Valid
    @Size(min=1)
    List<ObdDetail> detailList;

    public ObdRequest() {
    }

    public ObdRequest(String deliveryAddrs, String deliveryCode, String deliveryName, List<ObdDetail> detailList, String orderOtherId, String orderOtherRefId, Integer orderType, String orderUser, String orderUserCode, Long ownerUid, Date transTime, String warehouseCode) {
        this.deliveryAddrs = deliveryAddrs;
        this.deliveryCode = deliveryCode;
        this.deliveryName = deliveryName;
        this.detailList = detailList;
        this.orderOtherId = orderOtherId;
        this.orderOtherRefId = orderOtherRefId;
        this.orderType = orderType;
        this.orderUser = orderUser;
        this.orderUserCode = orderUserCode;
        this.ownerUid = ownerUid;
        this.transTime = transTime;
        this.warehouseCode = warehouseCode;
    }

    public String getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }

    public String getOrderOtherId() {
        return orderOtherId;
    }

    public void setOrderOtherId(String orderOtherId) {
        this.orderOtherId = orderOtherId;
    }

    public String getOrderOtherRefId() {
        return orderOtherRefId;
    }

    public void setOrderOtherRefId(String orderOtherRefId) {
        this.orderOtherRefId = orderOtherRefId;
    }

    public Long getOwnerUid() {
        return ownerUid;
    }

    public void setOwnerUid(Long ownerUid) {
        this.ownerUid = ownerUid;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Date getTransTime() {
        return transTime;
    }

    public void setTransTime(Date transTime) {
        this.transTime = transTime;
    }

    public String getDeliveryAddrs() {
        return deliveryAddrs;
    }

    public void setDeliveryAddrs(String deliveryAddrs) {
        this.deliveryAddrs = deliveryAddrs;
    }

    public List<ObdDetail> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<ObdDetail> detailList) {
        this.detailList = detailList;
    }

    public String getOrderUser() {
        return orderUser;
    }

    public void setOrderUser(String orderUser) {
        this.orderUser = orderUser;
    }

    public String getDeliveryCode() {
        return deliveryCode;
    }

    public void setDeliveryCode(String deliveryCode) {
        this.deliveryCode = deliveryCode;
    }

    public String getDeliveryName() {
        return deliveryName;
    }

    public void setDeliveryName(String deliveryName) {
        this.deliveryName = deliveryName;
    }

    public String getOrderUserCode() {
        return orderUserCode;
    }

    public void setOrderUserCode(String orderUserCode) {
        this.orderUserCode = orderUserCode;
    }
}
