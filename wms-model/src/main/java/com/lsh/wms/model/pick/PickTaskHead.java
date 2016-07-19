package com.lsh.wms.model.pick;

import java.io.Serializable;
import java.util.Date;

public class PickTaskHead implements Serializable {

	/**  */
    private Long id;
	/** 波次id */
    private Long waveId;
	/** 任务id */
    private Long pickTaskId;
	/** 任务名称 */
    private String pickTaskName;
	
	public Long getId(){
		return this.id;
	}
	
	public void setId(Long id){
		this.id = id;
	}
	
	public Long getWaveId(){
		return this.waveId;
	}
	
	public void setWaveId(Long waveId){
		this.waveId = waveId;
	}
	
	public Long getPickTaskId(){
		return this.pickTaskId;
	}
	
	public void setPickTaskId(Long pickTaskId){
		this.pickTaskId = pickTaskId;
	}
	
	public String getPickTaskName(){
		return this.pickTaskName;
	}
	
	public void setPickTaskName(String pickTaskName){
		this.pickTaskName = pickTaskName;
	}
	
	
}
