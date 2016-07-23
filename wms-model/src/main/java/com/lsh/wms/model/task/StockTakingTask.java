package com.lsh.wms.model.task;

import com.lsh.base.common.utils.DateUtils;
import com.lsh.base.common.utils.RandomUtils;

import java.io.Serializable;
import java.util.Date;

public class StockTakingTask implements Serializable  {

	/**  */
    private Long id;
	/** 盘点任务id */
    private Long taskId;
	/** 盘点计划id */
    private Long takingId ;
	/** 发起人员 */
    private Long planner = 0L;
	/** 盘点人员 */
    private Long operator =0L;
	/** 第几轮盘点 */
    private Long round = 0L;
	/** 创建时间 */
    private Long draftTime = DateUtils.getCurrentSeconds();
	/** 要求结束时间 */
    private Long dueTime = 0L;
	/** 完成时间 */
    private Long doneTime = 0L;
	/** 当前LocationId, 分配查找使用 */
	private Long locationId = 0L;
	/** 商品id，分配查找用 */
	private Long skuId = 0L ;
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
	
	public Long getTakingId(){
		return this.takingId;
	}
	
	public void setTakingId(Long takingId){
		this.takingId = takingId;
	}
	
	public Long getPlanner(){
		return this.planner;
	}
	
	public void setPlanner(Long planner){
		this.planner = planner;
	}
	
	public Long getOperator(){
		return this.operator;
	}
	
	public void setOperator(Long operator){
		this.operator = operator;
	}
	
	public Long getRound(){
		return this.round;
	}
	
	public void setRound(Long round){
		this.round = round;
	}
	
	public Long getDraftTime(){
		return this.draftTime;
	}
	
	public void setDraftTime(Long draftTime){
		this.draftTime = draftTime;
	}
	
	public Long getDueTime(){
		return this.dueTime;
	}
	
	public void setDueTime(Long dueTime){
		this.dueTime = dueTime;
	}
	
	public Long getDoneTime(){
		return this.doneTime;
	}
	
	public void setDoneTime(Long doneTime){
		this.doneTime = doneTime;
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

	public Long getLocationId() {
		return locationId;
	}

	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}

	public Long getSkuId() {
		return skuId;
	}

	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}
}
