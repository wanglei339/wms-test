package com.lsh.wms.model.key;

import java.io.Serializable;
import java.util.Date;

public class KeyLock implements Serializable {

	/**  */
    private Long id;
	/** 主键id */
    private String keyId;
	/** 键type */
    private Long type;
	/** 插入时间 */
    private Long createdAt;
	/**  */
    private Long updatedAt;
	
	public Long getId(){
		return this.id;
	}
	
	public void setId(Long id){
		this.id = id;
	}
	
	public String getKeyId(){
		return this.keyId;
	}
	
	public void setKeyId(String keyId){
		this.keyId = keyId;
	}
	
	public Long getType(){
		return this.type;
	}
	
	public void setType(Long type){
		this.type = type;
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
