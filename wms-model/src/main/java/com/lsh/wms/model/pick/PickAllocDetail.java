package com.lsh.wms.model.pick;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

public class PickAllocDetail implements Serializable {

	/**  */
    private Long id;
	/** 波次id */
    private Long waveId;
	/** 订单id */
    private Long orderId;
	/** 商品id */
    private Long skuId;
	/** 货主id */
    private Long ownerId;
	/** 批次id */
    private Long locId = 0L;
	/** 供商id */
    private Long supplierId = 0L;
	/** 订单需求量 */
    private BigDecimal reqQty = new BigDecimal("0.0000");
	/** 配货库存量 */
    private BigDecimal allocQty;
	/** 分配的捡货分区 */
    private Long pickZoneId;
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
	
	public Long getWaveId(){
		return this.waveId;
	}
	
	public void setWaveId(Long waveId){
		this.waveId = waveId;
	}
	
	public Long getOrderId(){
		return this.orderId;
	}
	
	public void setOrderId(Long orderId){
		this.orderId = orderId;
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
	
	public Long getLocId(){
		return this.locId;
	}
	
	public void setLocId(Long locId){
		this.locId = locId;
	}
	
	public Long getSupplierId(){
		return this.supplierId;
	}
	
	public void setSupplierId(Long supplierId){
		this.supplierId = supplierId;
	}
	
	public BigDecimal getReqQty(){
		return this.reqQty;
	}
	
	public void setReqQty(BigDecimal reqQty){
		this.reqQty = reqQty;
	}
	
	public BigDecimal getAllocQty(){
		return this.allocQty;
	}
	
	public void setAllocQty(BigDecimal allocQty){
		this.allocQty = allocQty;
	}
	
	public Long getPickZoneId(){
		return this.pickZoneId;
	}
	
	public void setPickZoneId(Long pickZoneId){
		this.pickZoneId = pickZoneId;
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
