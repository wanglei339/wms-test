package com.lsh.wms.model.taking;

import java.io.Serializable;
import java.math.BigDecimal;

public class StockTakingDetail implements Serializable {

	/**  */
    private Long id;
	/** 哪次盘点的明细 */
    private Long takingId;
	/** 任务Id */
	private Long taskId = 0L;
	/** 盘点行项目编号 */
    private Long detailId;
	/** 第几轮盘点的结果 */
    private Long round = 1L ;
	/** 实际值 */
    private BigDecimal realQty = BigDecimal.ZERO;
	/** 理论值 */
    private BigDecimal theoreticalQty = BigDecimal.ZERO;
	/** 容器设备id */
    private Long containerId = 0L;
	/** 批次号 */
    private Long lotId = 0L;
	/** 理论商品id */
    private Long skuId = 0L;
	/** 实际商品id */
    private Long realSkuId = 0L;
	/** 存储库位id */
    private Long locationId = 0L;
	/** 盘点人员 */
    private Long operator = 0L;
	/** */
	private Long itemId = 0L;
	/** */
	private Long realItemId = 0L;
	/**货主id */
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
	
	public Long getTakingId(){
		return this.takingId;
	}
	
	public void setTakingId(Long takingId){
		this.takingId = takingId;
	}
	
	public Long getDetailId(){
		return this.detailId;
	}
	
	public void setDetailId(Long detailId){
		this.detailId = detailId;
	}
	
	public Long getRound(){
		return this.round;
	}
	
	public void setRound(Long round){
		this.round = round;
	}
	
	public BigDecimal getRealQty(){
		return this.realQty;
	}
	
	public void setRealQty(BigDecimal realQty){
		this.realQty = realQty;
	}
	
	public BigDecimal getTheoreticalQty(){
		return this.theoreticalQty;
	}
	
	public void setTheoreticalQty(BigDecimal theoreticalQty){
		this.theoreticalQty = theoreticalQty;
	}
	
	public Long getContainerId(){
		return this.containerId;
	}
	
	public void setContainerId(Long containerId){
		this.containerId = containerId;
	}
	
	public Long getLotId(){
		return this.lotId;
	}
	
	public void setLotId(Long lotId){
		this.lotId = lotId;
	}
	
	public Long getSkuId(){
		return this.skuId;
	}
	
	public void setSkuId(Long skuId){
		this.skuId = skuId;
	}
	
	public Long getRealSkuId(){
		return this.realSkuId;
	}
	
	public void setRealSkuId(Long realSkuId){
		this.realSkuId = realSkuId;
	}
	
	public Long getLocationId(){
		return this.locationId;
	}
	
	public void setLocationId(Long locationId){
		this.locationId = locationId;
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

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public Long getRealItemId() {
		return realItemId;
	}

	public void setRealItemId(Long realItemId) {
		this.realItemId = realItemId;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
}
