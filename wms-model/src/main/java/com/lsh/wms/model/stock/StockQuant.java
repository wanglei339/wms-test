package com.lsh.wms.model.stock;

import java.io.Serializable;
import java.math.BigDecimal;

public class StockQuant implements Serializable {

	/**  */
    private String id;
	/** 商品ID */
    private Long skuId;
	/** 存储库位ID */
    private Long locationId;
	/** 容器设备ID */
    private Long containerId;
	/** 数量 */
    private BigDecimal qty;
	/** 商品单位转换ID */
    private Long skuUomId;
	/** 库存价值 */
    private BigDecimal value;
	/** 库存成本 */
    private BigDecimal cost;
	/** 占位STOCK_MOVE ID */
    private Long reserveMoveId;
	/** 0-可用，1-被冻结 */
    private Long isFrozen;
	/** 货物供应商ID */
    private Long supplierId;
	/** 货物所属公司ID */
    private Long ownerId;
	/** 批次号 */
    private Long lotId;
	/** 入库时间 */
    private String inDate;
	/** 保质期失效时间 */
    private String expireDate;
	/**  */
    private String createdAt;
	/**  */
    private String updatedAt;
	
	public String getId(){
		return this.id;
	}
	
	public void setId(String id){
		this.id = id;
	}
	
	public Long getSkuId(){
		return this.skuId;
	}
	
	public void setSkuId(Long skuId){
		this.skuId = skuId;
	}
	
	public Long getLocationId(){
		return this.locationId;
	}
	
	public void setLocationId(Long locationId){
		this.locationId = locationId;
	}
	
	public Long getContainerId(){
		return this.containerId;
	}
	
	public void setContainerId(Long containerId){
		this.containerId = containerId;
	}
	
	public BigDecimal getQty(){
		return this.qty;
	}
	
	public void setQty(BigDecimal qty){
		this.qty = qty;
	}
	
	public Long getSkuUomId(){
		return this.skuUomId;
	}
	
	public void setSkuUomId(Long skuUomId){
		this.skuUomId = skuUomId;
	}
	
	public BigDecimal getValue(){
		return this.value;
	}
	
	public void setValue(BigDecimal value){
		this.value = value;
	}
	
	public BigDecimal getCost(){
		return this.cost;
	}
	
	public void setCost(BigDecimal cost){
		this.cost = cost;
	}
	
	public Long getReserveMoveId(){
		return this.reserveMoveId;
	}
	
	public void setReserveMoveId(Long reserveMoveId){
		this.reserveMoveId = reserveMoveId;
	}
	
	public Long getIsFrozen(){
		return this.isFrozen;
	}
	
	public void setIsFrozen(Long isFrozen){
		this.isFrozen = isFrozen;
	}
	
	public Long getSupplierId(){
		return this.supplierId;
	}
	
	public void setSupplierId(Long supplierId){
		this.supplierId = supplierId;
	}
	
	public Long getOwnerId(){
		return this.ownerId;
	}
	
	public void setOwnerId(Long ownerId){
		this.ownerId = ownerId;
	}
	
	public Long getLotId(){
		return this.lotId;
	}
	
	public void setLotId(Long lotId){
		this.lotId = lotId;
	}
	
	public String getInDate(){
		return this.inDate;
	}
	
	public void setInDate(String inDate){
		this.inDate = inDate;
	}
	
	public String getExpireDate(){
		return this.expireDate;
	}
	
	public void setExpireDate(String expireDate){
		this.expireDate = expireDate;
	}
	
	public String getCreatedAt(){
		return this.createdAt;
	}
	
	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}
	
	public String getUpdatedAt(){
		return this.updatedAt;
	}
	
	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}
	
	
}
