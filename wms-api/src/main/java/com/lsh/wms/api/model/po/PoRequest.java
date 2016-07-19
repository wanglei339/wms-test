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

    /** 仓库ID */
    @NotNull
    private Long warehouseId;

    @NotBlank
    @Size(max=100)
    private String orderOtherId;

    /** 采购组 */
    @NotBlank
    @Size(max=64)
    private String orderUser;

    /** 货主 */
    @NotNull
    private Long ownerUid;

    /** 1收货单，2退货单，3调货单 */
    @NotNull
    private Integer orderType;

    /** 供商编码 */
    @NotNull
    private Long supplierCode;

    /** 供商名称 */
    @Size(max=50)
    private String supplierName;

    /** 商品凭证号 */
    @Size(max=100)
    private String skuVoucherNo;

    /** 供商电话 */
    @Size(max=50)
    private String supplierPhone;

    /** 供商传真 */
    @Size(max=50)
    private String supplierFax;

    /** 订单日期 */
    @NotNull
    private Date orderTime;

    /** 库存地 */
    @Size(max=100)
    private String stockCode;

    /** 收货地点 */
    @Size(max=64)
    private String deliveryPlace;

    /** 收货地址 */
    @Size(max=1000)
    private String deliveryAddrs;

    /** 发货时间 */
    private Date deliveryDate;

    /** 截止收货时间 */
    private Date endDeliveryDate;

    /** 商品 */
    @Valid
    @Size(min=1)
    private List<PoItem> items;

    public PoRequest() {

    }

    public PoRequest(Long warehouseId, String orderOtherId, String orderUser, Long ownerUid, Integer orderType,
                     Long supplierCode, String supplierName, String skuVoucherNo, String supplierPhone,
                     String supplierFax, Date orderTime, String stockCode, String deliveryPlace, String deliveryAddrs,
                     Date deliveryDate, Date endDeliveryDate, List<PoItem> items) {
        this.warehouseId = warehouseId;
        this.orderOtherId = orderOtherId;
        this.orderUser = orderUser;
        this.ownerUid = ownerUid;
        this.orderType = orderType;
        this.supplierCode = supplierCode;
        this.supplierName = supplierName;
        this.skuVoucherNo = skuVoucherNo;
        this.supplierPhone = supplierPhone;
        this.supplierFax = supplierFax;
        this.orderTime = orderTime;
        this.stockCode = stockCode;
        this.deliveryPlace = deliveryPlace;
        this.deliveryAddrs = deliveryAddrs;
        this.deliveryDate = deliveryDate;
        this.endDeliveryDate = endDeliveryDate;
        this.items = items;
    }

    public Long getWarehouseId(){
        return this.warehouseId;
    }

    public void setWarehouseId(Long warehouseId){
        this.warehouseId = warehouseId;
    }

    public String getOrderOtherId() {
        return orderOtherId;
    }

    public void setOrderOtherId(String orderOtherId) {
        this.orderOtherId = orderOtherId;
    }

    public String getOrderUser() {
        return orderUser;
    }

    public void setOrderUser(String orderUser) {
        this.orderUser = orderUser;
    }

    public Long getOwnerUid(){
        return this.ownerUid;
    }

    public void setOwnerUid(Long ownerUid){
        this.ownerUid = ownerUid;
    }

    public Integer getOrderType(){
        return this.orderType;
    }

    public void setOrderType(Integer orderType){
        this.orderType = orderType;
    }

    public Long getSupplierCode(){
        return this.supplierCode;
    }

    public void setSupplierCode(Long supplierCode){
        this.supplierCode = supplierCode;
    }

    public String getSupplierName(){
        return this.supplierName;
    }

    public void setSupplierName(String supplierName){
        this.supplierName = supplierName;
    }

    public String getSkuVoucherNo(){
        return this.skuVoucherNo;
    }

    public void setSkuVoucherNo(String skuVoucherNo){
        this.skuVoucherNo = skuVoucherNo;
    }

    public String getSupplierPhone(){
        return this.supplierPhone;
    }

    public void setSupplierPhone(String supplierPhone){
        this.supplierPhone = supplierPhone;
    }

    public String getSupplierFax(){
        return this.supplierFax;
    }

    public void setSupplierFax(String supplierFax){
        this.supplierFax = supplierFax;
    }

    public Date getOrderTime(){
        return this.orderTime;
    }

    public void setOrderTime(Date orderTime){
        this.orderTime = orderTime;
    }

    public String getStockCode(){
        return this.stockCode;
    }

    public void setStockCode(String stockCode){
        this.stockCode = stockCode;
    }

    public String getDeliveryPlace(){
        return this.deliveryPlace;
    }

    public void setDeliveryPlace(String deliveryPlace){
        this.deliveryPlace = deliveryPlace;
    }

    public String getDeliveryAddrs(){
        return this.deliveryAddrs;
    }

    public void setDeliveryAddrs(String deliveryAddrs){
        this.deliveryAddrs = deliveryAddrs;
    }

    public Date getDeliveryDate(){
        return this.deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate){
        this.deliveryDate = deliveryDate;
    }

    public Date getEndDeliveryDate(){
        return this.endDeliveryDate;
    }

    public void setEndDeliveryDate(Date endDeliveryDate){
        this.endDeliveryDate = endDeliveryDate;
    }

    public List<PoItem> getItems() {
        return items;
    }

    public void setItems(List<PoItem> items) {
        this.items = items;
    }
}
