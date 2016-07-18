package com.lsh.wms.model.baseinfo;

import java.io.Serializable;
import java.util.Date;

public class BaseinfoStaffJob implements Serializable {

	/**  */
    private Long id;
	/** 工种id */
    private Long jobId;
	/** 工种名称 */
    private String jobName;
	/** 1-正常 2-删除 */
    private Integer recordStatus;
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
	
	public Long getJobId(){
		return this.jobId;
	}
	
	public void setJobId(Long jobId){
		this.jobId = jobId;
	}
	
	public String getJobName(){
		return this.jobName;
	}
	
	public void setJobName(String jobName){
		this.jobName = jobName;
	}
	
	public Integer getRecordStatus(){
		return this.recordStatus;
	}
	
	public void setRecordStatus(Integer recordStatus){
		this.recordStatus = recordStatus;
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
