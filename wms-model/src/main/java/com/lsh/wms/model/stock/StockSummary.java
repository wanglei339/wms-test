package com.lsh.wms.model.stock;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class StockSummary implements Serializable {

	/**  */
    private Long id;
	/** 内部商品ID */
    private Long itemId;
	/** 货主商品编号 */
    private String skuCode;
	/** 货主id */
    private Long ownerId;
	/** 在库商品数量 */
    private BigDecimal inhouseQty;
	/** so锁定库存数量 */
    private BigDecimal allocQty;
	/** 实际可用库存数量 */
    private BigDecimal availQty;
	/** 预售库存（直流) */
    private BigDecimal presaleQty;
	/** 盘亏区库存 */
    private BigDecimal inventoryLossQty;
	/** 货架区库存 */
    private BigDecimal shelfQty;
	/** 拆零区库存 */
    private BigDecimal splitQty;
	/** 阁楼区库存 */
    private BigDecimal atticQty;
	/** 地堆区库存 */
    private BigDecimal floorQty;
	/** 暂存区库存 */
    private BigDecimal temporaryQty;
	/** 集货区库存 */
    private BigDecimal collectionQty;
	/** 退货区库存 */
    private BigDecimal backQty;
	/** 残次区库存 */
    private BigDecimal defectQty;
	/** 码头区库存 */
    private BigDecimal dockQty;
	/** 返仓区库存（优供退货） */
    private BigDecimal marketReturnQty;
	/** 播种区库存 */
    private BigDecimal sowQty;
	/** 直流供商退货区库存 */
    private BigDecimal supplierReturnQty;
	/** 差异区库存 */
    private BigDecimal diffQty;
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
	
	public Long getOwnerId(){
		return this.ownerId;
	}
	
	public void setOwnerId(Long ownerId){
		this.ownerId = ownerId;
	}
	
	public BigDecimal getInhouseQty(){
		return this.inhouseQty;
	}
	
	public void setInhouseQty(BigDecimal inhouseQty){
		this.inhouseQty = inhouseQty;
	}
	
	public BigDecimal getAllocQty(){
		return this.allocQty;
	}
	
	public void setAllocQty(BigDecimal allocQty){
		this.allocQty = allocQty;
	}
	
	public BigDecimal getAvailQty(){
		return this.availQty;
	}
	
	public void setAvailQty(BigDecimal availQty){
		this.availQty = availQty;
	}
	
	public BigDecimal getPresaleQty(){
		return this.presaleQty;
	}
	
	public void setPresaleQty(BigDecimal presaleQty){
		this.presaleQty = presaleQty;
	}
	
	public BigDecimal getInventoryLossQty(){
		return this.inventoryLossQty;
	}
	
	public void setInventoryLossQty(BigDecimal inventoryLossQty){
		this.inventoryLossQty = inventoryLossQty;
	}
	
	public BigDecimal getShelfQty(){
		return this.shelfQty;
	}
	
	public void setShelfQty(BigDecimal shelfQty){
		this.shelfQty = shelfQty;
	}
	
	public BigDecimal getSplitQty(){
		return this.splitQty;
	}
	
	public void setSplitQty(BigDecimal splitQty){
		this.splitQty = splitQty;
	}
	
	public BigDecimal getAtticQty(){
		return this.atticQty;
	}
	
	public void setAtticQty(BigDecimal atticQty){
		this.atticQty = atticQty;
	}
	
	public BigDecimal getFloorQty(){
		return this.floorQty;
	}
	
	public void setFloorQty(BigDecimal floorQty){
		this.floorQty = floorQty;
	}
	
	public BigDecimal getTemporaryQty(){
		return this.temporaryQty;
	}
	
	public void setTemporaryQty(BigDecimal temporaryQty){
		this.temporaryQty = temporaryQty;
	}
	
	public BigDecimal getCollectionQty(){
		return this.collectionQty;
	}
	
	public void setCollectionQty(BigDecimal collectionQty){
		this.collectionQty = collectionQty;
	}
	
	public BigDecimal getBackQty(){
		return this.backQty;
	}
	
	public void setBackQty(BigDecimal backQty){
		this.backQty = backQty;
	}
	
	public BigDecimal getDefectQty(){
		return this.defectQty;
	}
	
	public void setDefectQty(BigDecimal defectQty){
		this.defectQty = defectQty;
	}
	
	public BigDecimal getDockQty(){
		return this.dockQty;
	}
	
	public void setDockQty(BigDecimal dockQty){
		this.dockQty = dockQty;
	}
	
	public BigDecimal getMarketReturnQty(){
		return this.marketReturnQty;
	}
	
	public void setMarketReturnQty(BigDecimal marketReturnQty){
		this.marketReturnQty = marketReturnQty;
	}
	
	public BigDecimal getSowQty(){
		return this.sowQty;
	}
	
	public void setSowQty(BigDecimal sowQty){
		this.sowQty = sowQty;
	}
	
	public BigDecimal getSupplierReturnQty(){
		return this.supplierReturnQty;
	}
	
	public void setSupplierReturnQty(BigDecimal supplierReturnQty){
		this.supplierReturnQty = supplierReturnQty;
	}
	
	public BigDecimal getDiffQty(){
		return this.diffQty;
	}
	
	public void setDiffQty(BigDecimal diffQty){
		this.diffQty = diffQty;
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
