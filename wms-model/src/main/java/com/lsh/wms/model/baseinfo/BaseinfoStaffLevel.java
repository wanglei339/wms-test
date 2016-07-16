package com.lsh.wms.model.baseinfo;

import java.io.Serializable;
import java.util.Date;

public class BaseinfoStaffLevel implements Serializable {

	/**  */
    private Long id;
	/** 职级id */
    private Long levelId;
	/** 职级名称 */
    private String levelName;
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
	
	public Long getLevelId(){
		return this.levelId;
	}
	
	public void setLevelId(Long levelId){
		this.levelId = levelId;
	}
	
	public String getLevelName(){
		return this.levelName;
	}
	
	public void setLevelName(String levelName){
		this.levelName = levelName;
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
