package com.lsh.wms.model.baseinfo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class BaseinfoItemQuantRange implements Serializable {

	/**  */
    private Long id;
	/** 商品id */
    private Long itemId;
	/** 最小存货量 */
    private BigDecimal minQty;
	/** 最大存货量 */
    private BigDecimal maxQty;
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
	
	public Long getItemId(){
		return this.itemId;
	}
	
	public void setItemId(Long itemId){
		this.itemId = itemId;
	}
	
	public BigDecimal getMinQty(){
		return this.minQty;
	}
	
	public void setMinQty(BigDecimal minQty){
		this.minQty = minQty;
	}
	
	public BigDecimal getMaxQty(){
		return this.maxQty;
	}
	
	public void setMaxQty(BigDecimal maxQty){
		this.maxQty = maxQty;
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
