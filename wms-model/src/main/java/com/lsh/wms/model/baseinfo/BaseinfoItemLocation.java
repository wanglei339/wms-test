package com.lsh.wms.model.baseinfo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class BaseinfoItemLocation implements Serializable {

	/**  */
    private Long id;
//	/** 货品ID */
//    private Long skuId;
//	/** 货主ID */
//    private Long ownerId;
	/** 内部商品ID */
	private Long itemId;
	/** 拣货位ID */
    private Long pickLocationid;
	/** 使用状态 */
    private Integer userstatus;
	/** 拣货位类型*///// TODO: 16/11/2 add 
	private Integer pickLocationType;
	/** 最小存货量 */
	private BigDecimal minQty;
	/** 最大存货量 */
	private BigDecimal maxQty;

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
//
//	public Long getSkuId(){
//		return this.skuId;
//	}
//
//	public void setSkuId(Long skuId){
//		this.skuId = skuId;
//	}
//
//	public Long getOwnerId(){
//		return this.ownerId;
//	}
//
//	public void setOwnerId(Long ownerId){
//		this.ownerId = ownerId;
//	}
	
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


	public Integer getPickLocationType() {
		return pickLocationType;
	}

	public void setPickLocationType(Integer pickLocationType) {
		this.pickLocationType = pickLocationType;
	}

	public BigDecimal getMinQty() {
		return minQty;
	}

	public void setMinQty(BigDecimal minQty) {
		this.minQty = minQty;
	}

	public BigDecimal getMaxQty() {
		return maxQty;
	}

	public void setMaxQty(BigDecimal maxQty) {
		this.maxQty = maxQty;
	}
}
