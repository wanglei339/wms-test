package com.lsh.wms.model.baseinfo;

import java.io.Serializable;
import java.util.Date;

public class BaseinfoStaffGroup implements Serializable {

	/**  */
    private Long id;
	/** 组别id */
    private Long groupId;
	/** 组别名称 */
    private String groupName;
	/** 部门id */
    private Long departmentId;
	/** 1-正常 2-异常 */
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
	
	public Long getGroupId(){
		return this.groupId;
	}
	
	public void setGroupId(Long groupId){
		this.groupId = groupId;
	}
	
	public String getGroupName(){
		return this.groupName;
	}
	
	public void setGroupName(String groupName){
		this.groupName = groupName;
	}
	
	public Long getDepartmentId(){
		return this.departmentId;
	}
	
	public void setDepartmentId(Long departmentId){
		this.departmentId = departmentId;
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
