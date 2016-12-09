package com.lsh.wms.model.datareport;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class SkuMap implements Serializable {

	/**  */
    private Long id;
	/** 物美码  */
    private String skuCode;
	/** 移动平均价 */
    private BigDecimal movingAveragePrice;
	/** 来源系统 1 wumart */
    private Integer sourceSystem = 1;
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
	
	public String getSkuCode(){
		return this.skuCode;
	}
	
	public void setSkuCode(String skuCode){
		this.skuCode = skuCode;
	}
	
	public BigDecimal getMovingAveragePrice(){
		return this.movingAveragePrice;
	}
	
	public void setMovingAveragePrice(BigDecimal movingAveragePrice){
		this.movingAveragePrice = movingAveragePrice;
	}
	
	public Integer getSourceSystem(){
		return this.sourceSystem;
	}
	
	public void setSourceSystem(Integer sourceSystem){
		this.sourceSystem = sourceSystem;
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
