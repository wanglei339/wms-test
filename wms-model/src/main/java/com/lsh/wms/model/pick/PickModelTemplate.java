package com.lsh.wms.model.pick;

import java.io.Serializable;
import java.util.Date;

public class PickModelTemplate implements Serializable {

	/**  */
    private Long id;
	/** 捡货模型模版id */
    private Long pickModelTemplateId;
	/** 捡货模型模版名称 */
    private String pickModelTemplateName;
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
	
	public Long getPickModelTemplateId(){
		return this.pickModelTemplateId;
	}
	
	public void setPickModelTemplateId(Long pickModelTemplateId){
		this.pickModelTemplateId = pickModelTemplateId;
	}
	
	public String getPickModelTemplateName(){
		return this.pickModelTemplateName;
	}
	
	public void setPickModelTemplateName(String pickModelTemplateName){
		this.pickModelTemplateName = pickModelTemplateName;
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
