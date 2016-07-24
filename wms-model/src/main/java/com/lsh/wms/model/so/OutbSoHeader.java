package com.lsh.wms.model.so;

import java.io.Serializable;
import java.util.Date;

public class OutbSoHeader implements Serializable {

	/**  */
    private Long id;
	/** 仓库ID */
    private Long warehouseId;
	/** SO订单ID */
    private Long orderId;
	/** 出库订单号 */
    private String orderOtherId;
	/** 下单用户 */
    private String orderUser;
	/** 货主 */
    private Long ownerUid;
	/** 订单类型 1进货单，2退货单 */
    private Integer orderType;
	/** 波次号 */
    private Long waveId;
	/** TMS线路 */
    private String transPlan;
	/** TMS顺序号 */
    private Integer waveIndex;
	/** 交货时间 */
    private Date transTime;
	/** 订单状态，0取消，1正常，2拣货，3QC,4出库 */
    private Integer orderStatus;
	/** 收货地址 */
    private String deliveryAddrs;
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
	
	public Long getOrderId(){
		return this.orderId;
	}
	
	public void setOrderId(Long orderId){
		this.orderId = orderId;
	}
	
	public String getOrderOtherId(){
		return this.orderOtherId;
	}
	
	public void setOrderOtherId(String orderOtherId){
		this.orderOtherId = orderOtherId;
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
