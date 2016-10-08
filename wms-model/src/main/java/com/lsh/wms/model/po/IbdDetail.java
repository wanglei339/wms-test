package com.lsh.wms.model.po;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class IbdDetail implements Serializable {

	/**  */
	private Long id;
	/** 上游细单id */
	private String detailOtherId;
	/** 订单id */
	private Long orderId;
	/** 物美码 */
	private String skuCode;
	/** 商品名称 */
	private String skuName;
	/** 进货数 */
	private BigDecimal orderQty;
	/** 包装名称 */
	private String packName;
	/** 包装单位 */
	private BigDecimal packUnit;
	/** 基本单位名称 */
	private String unitName;
	/** 基本单位数量 */
	private BigDecimal unitQty;
	/** 价格 */
	private BigDecimal price;
	/** 实际收货数 */
	private BigDecimal inboundQty;
	/** 批次号 */
	private String lotCode;
	/** 保质期例外收货 0校验保质期1不校验 */
	private Integer exceptionReceipt;
	/** 返仓单生成移库任务的taskid */
	private Long taskId;
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

	public String getPackName(){
		return this.packName;
	}

	public void setPackName(String packName){
		this.packName = packName;
	}

	public BigDecimal getPackUnit(){
		return this.packUnit;
	}

	public void setPackUnit(BigDecimal packUnit){
		this.packUnit = packUnit;
	}

	public String getUnitName(){
		return this.unitName;
	}

	public void setUnitName(String unitName){
		this.unitName = unitName;
	}

	public BigDecimal getUnitQty(){
		return this.unitQty;
	}

	public void setUnitQty(BigDecimal unitQty){
		this.unitQty = unitQty;
	}

	public BigDecimal getPrice(){
		return this.price;
	}

	public void setPrice(BigDecimal price){
		this.price = price;
	}

	public BigDecimal getInboundQty(){
		return this.inboundQty;
	}

	public void setInboundQty(BigDecimal inboundQty){
		this.inboundQty = inboundQty;
	}

	public String getLotCode(){
		return this.lotCode;
	}

	public void setLotCode(String lotCode){
		this.lotCode = lotCode;
	}

	public Integer getExceptionReceipt() {
		return exceptionReceipt;
	}

	public void setExceptionReceipt(Integer exceptionReceipt) {
		this.exceptionReceipt = exceptionReceipt;
	}

	public Long getTaskId(){
		return this.taskId;
	}

	public void setTaskId(Long taskId){
		this.taskId = taskId;
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
