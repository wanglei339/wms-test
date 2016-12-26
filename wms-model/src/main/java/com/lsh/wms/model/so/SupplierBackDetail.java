package com.lsh.wms.model.so;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class SupplierBackDetail implements Serializable {

	/**  */
    private Long id;
	/** 退货id */
    private Long backId;
	/** 退货订单id，对应obd_header的order_id */
    private Long orderId;
	/** 上游细单id,对应obd_detail的detail_other_id */
    private String detailOtherId;
	/** 商品码 */
    private Long itemId;
	/** 商品id */
    private Long skuId;
	/** 商品编码 一个商品编码可以对应多个国条码 */
    private String skuCode;
	/** 货主id */
    private Long ownerId;
	/** 批次id */
    private Long lotId;
	/** 0-新建，1-完成 */
    private Long status;
	/** 是否有效，1有效  0无效 */
    private Long isValid;
	/** 需求量 */
    private BigDecimal reqQty;
	/** 库存量 */
    private BigDecimal allocQty;
	/** 分配库存单位名称 */
    private String allocUnitName;
	/** 分配库存单位数量 */
    private BigDecimal allocUnitQty;
	/** 存储位 */
    private Long locationId;
	/** 虚拟容器id */
    private Long containerId;
	/** 实际容器id */
	private Long realContainerId;
	/** 操作员id */
    private Long operator;
	/** 完成时间 */
    private Long doneAt;
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
	
	public Long getBackId(){
		return this.backId;
	}
	
	public void setBackId(Long backId){
		this.backId = backId;
	}
	
	public Long getOrderId(){
		return this.orderId;
	}
	
	public void setOrderId(Long orderId){
		this.orderId = orderId;
	}
	
	public String getDetailOtherId(){
		return this.detailOtherId;
	}
	
	public void setDetailOtherId(String detailOtherId){
		this.detailOtherId = detailOtherId;
	}
	
	public Long getItemId(){
		return this.itemId;
	}
	
	public void setItemId(Long itemId){
		this.itemId = itemId;
	}
	
	public Long getSkuId(){
		return this.skuId;
	}
	
	public void setSkuId(Long skuId){
		this.skuId = skuId;
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
	
	public Long getLotId(){
		return this.lotId;
	}
	
	public void setLotId(Long lotId){
		this.lotId = lotId;
	}
	
	public Long getStatus(){
		return this.status;
	}
	
	public void setStatus(Long status){
		this.status = status;
	}
	
	public Long getIsValid(){
		return this.isValid;
	}
	
	public void setIsValid(Long isValid){
		this.isValid = isValid;
	}
	
	public BigDecimal getReqQty(){
		return this.reqQty;
	}
	
	public void setReqQty(BigDecimal reqQty){
		this.reqQty = reqQty;
	}
	
	public BigDecimal getAllocQty(){
		return this.allocQty;
	}
	
	public void setAllocQty(BigDecimal allocQty){
		this.allocQty = allocQty;
	}
	
	public String getAllocUnitName(){
		return this.allocUnitName;
	}
	
	public void setAllocUnitName(String allocUnitName){
		this.allocUnitName = allocUnitName;
	}
	
	public BigDecimal getAllocUnitQty(){
		return this.allocUnitQty;
	}
	
	public void setAllocUnitQty(BigDecimal allocUnitQty){
		this.allocUnitQty = allocUnitQty;
	}
	
	public Long getLocationId(){
		return this.locationId;
	}
	
	public void setLocationId(Long locationId){
		this.locationId = locationId;
	}
	
	public Long getContainerId(){
		return this.containerId;
	}
	
	public void setContainerId(Long containerId){
		this.containerId = containerId;
	}
	
	public Long getOperator(){
		return this.operator;
	}
	
	public void setOperator(Long operator){
		this.operator = operator;
	}
	
	public Long getDoneAt(){
		return this.doneAt;
	}
	
	public void setDoneAt(Long doneAt){
		this.doneAt = doneAt;
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


	public Long getRealContainerId() {
		return realContainerId;
	}

	public void setRealContainerId(Long realContainerId) {
		this.realContainerId = realContainerId;
	}
}
