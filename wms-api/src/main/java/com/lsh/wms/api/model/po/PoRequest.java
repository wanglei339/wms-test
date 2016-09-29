package com.lsh.wms.api.model.po;

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
public class PoRequest implements Serializable {

    @NotBlank
    @Size(max=100)
    private String orderOtherId;


    @Size(max=100)
    private String orderOtherRefId = "";

    /** 采购组 */
    @Size(max=64)
    private String orderUser = "";

    /** 货主 */
    @NotNull
    private Long ownerUid;

    /** 1收货单，2退货单，3调货单 */
    @NotNull
    private Integer orderType;

    /** 供商编码 */
    @NotNull
    private Long supplierCode = 0L;

    /** 供商名称 */
    @Size(max=50)
    private String supplierName = "";

    /** 订单日期 */
    @NotNull
    private Date orderTime = new Date();

    /** 库存地 */
    @Size(max=100)
    private String stockCode = "";


    /** 发货时间 */
    private Date deliveryDate = new Date();

    /** 截止收货时间 */
    private Date endDeliveryDate = new Date();

    /** 商品 */
    @Valid
    @Size(min=1)
    private List<PoItem> items;

    public PoRequest() {

    }

    public PoRequest(Date deliveryDate, Date endDeliveryDate, List<PoItem> items, String orderOtherId, String orderOtherRefId, Date orderTime, Integer orderType, String orderUser, Long ownerUid, String stockCode, Long supplierCode, String supplierName) {
        this.deliveryDate = deliveryDate;
        this.endDeliveryDate = endDeliveryDate;
        this.items = items;
        this.orderOtherId = orderOtherId;
        this.orderOtherRefId = orderOtherRefId;
        this.orderTime = orderTime;
        this.orderType = orderType;
        this.orderUser = orderUser;
        this.ownerUid = ownerUid;
        this.stockCode = stockCode;
        this.supplierCode = supplierCode;
        this.supplierName = supplierName;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Date getEndDeliveryDate() {
        return endDeliveryDate;
    }

    public void setEndDeliveryDate(Date endDeliveryDate) {
        this.endDeliveryDate = endDeliveryDate;
    }

    public List<PoItem> getItems() {
        return items;
    }

    public void setItems(List<PoItem> items) {
        this.items = items;
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

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
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

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public Long getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(Long supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }
}
