package com.lsh.wms.api.model.po;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

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

    private String orderOtherRefId;

    @NotNull
    private Long ownerUid;

    /** 1收货单，2退货单，3调货单 */
    @NotNull
    private Integer orderType;

    /** 供商编码 */
    @NotNull
    private String supplierCode = "";

    /** 订单日期 */
    @NotNull
    private Date orderTime = new Date();

    /** 截止收货时间 */
    private Date endDeliveryDate = new Date();

    /** 商品 */
    @Valid
    @Size(min=1)
    private List<IbdDetail> detailList;

    public IbdRequest() {}

    public IbdRequest(String warehouseCode, String orderOtherId, String orderOtherRefId,
                      Long ownerUid, Integer orderType,
                      String supplierCode, Date orderTime,
                      Date endDeliveryDate, List<IbdDetail> detailList) {
        this.warehouseCode = warehouseCode;
        this.orderOtherId = orderOtherId;
        this.orderOtherRefId = orderOtherRefId;
        this.ownerUid = ownerUid;
        this.orderType = orderType;
        this.supplierCode = supplierCode;
        this.orderTime = orderTime;
        this.endDeliveryDate = endDeliveryDate;
        this.detailList = detailList;
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

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public Date getEndDeliveryDate() {
        return endDeliveryDate;
    }

    public void setEndDeliveryDate(Date endDeliveryDate) {
        this.endDeliveryDate = endDeliveryDate;
    }

    public List<IbdDetail> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<IbdDetail> detailList) {
        this.detailList = detailList;
    }

}
