package com.lsh.wms.model.baseinfo;

import java.io.Serializable;
import java.util.Date;

public class ItemSkuRelation implements Serializable {

	/**  */
    private Long id;
	/**  */
    private Long itemId;
	/**  */
    private Long skuId;
	/**  */
    private Long ownerId;
	/** 是否有效 1 有效 2无效 */
    private Long isValid;
	
	public Long getId(){
		return this.id;
	}
	
	public void setId(Long id){
		this.id = id;
	}
	
	public Long getItemId(){
		return this.itemId;
	}
	
	public void setItemId(Long itemId){
		this.itemId = itemId;
	}
	
	public Long getSkuId(){
		return this.skuId;
	}
	
	public void setSkuId(Long skuId){
		this.skuId = skuId;
	}
	
	public Long getOwnerId(){
		return this.ownerId;
	}
	
	public void setOwnerId(Long ownerId){
		this.ownerId = ownerId;
	}
	
	public Long getIsValid(){
		return this.isValid;
	}
	
	public void setIsValid(Long isValid){
		this.isValid = isValid;
	}
	
	
}
