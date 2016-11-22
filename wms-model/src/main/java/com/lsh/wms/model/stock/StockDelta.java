package com.lsh.wms.model.stock;

import com.lsh.base.common.utils.DateUtils;

import java.io.Serializable;
import java.math.BigDecimal;

public class StockDelta implements Serializable {

	/**  */
    private Long id;
	/** 内部商品ID */
    private Long itemId;
	/** 库存数量变化值 */
    private BigDecimal inhouseQty = BigDecimal.ZERO;
	/** 锁定数量变化值 */
    private BigDecimal allocQty = BigDecimal.ZERO;
	/** 引起库存变化的业务ID */
    private Long businessId;
	/** 库存变化类型 1-上架 2-在库SO下发 3-在库SO出库 4-转残次 5-转退货 */
    private Integer type;
	/**  */
    private Long createdAt = DateUtils.getCurrentSeconds();
	/**  */
    private Long updatedAt = DateUtils.getCurrentSeconds();
	
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
	
	public BigDecimal getInhouseQty(){
		return this.inhouseQty;
	}
	
	public void setInhouseQty(BigDecimal inhouseQty){
		this.inhouseQty = inhouseQty;
	}
	
	public BigDecimal getAllocQty(){
		return this.allocQty;
	}
	
	public void setAllocQty(BigDecimal allocQty){
		this.allocQty = allocQty;
	}
	
	public Long getBusinessId(){
		return this.businessId;
	}
	
	public void setBusinessId(Long businessId){
		this.businessId = businessId;
	}
	
	public Integer getType(){
		return this.type;
	}
	
	public void setType(Integer type){
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
