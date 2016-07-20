package com.lsh.wms.model.stock;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class StockMove implements Serializable {

	/**  */
    private Long id;
	/** 商品id */
    private Long skuId;
	/** 起始库存位id */
    private Long fromLocationId = 0L;
	/** 目标库存位id */
    private Long toLocationId = 0L;
	/** 真实起始库存位id */
	private Long realFromLocationId = 0L;
	/** 真实目标库存位id */
	private Long realToLocationId = 0L;
	/** 起始容器id */
    private Long fromContainerId = 0L;
	/** 目标容器id */
    private Long toContainerId = 0L;
	/** 计划数量 */
    private BigDecimal qty = BigDecimal.ZERO;
	/** 实际数量 */
	private BigDecimal qtyDone = BigDecimal.ZERO;
	/** 下游move id */
    private Long moveDestId = 0L;
	/** 反向move使用，记录对应的正向move */
    private Long oriMoveId = 0L;
	/** 任务id */
    private Long taskId = 0L;
	/** 任务行项目id */
    private Long seqNo = 0L;
	/** 操作人员id */
    private Long operator = 0L;
	/** 任务状态，1-draft, 2-waiting, 3-assgined, 4-done, 5-can */
    private Long status = 1L;
	/** 移动类型，1-loc2loc，2-con2con，3-loc2con, 4-con2loc */
    private Long moveType = 0L;
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
	
	public Long getSkuId(){
		return this.skuId;
	}
	
	public void setSkuId(Long skuId){
		this.skuId = skuId;
	}
	
	public Long getFromLocationId(){
		return this.fromLocationId;
	}
	
	public void setFromLocationId(Long fromLocationId){
		this.fromLocationId = fromLocationId;
	}
	
	public Long getToLocationId(){
		return this.toLocationId;
	}
	
	public void setToLocationId(Long toLocationId){
		this.toLocationId = toLocationId;
	}
	
	public Long getFromContainerId(){
		return this.fromContainerId;
	}
	
	public void setFromContainerId(Long fromContainerId){
		this.fromContainerId = fromContainerId;
	}
	
	public Long getToContainerId(){
		return this.toContainerId;
	}
	
	public void setToContainerId(Long toContainerId){
		this.toContainerId = toContainerId;
	}
	
	public BigDecimal getQty(){
		return this.qty;
	}
	
	public void setQty(BigDecimal qty){
		this.qty = qty;
	}

	public BigDecimal getQtyDone() {
		return this.qtyDone;
	}

	public void setQtyDone(BigDecimal qtyDone) {
		this.qtyDone = qtyDone;
	}

	public Long getMoveDestId(){
		return this.moveDestId;
	}
	
	public void setMoveDestId(Long moveDestId){
		this.moveDestId = moveDestId;
	}
	
	public Long getOriMoveId(){
		return this.oriMoveId;
	}
	
	public void setOriMoveId(Long oriMoveId){
		this.oriMoveId = oriMoveId;
	}
	
	public Long getTaskId(){
		return this.taskId;
	}
	
	public void setTaskId(Long taskId){
		this.taskId = taskId;
	}
	
	public Long getSeqNo(){
		return this.seqNo;
	}
	
	public void setSeqNo(Long seqNo){
		this.seqNo = seqNo;
	}
	public Long getOperator(){
		return this.operator;
	}
	
	public void setOperator(Long operator){
		this.operator = operator;
	}
	
	public Long getStatus(){
		return this.status;
	}
	
	public void setStatus(Long status){
		this.status = status;
	}
	
	public Long getMoveType(){
		return this.moveType;
	}
	
	public void setMoveType(Long moveType){
		this.moveType = moveType;
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

	public Long getRealToLocationId() {
		return realToLocationId;
	}

	public void setRealToLocationId(Long realToLocationId) {
		this.realToLocationId = realToLocationId;
	}

	public Long getRealFromLocationId() {
		return realFromLocationId;
	}

	public void setRealFromLocationId(Long realFromLocationId) {
		this.realFromLocationId = realFromLocationId;
	}
}
