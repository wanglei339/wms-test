package com.lsh.wms.model.shelve;

import java.io.Serializable;
import java.util.Date;

public class PickTaskDetail implements Serializable {

	/**  */
    private Long id;
	/** 任务id */
    private Long taskId;
	/** 订单id */
    private Long orderId;
	/** 波次id */
    private Long waveId;
	/** 商品id */
    private Long skuId;
	/** 货主id */
    private Long ownerId;
	/** 批次id */
    private Long lotId;
	/** 供商id */
    private Long supplierId;
	/** 分配捡货量 */
    private BigDecimal allocQty;
	/** 实际捡货量 */
    private BigDecimal realQty;
	/** 分配拣货位 */
    private Long allocLocationId;
	/** 实际拣货位 */
    private Long realLocationId;
	/** 分配的集货位 */
    private Long allocCollectLocation;
	/** 拣货时间 */
    private Long pickAt;
	/**  */
    private Long createdAt;
	/**  */
    private Long updatedAt;
	
	public Long getId(){
		return this.id;
	}
	
	public void setId(Long id){
		this.id = id;
	}
	
	public Long getTaskId(){
		return this.taskId;
	}
	
	public void setTaskId(Long taskId){
		this.taskId = taskId;
	}
	
	public Long getOrderId(){
		return this.orderId;
	}
	
	public void setOrderId(Long orderId){
		this.orderId = orderId;
	}
	
	public Long getWaveId(){
		return this.waveId;
	}
	
	public void setWaveId(Long waveId){
		this.waveId = waveId;
	}
	
	public Long getSkuId(){
		return this.skuId;
	}
	
	public void setSkuId(Long skuId){
		this.skuId = skuId;
	}
	
	public Long getOwnerId(){
		return this.ownerId;
	}
	
	public void setOwnerId(Long ownerId){
		this.ownerId = ownerId;
	}
	
	public Long getLotId(){
		return this.lotId;
	}
	
	public void setLotId(Long lotId){
		this.lotId = lotId;
	}
	
	public Long getSupplierId(){
		return this.supplierId;
	}
	
	public void setSupplierId(Long supplierId){
		this.supplierId = supplierId;
	}
	
	public BigDecimal getAllocQty(){
		return this.allocQty;
	}
	
	public void setAllocQty(BigDecimal allocQty){
		this.allocQty = allocQty;
	}
	
	public BigDecimal getRealQty(){
		return this.realQty;
	}
	
	public void setRealQty(BigDecimal realQty){
		this.realQty = realQty;
	}
	
	public Long getAllocLocationId(){
		return this.allocLocationId;
	}
	
	public void setAllocLocationId(Long allocLocationId){
		this.allocLocationId = allocLocationId;
	}
	
	public Long getRealLocationId(){
		return this.realLocationId;
	}
	
	public void setRealLocationId(Long realLocationId){
		this.realLocationId = realLocationId;
	}
	
	public Long getAllocCollectLocation(){
		return this.allocCollectLocation;
	}
	
	public void setAllocCollectLocation(Long allocCollectLocation){
		this.allocCollectLocation = allocCollectLocation;
	}
	
	public Long getPickAt(){
		return this.pickAt;
	}
	
	public void setPickAt(Long pickAt){
		this.pickAt = pickAt;
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
	
	
}
