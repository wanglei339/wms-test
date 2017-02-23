package com.lsh.wms.model.stock;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class StockQuantLocationInfo implements Serializable {

	/**  */
    private Long id;
	/** 库位id */
    private Long locationId;
	/** 货主id */
    private Long ownerId;
	/** 商品id */
    private Long itemId;
	/** 货主商品编号 */
    private String skuCode;
	/** 标准码类型, 1 - 国条, 2 - ISBN */
    private Integer codeType;
	/** 标准唯一码 */
    private String code;
	/** 外包装单位-含有基本单位个数 */
    private BigDecimal packUnit;
	/** 外包装名称 */
    private String packName;
	/** 库存总数 */
    private BigDecimal qty;
	/** 保质期天数 */
    private BigDecimal shelfLife;
	/**  */
    private Long createdAt;
	/**  */
    private Long updatedAt;
	/** 最小剩余保质期百分比 */
    private double minLifeRet;
	/** 供商id */
    private Long supplierId;

	private double minLife;
	
	public Long getId(){
		return this.id;
	}
	
	public void setId(Long id){
		this.id = id;
	}
	
	public Long getLocationId(){
		return this.locationId;
	}
	
	public void setLocationId(Long locationId){
		this.locationId = locationId;
	}
	
	public Long getOwnerId(){
		return this.ownerId;
	}
	
	public void setOwnerId(Long ownerId){
		this.ownerId = ownerId;
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
	
	public Integer getCodeType(){
		return this.codeType;
	}
	
	public void setCodeType(Integer codeType){
		this.codeType = codeType;
	}
	
	public String getCode(){
		return this.code;
	}
	
	public void setCode(String code){
		this.code = code;
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
	
	public BigDecimal getQty(){
		return this.qty;
	}
	
	public void setQty(BigDecimal qty){
		this.qty = qty;
	}
	
	public BigDecimal getShelfLife(){
		return this.shelfLife;
	}
	
	public void setShelfLife(BigDecimal shelfLife){
		this.shelfLife = shelfLife;
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
	
	public double getMinLifeRet(){
		return this.minLifeRet;
	}
	
	public void setMinLifeRet(double minLifeRet){
		this.minLifeRet = minLifeRet;
	}
	
	public Long getSupplierId(){
		return this.supplierId;
	}
	
	public void setSupplierId(Long supplierId){
		this.supplierId = supplierId;
	}

	public double getMinLife() {
		return minLife;
	}

	public void setMinLife(double minLife) {
		this.minLife = minLife;
	}
}
