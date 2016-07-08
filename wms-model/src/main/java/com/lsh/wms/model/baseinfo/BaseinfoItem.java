package com.lsh.wms.model.baseinfo;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

public class BaseinfoItem implements Serializable {

	/**  */
    private Long id;
	/** 商品id */
    private Long skuId;
	/** 货主id */
    private Long ownerId;
	/** 货主商品编号 */
    private String skuCode;
	/** 商品名称 */
    private String skuName;
	/** 标准码类型, 1 - 国条, 2 - ISBN */
    private String codeType;
	/** 标准唯一码 */
    private String code;
	/** 1级品类id */
    private Long topCat;
	/** 2级品类id */
    private Long secondCat;
	/** 3级品类id */
    private Long thirdCat;
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
	/** 2级包装－长 */
    private BigDecimal l2Length;
	/** 2级包装－宽 */
    private BigDecimal l2Width;
	/** 2级包装－高 */
    private BigDecimal l2Height;
	/** 2级包装－重量 */
    private BigDecimal l2Weight;
	/** 外包装－长 */
    private BigDecimal packLength;
	/** 外包装－宽 */
    private BigDecimal packWidth;
	/** 外包装－高 */
    private BigDecimal packHeight;
	/** 外包装－重量 */
    private BigDecimal packWeight;
	/** 售卖单位 */
    private BigDecimal saleUnit;
	/** 商品等级，对应A,B,C */
    private String itemLevel;
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
	
	public Long getTopCat(){
		return this.topCat;
	}
	
	public void setTopCat(Long topCat){
		this.topCat = topCat;
	}
	
	public Long getSecondCat(){
		return this.secondCat;
	}
	
	public void setSecondCat(Long secondCat){
		this.secondCat = secondCat;
	}
	
	public Long getThirdCat(){
		return this.thirdCat;
	}
	
	public void setThirdCat(Long thirdCat){
		this.thirdCat = thirdCat;
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
	
	public BigDecimal getL2Length(){
		return this.l2Length;
	}
	
	public void setL2Length(BigDecimal l2Length){
		this.l2Length = l2Length;
	}
	
	public BigDecimal getL2Width(){
		return this.l2Width;
	}
	
	public void setL2Width(BigDecimal l2Width){
		this.l2Width = l2Width;
	}
	
	public BigDecimal getL2Height(){
		return this.l2Height;
	}
	
	public void setL2Height(BigDecimal l2Height){
		this.l2Height = l2Height;
	}
	
	public BigDecimal getL2Weight(){
		return this.l2Weight;
	}
	
	public void setL2Weight(BigDecimal l2Weight){
		this.l2Weight = l2Weight;
	}
	
	public BigDecimal getPackLength(){
		return this.packLength;
	}
	
	public void setPackLength(BigDecimal packLength){
		this.packLength = packLength;
	}
	
	public BigDecimal getPackWidth(){
		return this.packWidth;
	}
	
	public void setPackWidth(BigDecimal packWidth){
		this.packWidth = packWidth;
	}
	
	public BigDecimal getPackHeight(){
		return this.packHeight;
	}
	
	public void setPackHeight(BigDecimal packHeight){
		this.packHeight = packHeight;
	}
	
	public BigDecimal getPackWeight(){
		return this.packWeight;
	}
	
	public void setPackWeight(BigDecimal packWeight){
		this.packWeight = packWeight;
	}
	
	public BigDecimal getSaleUnit(){
		return this.saleUnit;
	}
	
	public void setSaleUnit(BigDecimal saleUnit){
		this.saleUnit = saleUnit;
	}
	
	public String getItemLevel(){
		return this.itemLevel;
	}
	
	public void setItemLevel(String itemLevel){
		this.itemLevel = itemLevel;
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
