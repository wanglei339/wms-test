package com.lsh.wms.model.task;

import java.io.Serializable;
import java.util.Date;

public class Task implements Serializable {

	/**  */
    private Long id;
	/** 任务id */
    private Long taskId;
	/** 计划id */
    private Long planId;
	/** 当前LocationId, 分配查找使用 */
    private Long locationId;
	/** 商品id，分配查找用 */
    private Long skuId;
	/** 容器id，分配查找用 */
    private Long containerId;
	/** 任务类型，100-盘点， 101-拣货，102-上架，103-移库，104-补货, 105-QC */
    private Long type;
	/** 任务状态，1-draft, 2-waiting, 3-assigned, 4-allocated, 5-done, 6-cancel */
    private Long status;
	/** 优先级 */
    private Long priority;
	/** 操作人员id */
    private Long operator;
	/** 创建时间 */
    private Long draftTime;
	/** 分配时间 */
    private Long allocTime;
	/** 最晚完成时间 */
    private Long dueTime;
	/** 实际完成时间 */
    private Long finishTime;
	/** 取消时间 */
    private Long cancelTime;
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
	
	public Long getStatus(){
		return this.status;
	}
	
	public void setStatus(Long status){
		this.status = status;
	}
	
	public Long getPriority(){
		return this.priority;
	}
	
	public void setPriority(Long priority){
		this.priority = priority;
	}
	
	public Long getOperator(){
		return this.operator;
	}
	
	public void setOperator(Long operator){
		this.operator = operator;
	}
	
	public Long getDraftTime(){
		return this.draftTime;
	}
	
	public void setDraftTime(Long draftTime){
		this.draftTime = draftTime;
	}
	
	public Long getAllocTime(){
		return this.allocTime;
	}
	
	public void setAllocTime(Long allocTime){
		this.allocTime = allocTime;
	}
	
	public Long getDueTime(){
		return this.dueTime;
	}
	
	public void setDueTime(Long dueTime){
		this.dueTime = dueTime;
	}
	
	public Long getFinishTime(){
		return this.finishTime;
	}
	
	public void setFinishTime(Long finishTime){
		this.finishTime = finishTime;
	}
	
	public Long getCancelTime(){
		return this.cancelTime;
	}
	
	public void setCancelTime(Long cancelTime){
		this.cancelTime = cancelTime;
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
