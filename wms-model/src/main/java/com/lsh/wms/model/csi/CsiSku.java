package com.lsh.wms.model.csi;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

public class CsiSku implements Serializable {

	/**  */
    private Long id;
	/** 仓库系统商品唯一码 */
    private Long skuId = 0L;
	/** 商品名称 */
    private String skuName;
	/** 标准码类型, 1 - 国条, 2 - ISBN */
    private String codeType;
	/** 标准唯一码 */
    private String code;
	/** 保质期天数 */
    private BigDecimal shelfLife;
	/** 基本单位-长 */
    private BigDecimal length;
	/** 基本单位-宽 */
    private BigDecimal width;
	/** 基本单位-高 */
    private BigDecimal height;
	/** 基本单位-重量 */
    private BigDecimal weight;
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
	
	public Long getSkuId(){
		return this.skuId;
	}
	
	public void setSkuId(Long skuId){
		this.skuId = skuId;
	}
	
	public String getSkuName(){
		return this.skuName;
	}
	
	public void setSkuName(String skuName){
		this.skuName = skuName;
	}
	
	public String getCodeType(){
		return this.codeType;
	}
	
	public void setCodeType(String codeType){
		this.codeType = codeType;
	}
	
	public String getCode(){
		return this.code;
	}
	
	public void setCode(String code){
		this.code = code;
	}
	
	public BigDecimal getShelfLife(){
		return this.shelfLife;
	}
	
	public void setShelfLife(BigDecimal shelfLife){
		this.shelfLife = shelfLife;
	}
	
	public BigDecimal getLength(){
		return this.length;
	}
	
	public void setLength(BigDecimal length){
		this.length = length;
	}
	
	public BigDecimal getWidth(){
		return this.width;
	}
	
	public void setWidth(BigDecimal width){
		this.width = width;
	}
	
	public BigDecimal getHeight(){
		return this.height;
	}
	
	public void setHeight(BigDecimal height){
		this.height = height;
	}
	
	public BigDecimal getWeight(){
		return this.weight;
	}
	
	public void setWeight(BigDecimal weight){
		this.weight = weight;
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
