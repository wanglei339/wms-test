package com.lsh.wms.model.seed;

import java.io.Serializable;
import java.math.BigDecimal;

public class SeedingTaskHead implements Serializable {

	/**  */
    private Long id;
	/** 任务id */
    private Long taskId;
	/** 门店号 */
    private Long storeNo;
	/**  */
    private Long createdAt;
	/**  */
    private Long updatedAt;
	/** 所需数量 */
    private BigDecimal requireQty;
	/** 播种目标托盘id */
	private Long realContainerId = 0L;
	/** 订单箱规 */
	private BigDecimal packUnit;
	
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
	
	public Long getStoreNo(){
		return this.storeNo;
	}
	
	public void setStoreNo(Long storeNo){
		this.storeNo = storeNo;
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
	
	public BigDecimal getRequireQty(){
		return this.requireQty;
	}
	
	public void setRequireQty(BigDecimal requireQty){
		this.requireQty = requireQty;
	}

	public Long getRealContainerId() {
		return realContainerId;
	}

	public void setRealContainerId(Long realContainerId) {
		this.realContainerId = realContainerId;
	}

	public BigDecimal getPackUnit() {
		return packUnit;
	}

	public void setPackUnit(BigDecimal packUnit) {
		this.packUnit = packUnit;
	}
}
