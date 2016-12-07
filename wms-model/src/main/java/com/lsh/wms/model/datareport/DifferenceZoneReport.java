package com.lsh.wms.model.datareport;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class DifferenceZoneReport implements Serializable {

	/**  */
    private Long id;
	/**  */
    private Long differenceId;
	/** 商品Id */
    private Long itemId;
	/** 商品编码 */
    private String skuCode;
	/** 来源location */
    private Long fromLocationId;
	/** 来源类型 1 拣货 2 QC 3 补货 */
    private Integer sourceType;
	/** 基本单位名称 EA */
    private String unitName;
	/** 数量 */
    private BigDecimal qty;
	/** 操作人 */
    private Long operator;
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
	
	public Long getDifferenceId(){
		return this.differenceId;
	}
	
	public void setDifferenceId(Long differenceId){
		this.differenceId = differenceId;
	}
	
	public Long getItemId(){
		return this.itemId;
	}
	
	public void setItemId(Long itemId){
		this.itemId = itemId;
	}
	
	public String getSkuCode(){
		return this.skuCode;
	}
	
	public void setSkuCode(String skuCode){
		this.skuCode = skuCode;
	}
	
	public Long getFromLocationId(){
		return this.fromLocationId;
	}
	
	public void setFromLocationId(Long fromLocationId){
		this.fromLocationId = fromLocationId;
	}
	
	public Integer getSourceType(){
		return this.sourceType;
	}
	
	public void setSourceType(Integer sourceType){
		this.sourceType = sourceType;
	}
	
	public String getUnitName(){
		return this.unitName;
	}
	
	public void setUnitName(String unitName){
		this.unitName = unitName;
	}
	
	public BigDecimal getQty(){
		return this.qty;
	}
	
	public void setQty(BigDecimal qty){
		this.qty = qty;
	}
	
	public Long getOperator(){
		return this.operator;
	}
	
	public void setOperator(Long operator){
		this.operator = operator;
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
