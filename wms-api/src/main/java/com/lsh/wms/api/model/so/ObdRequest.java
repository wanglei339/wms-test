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
    private Long warehouseId;

    @NotBlank
    @Size(max=100)
    private String orderOtherId;


    @Size(max=100)
    private String orderOtherRefId = "";

    /** 下单用户 */
    @NotBlank
    @Size(max=64)
    private String orderUser;

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

    public ObdRequest(Long warehouseId, String orderUser,String orderOtherId, String orderOtherRefId, Long ownerUid, Integer orderType, Date transTime, String deliveryAddrs, List<ObdDetail> detailList) {
        this.warehouseId = warehouseId;
        this.orderUser = orderUser;
        this.orderOtherId = orderOtherId;
        this.orderOtherRefId = orderOtherRefId;
        this.ownerUid = ownerUid;
        this.orderType = orderType;
        this.transTime = transTime;
        this.deliveryAddrs = deliveryAddrs;
        this.detailList = detailList;
    }

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
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
}
