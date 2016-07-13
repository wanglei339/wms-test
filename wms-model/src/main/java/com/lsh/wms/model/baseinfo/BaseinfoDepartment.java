package com.lsh.wms.model.baseinfo;

import java.io.Serializable;
import java.util.Date;

public class BaseinfoDepartment implements Serializable {

	/**  */
    private Long id;
	/** 部门id */
    private Long departmentId = 0L;
	/** 部门名称 */
    private String departmentName;
	/** 1-正常 2-异常 */
    private Integer status;
	/**  */
    private Long createdAt = 0L;
	/**  */
    private Long updatedAt = 0L;
	
	public Long getId(){
		return this.id;
	}
	
	public void setId(Long id){
		this.id = id;
	}
	
	public Long getDepartmentId(){
		return this.departmentId;
	}
	
	public void setDepartmentId(Long departmentId){
		this.departmentId = departmentId;
	}
	
	public String getDepartmentName(){
		return this.departmentName;
	}
	
	public void setDepartmentName(String departmentName){
		this.departmentName = departmentName;
	}
	
	public Integer getStatus(){
		return this.status;
	}
	
	public void setStatus(Integer status){
		this.status = status;
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
