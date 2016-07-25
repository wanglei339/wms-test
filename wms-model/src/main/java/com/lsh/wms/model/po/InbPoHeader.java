package com.lsh.wms.model.po;

import java.io.Serializable;
import java.util.Date;

public class InbPoHeader implements Serializable {

	/**  */
    private Long id;
	/** 仓库ID */
    private Long warehouseId;
	/**采购订单号  */
    private String orderOtherId;
	/** 入库订单单号 */
    private Long orderId;
	/** 采购组 */
    private String orderUser;
	/** 货主 */
    private Long ownerUid;
	/** 1收货单，2退货单，3调货单 */
    private Integer orderType;
	/** 供商编码 */
    private Long supplierCode;
	/** 供商名称 */
    private String supplierName;
	/** 商品凭证号 */
    private String skuVoucherNo;
	/** 供商电话 */
    private String supplierPhone;
	/** 供商传真 */
    private String supplierFax;
	/** 订单日期 */
    private Date orderTime;
	/** 订单状态，0取消，1正常，2已发货,3,已投单 4，部分收货，5已收货 */
    private Integer orderStatus;
	/** 库存地 */
    private String stockCode;
	/** 收货地点 */
    private String deliveryPlace;
	/** 收货地址 */
    private String deliveryAddrs;
	/** 发货时间 */
    private Date deliveryDate;
	/** 截止收货时间 */
    private Date endDeliveryDate;
	/**  */
    private String insertby;
	/**  */
    private String updateby;
	/**  */
    private Date inserttime;
	/**  */
    private Date updatetime;

	private Object orderDetails;
	
	public Long getId(){
		return this.id;
	}
	
	public void setId(Long id){
		this.id = id;
	}
	
	public Long getWarehouseId(){
		return this.warehouseId;
	}
	
	public void setWarehouseId(Long warehouseId){
		this.warehouseId = warehouseId;
	}
	
	public String getOrderOtherId(){
		return this.orderOtherId;
	}
	
	public void setOrderOtherId(String orderOtherId){
		this.orderOtherId = orderOtherId;
	}
	
	public Long getOrderId(){
		return this.orderId;
	}
	
	public void setOrderId(Long orderId){
		this.orderId = orderId;
	}
	
	public String getOrderUser(){
		return this.orderUser;
	}
	
	public void setOrderUser(String orderUser){
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
	
	public Integer getOrderStatus(){
		return this.orderStatus;
	}
	
	public void setOrderStatus(Integer orderStatus){
		this.orderStatus = orderStatus;
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
	
	public String getInsertby(){
		return this.insertby;
	}
	
	public void setInsertby(String insertby){
		this.insertby = insertby;
	}
	
	public String getUpdateby(){
		return this.updateby;
	}
	
	public void setUpdateby(String updateby){
		this.updateby = updateby;
	}
	
	public Date getInserttime(){
		return this.inserttime;
	}
	
	public void setInserttime(Date inserttime){
		this.inserttime = inserttime;
	}
	
	public Date getUpdatetime(){
		return this.updatetime;
	}
	
	public void setUpdatetime(Date updatetime){
		this.updatetime = updatetime;
	}

	public Object getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(Object orderDetails) {
		this.orderDetails = orderDetails;
	}
}
