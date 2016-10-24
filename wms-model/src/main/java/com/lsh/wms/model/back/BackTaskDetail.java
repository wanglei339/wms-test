package com.lsh.wms.model.back;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class BackTaskDetail implements Serializable {

	/**  */
    private Long id;
	/** 任务id */
    private Long taskId;
	/** 商品id */
    private Long skuId;
	/**  */
    private Long createdAt;
	/**  */
    private Long updatedAt;
	/** 退货入库实际数量 */
    private BigDecimal realQty = BigDecimal.ZERO;
	/** 订单箱规 */
	private BigDecimal packUnit;
	/** 商品名称*/
	private String skuName;
	/** 国码*/
	private String barcode;
	/** 订单箱规名字*/
	private String packName;
	/** 退货入库数量*/
	private BigDecimal qty;


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
	
	public Long getSkuId(){
		return this.skuId;
	}
	
	public void setSkuId(Long skuId){
		this.skuId = skuId;
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
	
	public BigDecimal getRealQty(){
		return this.realQty;
	}
	
	public void setRealQty(BigDecimal realQty){
		this.realQty = realQty;
	}

	public BigDecimal getPackUnit() {
		return packUnit;
	}

	public void setPackUnit(BigDecimal packUnit) {
		this.packUnit = packUnit;
	}

	public String getSkuName() {
		return skuName;
	}

	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getPackName() {
		return packName;
	}

	public void setPackName(String packName) {
		this.packName = packName;
	}

	public BigDecimal getQty() {
		return qty;
	}

	public void setQty(BigDecimal qty) {
		this.qty = qty;
	}
}
