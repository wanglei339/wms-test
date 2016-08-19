package com.lsh.wms.model.shelve;

import com.lsh.base.common.utils.DateUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class AtticShelveTaskDetail implements Serializable {

	/**  */
    private Long id;
	/** 任务id */
    private Long taskId;
	/** 收货单id */
    private Long receiptId;
	/** 订单id */
    private Long orderId;
	/** 商品id */
    private Long skuId;
	/** 货主id */
    private Long ownerId;
	/** 批次id */
    private Long lotId;
	/** 供商id */
    private Long supplierId;
	/** 分配库位 */
    private Long allocLocationId = 0L;
	/** 实际库位 */
    private Long realLocationId = 0L;
	/** 容器id */
    private Long containerId;
	/** 上架人员id */
    private Long operator = 0L;
	/** 上架时间 */
    private Long shelveAt = DateUtils.getCurrentSeconds();
	/** 状态 1-已创建,2-上架完成 */
    private Long status = 1L;
	/** 数量*/
	private BigDecimal qty = BigDecimal.ZERO;
	/** 实际数量*/
	private BigDecimal realQty = BigDecimal.ZERO;
	/**  */
    private Long createdAt = DateUtils.getCurrentSeconds();
	/**  */
    private Long updatedAt = DateUtils.getCurrentSeconds();
	
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
	
	public Long getShelveAt(){
		return this.shelveAt;
	}
	
	public void setShelveAt(Long shelveAt){
		this.shelveAt = shelveAt;
	}
	
	public Long getStatus(){
		return this.status;
	}
	
	public void setStatus(Long status){
		this.status = status;
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

	public BigDecimal getQty() {
		return qty;
	}

	public void setQty(BigDecimal qty) {
		this.qty = qty;
	}

	public BigDecimal getRealQty() {
		return realQty;
	}

	public void setRealQty(BigDecimal realQty) {
		this.realQty = realQty;
	}
}
