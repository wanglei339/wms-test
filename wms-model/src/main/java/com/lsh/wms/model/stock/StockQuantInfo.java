package com.lsh.wms.model.stock;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class StockQuantInfo implements Serializable {

	/**  */
    private Long id;
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
    private BigDecimal allQty;
	/** so锁定库存*/
	private BigDecimal soCloseQty;
	/** 可用库存量 */
    private BigDecimal canUseQty;
	/** 残次库存量 */
    private BigDecimal defectQty;
	/** 退货库存量 */
    private BigDecimal returnQty;
	/** 保质期天数 */
    private BigDecimal shelfLife;
	/** 剩余保质期天数 */
    private String remainShelfLife;
	/** 最小剩余保质期百分比*/
	private double minLifeRet;
	/** 货主id*/
	private Long ownerId;

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
	
	public BigDecimal getAllQty(){
		return this.allQty;
	}
	
	public void setAllQty(BigDecimal allQty){
		this.allQty = allQty;
	}
	
	public BigDecimal getCanUseQty(){
		return this.canUseQty;
	}
	
	public void setCanUseQty(BigDecimal canUseQty){
		this.canUseQty = canUseQty;
	}
	
	public BigDecimal getDefectQty(){
		return this.defectQty;
	}
	
	public void setDefectQty(BigDecimal defectQty){
		this.defectQty = defectQty;
	}
	
	public BigDecimal getReturnQty(){
		return this.returnQty;
	}
	
	public void setReturnQty(BigDecimal returnQty){
		this.returnQty = returnQty;
	}
	
	public BigDecimal getShelfLife(){
		return this.shelfLife;
	}
	
	public void setShelfLife(BigDecimal shelfLife){
		this.shelfLife = shelfLife;
	}
	
	public String getRemainShelfLife(){
		return this.remainShelfLife;
	}
	
	public void setRemainShelfLife(String remainShelfLife){
		this.remainShelfLife = remainShelfLife;
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

	public BigDecimal getSoCloseQty() {
		return soCloseQty;
	}

	public void setSoCloseQty(BigDecimal soCloseQty) {
		this.soCloseQty = soCloseQty;
	}

	public double getMinLifeRet() {
		return minLifeRet;
	}

	public void setMinLifeRet(double minLifeRet) {
		this.minLifeRet = minLifeRet;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
}
