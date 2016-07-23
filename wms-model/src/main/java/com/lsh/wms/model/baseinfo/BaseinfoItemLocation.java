package com.lsh.wms.model.baseinfo;

import java.io.Serializable;
import java.util.Date;

public class BaseinfoItemLocation implements Serializable {

	/**  */
    private Long id;
	/** 货品ID */
    private Long skuId;
	/** 货主ID */
    private Long ownerId;
	/** 内部商品ID */
	private Long itemId;
	/** 拣货位ID */
    private Long pickLocationid;
	/** 使用状态 */
    private Integer userstatus;

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public Long getId(){
		return this.id;
	}
	
	public void setId(Long id){
		this.id = id;
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
	
	public Long getPickLocationid(){
		return this.pickLocationid;
	}
	
	public void setPickLocationid(Long pickLocationid){
		this.pickLocationid = pickLocationid;
	}
	
	public Integer getUserstatus(){
		return this.userstatus;
	}
	
	public void setUserstatus(Integer userstatus){
		this.userstatus = userstatus;
	}
	
	
}
