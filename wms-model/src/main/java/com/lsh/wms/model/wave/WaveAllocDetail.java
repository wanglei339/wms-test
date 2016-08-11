package com.lsh.wms.model.wave;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

public class WaveAllocDetail implements Serializable {

	/**  */
    private Long id;
	/** 波次id */
    private Long waveId;
	/** 订单id */
    private Long orderId;
	/** 商品码 */
	private Long itemId;
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
	/** 分配库存单位名称 */
	private String allocUnitName = "EA";
	/** 分配库存单位数量 */
	private BigDecimal allocUnitQty = new BigDecimal("0.0000");
	/** 分配的捡货分区 */
    private Long pickZoneId;
	/** 分配分拣位 */
	private Long allocPickLocation = 0L;
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

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
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

	public BigDecimal getAllocUnitQty(){
		return this.allocUnitQty;
	}

	public void setAllocUnitQty(BigDecimal allocUnitQty){
		this.allocUnitQty = allocUnitQty;
	}

	public String getAllocUnitName(){
		return this.allocUnitName;
	}

	public void setAllocUnitName(String allocUnitName){
		this.allocUnitName = allocUnitName;
	}
	
	public Long getPickZoneId(){
		return this.pickZoneId;
	}
	
	public void setPickZoneId(Long pickZoneId){
		this.pickZoneId = pickZoneId;
	}

	public Long getAllocPickLocation(){
		return this.allocPickLocation;
	}

	public void setAllocPickLocation(Long allocPickLocation){
		this.allocPickLocation = allocPickLocation;
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
