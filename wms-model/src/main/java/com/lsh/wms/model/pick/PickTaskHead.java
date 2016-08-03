package com.lsh.wms.model.pick;

import java.io.Serializable;
import java.util.Date;

public class PickTaskHead implements Serializable {

	/**  */
    private Long id;
	/** 任务id */
    private Long taskId;
	/** 订单id */
    private Long deliveryId;
	/** 波次id */
    private Long waveId;
	/** 线路id */
    private Long waybillId;
	/** 拣货任务类型，1-摘果，2-播种，3-边摘边播 */
    private Integer pickType;
	/** 容器id */
    private Long containerId;
	/** 拣货完成时间 */
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
	
	public Long getDeliveryId(){
		return this.deliveryId;
	}
	
	public void setDeliveryId(Long deliveryId){
		this.deliveryId = deliveryId;
	}
	
	public Long getWaveId(){
		return this.waveId;
	}
	
	public void setWaveId(Long waveId){
		this.waveId = waveId;
	}
	
	public Long getWaybillId(){
		return this.waybillId;
	}
	
	public void setWaybillId(Long waybillId){
		this.waybillId = waybillId;
	}
	
	public Integer getPickType(){
		return this.pickType;
	}
	
	public void setPickType(Integer pickType){
		this.pickType = pickType;
	}
	
	public Long getContainerId(){
		return this.containerId;
	}
	
	public void setContainerId(Long containerId){
		this.containerId = containerId;
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
