package com.lsh.wms.model.baseinfo;

import java.io.Serializable;
import java.util.Date;

public class BaseinfoStaffJobRelation implements Serializable {

	/**  */
    private Long id;
	/** 员工id */
    private Long staffId;
	/** 工种id */
    private Long jobId;
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
	
	public Long getStaffId(){
		return this.staffId;
	}
	
	public void setStaffId(Long staffId){
		this.staffId = staffId;
	}
	
	public Long getJobId(){
		return this.jobId;
	}
	
	public void setJobId(Long jobId){
		this.jobId = jobId;
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
