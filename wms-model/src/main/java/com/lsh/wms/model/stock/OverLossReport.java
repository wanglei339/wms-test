package com.lsh.wms.model.stock;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class OverLossReport implements Serializable {

	/**  */
    private Long id;
	/** 货主id*/
	private Long ownerId;
	/**  */
    private Long lossReportId;
	/**  */
    private Long itemId;
	/** 批次id */
    private Long lotId;
	/** 报损报溢任务id */
    private Long refTaskId;
	/** 库存地 */
    private String storageLocation =  "0001";
	/** 类型551报损552报溢 */
    private Long moveType;
	/** 移动原因 */
    private String moveReason = "0001";
	/** 物美码  */
    private String skuCode;
	/** 包装单位 */
    private String packName;
	/** 数量 */
    private BigDecimal qty;
	/** 是否有效 */
    private Long isValid = 1l;
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
	
	public Long getLossReportId(){
		return this.lossReportId;
	}
	
	public void setLossReportId(Long lossReportId){
		this.lossReportId = lossReportId;
	}
	
	public Long getItemId(){
		return this.itemId;
	}
	
	public void setItemId(Long itemId){
		this.itemId = itemId;
	}
	
	public Long getLotId(){
		return this.lotId;
	}
	
	public void setLotId(Long lotId){
		this.lotId = lotId;
	}
	
	public Long getRefTaskId(){
		return this.refTaskId;
	}
	
	public void setRefTaskId(Long refTaskId){
		this.refTaskId = refTaskId;
	}
	
	public String getStorageLocation(){
		return this.storageLocation;
	}
	
	public void setStorageLocation(String storageLocation){
		this.storageLocation = storageLocation;
	}
	
	public Long getMoveType(){
		return this.moveType;
	}
	
	public void setMoveType(Long moveType){
		this.moveType = moveType;
	}
	
	public String getMoveReason(){
		return this.moveReason;
	}
	
	public void setMoveReason(String moveReason){
		this.moveReason = moveReason;
	}
	
	public String getSkuCode(){
		return this.skuCode;
	}
	
	public void setSkuCode(String skuCode){
		this.skuCode = skuCode;
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
	
	public Long getIsValid(){
		return this.isValid;
	}
	
	public void setIsValid(Long isValid){
		this.isValid = isValid;
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

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
}
