package com.lsh.wms.model.so;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ObdDetail implements Serializable {

	/**  */
	private Long id;
	/** 上游细单id */
	private String detailOtherId;
	/** 订单id */
	private Long orderId;

	private Long itemId;

	private Long skuId;
	/** 外部商品id */
	private String skuCode;
	/** 商品名称 */
	private String skuName;
	/** 订货数 */
	private BigDecimal orderQty;
	/** 原始订货数 */
	private BigDecimal oriOrderQty;
	/** 基本单位数量 */
	private BigDecimal unitQty;
	/** 包装单位 */
	private BigDecimal packUnit;
	/** 包装名称 */
	private String packName;
	/** 基本单位名称 */
	private String unitName;
	/** 商品单价,未税 */
	private BigDecimal price;
	/** 批次号 */
	private String lotCode;
	/**  */
	private Long createdAt = 0l;
	/**  */
	private Long updatedAt = 0l;

	public Long getId(){
		return this.id;
	}

	public void setId(Long id){
		this.id = id;
	}

	public String getDetailOtherId(){
		return this.detailOtherId;
	}

	public void setDetailOtherId(String detailOtherId){
		this.detailOtherId = detailOtherId;
	}

	public Long getOrderId(){
		return this.orderId;
	}

	public void setOrderId(Long orderId){
		this.orderId = orderId;
	}

	public String getSkuCode(){
		return this.skuCode;
	}

	public void setSkuCode(String skuCode){
		this.skuCode = skuCode;
	}

	public String getSkuName(){
		return this.skuName;
	}

	public void setSkuName(String skuName){
		this.skuName = skuName;
	}

	public BigDecimal getOrderQty(){
		return this.orderQty;
	}

	public void setOrderQty(BigDecimal orderQty){
		this.orderQty = orderQty;
	}

	public BigDecimal getOriOrderQty() {
		return oriOrderQty;
	}

	public void setOriOrderQty(BigDecimal oriOrderQty) {
		this.oriOrderQty = oriOrderQty;
	}

	public BigDecimal getUnitQty(){
		return this.unitQty;
	}

	public void setUnitQty(BigDecimal unitQty){
		this.unitQty = unitQty;
	}

	public BigDecimal getPackUnit(){
		return this.packUnit;
	}

	public void setPackUnit(BigDecimal packUnit){
		this.packUnit = packUnit;
	}

	public String getPackName(){
		return this.packName;
	}

	public void setPackName(String packName){
		this.packName = packName;
	}

	public String getUnitName(){
		return this.unitName;
	}

	public void setUnitName(String unitName){
		this.unitName = unitName;
	}

	public BigDecimal getPrice(){
		return this.price;
	}

	public void setPrice(BigDecimal price){
		this.price = price;
	}

	public String getLotCode(){
		return this.lotCode;
	}

	public void setLotCode(String lotCode){
		this.lotCode = lotCode;
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

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public Long getSkuId() {
		return skuId;
	}

	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}
}
