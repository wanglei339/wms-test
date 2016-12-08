package com.lsh.wms.model.shelve;

import java.io.Serializable;
import java.util.Date;

public class ShelveTaskHead implements Serializable {

	/**  */
    private Long id;
	/** 任务id */
    private Long taskId = 0L;
	/** 收货单id */
    private Long receiptId = 0L;
	/** 订单id */
    private Long orderId = 0L;
	/** 商品id */
    private Long skuId = 0L;
	/** 货主id */
    private Long ownerId = 0L;
	/** 批次id */
    private Long lotId = 0L;
	/** 供商id */
    private Long supplierId = 0L;
	/** 分配库位 */
    private Long allocLocationId = 0L;
	/** 实际库位 */
    private Long realLocationId = 0L;
	/** 容器id */
    private Long containerId = 0L;
	/** 上架人员id */
    private Long operator = 0L;
	/** 已计算过的货架位 */
	private String calcLocationIds = "";
	/** 上架时间 */
    private Long shelveAt = 0L;
	/**  */
    private Long createdAt = 0L;
	/**  */
    private Long updatedAt = 0L;
	
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
	
	public Long getReceiptId(){
		return this.receiptId;
	}
	
	public void setReceiptId(Long receiptId){
		this.receiptId = receiptId;
	}
	
	public Long getOrderId(){
		return this.orderId;
	}
	
	public void setOrderId(Long orderId){
		this.orderId = orderId;
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
	
	public Long getContainerId(){
		return this.containerId;
	}
	
	public void setContainerId(Long containerId){
		this.containerId = containerId;
	}
	
	public Long getOperator(){
		return this.operator;
	}
	
	public void setOperator(Long operator){
		this.operator = operator;
	}

	public String getCalcLocationIds(){
		return this.calcLocationIds;
	}

	public void setCalcLocationIds(String calcLocationId){
		this.calcLocationIds = calcLocationId;
	}
	
	public Long getShelveAt(){
		return this.shelveAt;
	}
	
	public void setShelveAt(Long shelveAt){
		this.shelveAt = shelveAt;
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
