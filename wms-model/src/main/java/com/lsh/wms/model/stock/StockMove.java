package com.lsh.wms.model.stock;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class StockMove implements Serializable {
	/**  */
	private Long id;
	/** 商品id */
	private Long skuId = 0L;
	/** onwerId */
	private Long ownerId = 0L;
	/** 商品id */
	private Long itemId= 0L;
	/** 起始库存位id */
	private Long fromLocationId = 0L;
	/** 目标库存位id */
	private Long toLocationId = 0L;
	/** 起始容器id */
	private Long fromContainerId = 0L;
	/** 目标容器id */
	private Long toContainerId = 0L;
	/** 计划数量 */
	private BigDecimal qty = BigDecimal.ZERO;
	/** 任务id */
	private Long taskId = 0L;
	/** 操作人员id */
	private Long operator = 0L;
	/** 任务状态，1-draft, 2-waiting, 3-assgined, 4-done, 5-can */
	private Long status = 0L;
	/** 移动类型，1-loc2loc，2-con2con，3-loc2con, 4-con2loc */
	private Long moveType = 0L;
	/**  */
	private Long createdAt;
	/**  */
	private Long updatedAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSkuId() {
		return skuId;
	}

	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public Long getFromLocationId() {
		return fromLocationId;
	}

	public void setFromLocationId(Long fromLocationId) {
		this.fromLocationId = fromLocationId;
	}

	public Long getToLocationId() {
		return toLocationId;
	}

	public void setToLocationId(Long toLocationId) {
		this.toLocationId = toLocationId;
	}

	public Long getFromContainerId() {
		return fromContainerId;
	}

	public void setFromContainerId(Long fromContainerId) {
		this.fromContainerId = fromContainerId;
	}

	public Long getToContainerId() {
		return toContainerId;
	}

	public void setToContainerId(Long toContainerId) {
		this.toContainerId = toContainerId;
	}

	public BigDecimal getQty() {
		return qty;
	}

	public void setQty(BigDecimal qty) {
		this.qty = qty;
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public Long getOperator() {
		return operator;
	}

	public void setOperator(Long operator) {
		this.operator = operator;
	}

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public Long getMoveType() {
		return moveType;
	}

	public void setMoveType(Long moveType) {
		this.moveType = moveType;
	}

	public Long getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Long createdAt) {
		this.createdAt = createdAt;
	}

	public Long getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Long updatedAt) {
		this.updatedAt = updatedAt;
	}
}
