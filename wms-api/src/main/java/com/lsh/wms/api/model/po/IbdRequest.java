package com.lsh.wms.api.model.po;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.format.annotation.DateTimeFormat;

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
public class IbdRequest implements Serializable {

    //@NotNull
    private String warehouseCode = "";

    @NotNull
    private String orderOtherId;

    private String orderOtherRefId = "";

    @NotNull
    private Long ownerUid;

    /** 1收货单，2退货单，3调货单 */
    @NotNull
    private Integer orderType;

    /** 供商编码 */
    //@NotNull
    private String supplierCode = "";

    /** 订单日期 */
    @NotNull
    private String orderTime;

    /** 截止收货时间 */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date endDeliveryDate = new Date();

    /** 返仓客户 */
    @Size(max=64)
    private String orderUser = "";

    /** 商品 */
    @Valid
    @Size(min=1)
    private List<IbdDetail> detailList;

    public IbdRequest() {}

    public IbdRequest(List<IbdDetail> detailList, Date endDeliveryDate, String orderOtherId, String orderOtherRefId, String orderTime, Integer orderType, String orderUser, Long ownerUid, String supplierCode, String warehouseCode) {
        this.detailList = detailList;
        this.endDeliveryDate = endDeliveryDate;
        this.orderOtherId = orderOtherId;
        this.orderOtherRefId = orderOtherRefId;
        this.orderTime = orderTime;
        this.orderType = orderType;
        this.orderUser = orderUser;
        this.ownerUid = ownerUid;
        this.supplierCode = supplierCode;
        this.warehouseCode = warehouseCode;
    }

    public List<IbdDetail> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<IbdDetail> detailList) {
        this.detailList = detailList;
    }

    public Date getEndDeliveryDate() {
        return endDeliveryDate;
    }

    public void setEndDeliveryDate(Date endDeliveryDate) {
        this.endDeliveryDate = endDeliveryDate;
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

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public String getOrderUser() {
        return orderUser;
    }

    public void setOrderUser(String orderUser) {
        this.orderUser = orderUser;
    }

    public Long getOwnerUid() {
        return ownerUid;
    }

    public void setOwnerUid(Long ownerUid) {
        this.ownerUid = ownerUid;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }
}
