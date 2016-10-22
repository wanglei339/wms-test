package com.lsh.wms.model.so;

import java.io.Serializable;
import java.util.Date;

public class ObdHeader implements Serializable {

	/**  */
	private Long id;
	/** so订单id */
	private Long orderId;
	/** 参考id */
	private String orderOtherRefId;
	/** 出库订单号 */
	private String orderOtherId;
	/** 售达方编码 */
	private String orderUserCode;
	/** 售达方名称 */
	private String orderUser;
	/** 送达方名称,直流订单表示门店名称 */
	private String deliveryName;
	/** 送达方编码,直流订单表示门店编码 */
	private String deliveryCode;
	/** 货主 */
	private Long ownerUid;
	/** 订单类型 1so单，2供商退货单 3调拨出库单 */
	private Integer orderType;
	/** 波次号 */
	private Long waveId;
	/** tms线路 */
	private String transPlan;
	/** tms顺序号 */
	private Integer waveIndex;
	/** 交货时间 */
	private Date transTime;
	/** 订单状态，1正常－新建，2-进入波次，3-捡货完成，4-qc完成, 5-发货完成 */
	private Integer orderStatus;
	/** 收货地址(送达地址) */
	private String deliveryAddrs;
	/** 退货 收货供商id*/
	private String supplierNo = "";
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

	public Long getOrderId(){
		return this.orderId;
	}

	public void setOrderId(Long orderId){
		this.orderId = orderId;
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

	public String getOrderUserCode(){
		return this.orderUserCode;
	}

	public void setOrderUserCode(String orderUserCode){
		this.orderUserCode = orderUserCode;
	}

	public String getOrderUser(){
		return this.orderUser;
	}

	public void setOrderUser(String orderUser){
		this.orderUser = orderUser;
	}

	public String getDeliveryName(){
		return this.deliveryName;
	}

	public void setDeliveryName(String deliveryName){
		this.deliveryName = deliveryName;
	}

	public String getDeliveryCode(){
		return this.deliveryCode;
	}

	public void setDeliveryCode(String deliveryCode){
		this.deliveryCode = deliveryCode;
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

	public Long getWaveId(){
		return this.waveId;
	}

	public void setWaveId(Long waveId){
		this.waveId = waveId;
	}

	public String getTransPlan(){
		return this.transPlan;
	}

	public void setTransPlan(String transPlan){
		this.transPlan = transPlan;
	}

	public Integer getWaveIndex(){
		return this.waveIndex;
	}

	public void setWaveIndex(Integer waveIndex){
		this.waveIndex = waveIndex;
	}

	public Date getTransTime(){
		return this.transTime;
	}

	public void setTransTime(Date transTime){
		this.transTime = transTime;
	}

	public Integer getOrderStatus(){
		return this.orderStatus;
	}

	public void setOrderStatus(Integer orderStatus){
		this.orderStatus = orderStatus;
	}

	public String getDeliveryAddrs(){
		return this.deliveryAddrs;
	}

	public void setDeliveryAddrs(String deliveryAddrs){
		this.deliveryAddrs = deliveryAddrs;
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

	public String getSupplierNo() {
		return supplierNo;
	}

	public void setSupplierNo(String supplierNo) {
		this.supplierNo = supplierNo;
	}
}
