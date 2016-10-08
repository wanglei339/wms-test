package com.lsh.wms.model.po;

import java.io.Serializable;
import java.util.Date;

public class IbdHeader implements Serializable {

	/**  */
	private Long id;
	/** 参考单号 */
	private String orderOtherRefId;
	/**  */
	private String orderOtherId;
	/** 采购单号 */
	private Long orderId;
	/** 返仓客户 */
	private String orderUser;
	/** 货主 */
	private Long ownerUid;
	/** 1po单，2退货单，3调货单，4直流订单 */
	private Integer orderType;
	/** 供商编码 */
	private Long supplierCode;
	/** 供商名称 */
	private String supplierName;
	/** 订单日期 */
	private Date orderTime;
	/** 订单状态，0取消，1正常，2待投单,3,已投单，5已收货 */
	private Integer orderStatus;
	/** 直流子类型，区分大店、小店 */
	private String stockCode;
	/** 发货时间 */
	private Date deliveryDate;
	/** 截止收货时间 */
	private Date endDeliveryDate;
	/**  */
	private Long createdAt = 0l;
	/**  */
	private Long updatedAt = 0l;

	private Object orderDetails;

	public Long getId(){
		return this.id;
	}

	public void setId(Long id){
		this.id = id;
	}

	public String getOrderOtherRefId(){
		return this.orderOtherRefId;
	}

	public void setOrderOtherRefId(String orderOtherRefId){
		this.orderOtherRefId = orderOtherRefId;
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

	public Long getCreatedAt(){
		return this.createdAt;
	}

	public void setCreatedAt(Long createdAt){
		this.createdAt = createdAt;
	}

	public Long getUpdatedAt(){
		return this.updatedAt;
	}

	public void setUpdatedAt(Long updatedAt){
		this.updatedAt = updatedAt;
	}

	public Object getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(Object orderDetails) {
		this.orderDetails = orderDetails;
	}
}
