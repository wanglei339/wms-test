package com.lsh.wms.model.task;

import java.io.Serializable;
import java.util.Date;

public class TaskInfo implements Serializable {

	/**  */
    private Long id;
	/** 任务id */
    private Long taskId;
	/** 计划id */
    private Long planId;
	/** 当前LocationId, 分配查找使用 */
    private Long locationId = 0L;
	/** 商品id，分配查找用 */
    private Long skuId = 0L;
	/** 容器id，分配查找用 */
    private Long containerId = 0L;
	/** 任务类型，1-拣货，2-上架，3-盘点，4-移库，5-补货, 6-QC */
    private Long type = 0L;
	/** 操作员 */
    private Long operator = 0L;
	/** 任务状态，1-draft, 2-waiting, 3-assigned, 4-allocated, 5-done, 6-cancel */
    private Long status = 0L;
	/** 下一个任务id */
    private Long nextTaskId = 0L;
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
	
	public Long getPlanId(){
		return this.planId;
	}
	
	public void setPlanId(Long planId){
		this.planId = planId;
	}
	
	public Long getLocationId(){
		return this.locationId;
	}
	
	public void setLocationId(Long locationId){
		this.locationId = locationId;
	}
	
	public Long getSkuId(){
		return this.skuId;
	}
	
	public void setSkuId(Long skuId){
		this.skuId = skuId;
	}
	
	public Long getContainerId(){
		return this.containerId;
	}
	
	public void setContainerId(Long containerId){
		this.containerId = containerId;
	}
	
	public Long getType(){
		return this.type;
	}
	
	public void setType(Long type){
		this.type = type;
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
	
	public Long getNextTaskId(){
		return this.nextTaskId;
	}
	
	public void setNextTaskId(Long nextTaskId){
		this.nextTaskId = nextTaskId;
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
