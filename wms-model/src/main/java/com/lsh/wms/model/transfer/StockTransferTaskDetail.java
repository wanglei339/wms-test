package com.lsh.wms.model.transfer;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class StockTransferTaskDetail implements Serializable {

	/**  */
    private Long id;
	/** 移库任务id */
    private Long taskId = 0L;
	/** 移库计划id */
    private Long planId = 0L;
	/** 移库detail id */
    private Long detailId = 0L;
	/** 商品id */
    private Long skuId = 0L;
	/** 商品id */
    private Long itemId = 0L;
	/** 移出库位id */
    private Long fromLocationId = 0L;
	/** 移入库位id */
    private Long toLocationId = 0L;
	/** 数量 */
    private BigDecimal qty = BigDecimal.ZERO;
	/** 以包装单位计量的库移数量 */
    private BigDecimal uomQty = BigDecimal.ZERO;
	/** 移库操作单位*/
	private Long subType = 2L;
	/** 商品单位转换id */
	private String packName = "";
	/** 商品包装单位 */
	private BigDecimal packUnit = BigDecimal.ONE;
	/** 容器设备id */
	private Long containerId = 0L;
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
	
	public Long getTaskId(){
		return this.taskId;
	}
	
	public void setTaskId(Long taskId){
		this.taskId = taskId;
	}
	
	public Long getPlanId(){
		return this.planId;
	}
	
	public void setPlanId(Long planId){
		this.planId = planId;
	}
	
	public Long getDetailId(){
		return this.detailId;
	}
	
	public void setDetailId(Long detailId){
		this.detailId = detailId;
	}
	
	public Long getSkuId(){
		return this.skuId;
	}
	
	public void setSkuId(Long skuId){
		this.skuId = skuId;
	}
	
	public Long getItemId(){
		return this.itemId;
	}
	
	public void setItemId(Long itemId){
		this.itemId = itemId;
	}
	
	public Long getFromLocationId(){
		return this.fromLocationId;
	}
	
	public void setFromLocationId(Long fromLocationId){
		this.fromLocationId = fromLocationId;
	}
	
	public Long getToLocationId(){
		return this.toLocationId;
	}
	
	public void setToLocationId(Long toLocationId){
		this.toLocationId = toLocationId;
	}
	
	public BigDecimal getQty(){
		return this.qty;
	}
	
	public void setQty(BigDecimal qty){
		this.qty = qty;
	}
	
	public BigDecimal getUomQty(){
		return this.uomQty;
	}
	
	public void setUomQty(BigDecimal uomQty){
		this.uomQty = uomQty;
	}

	public Long getSubType() {
		return this.getSubType();
	}

	public void setSubType(Long subType) {
		this.subType = subType;
	}

	public String getPackName() {
		return this.packName;
	}

	public void setPackName(String packName) {
		this.packName = packName;
	}

	public BigDecimal getPackUnit() {
		return this.packUnit;
	}

	public void setPackUnit(BigDecimal packUnit) {
		this.packUnit = packUnit;
	}

	public Long getContainerId() {
		return this.containerId;
	}

	public void setContainerId(Long containerId) {
		this.containerId = containerId;
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
