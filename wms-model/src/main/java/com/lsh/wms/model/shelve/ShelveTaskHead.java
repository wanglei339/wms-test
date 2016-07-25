package com.lsh.wms.model.shelve;

import java.io.Serializable;
import java.util.Date;

public class ShelveTaskHead implements Serializable {

	/**  */
    private Long id;
	/** 任务id */
    private Long taskId;
	/** 收货单id */
    private Long receiveId;
	/** 收货码 */
    private Long receiveCode;
	/** 订单id */
    private Long orderId;
	/** 商品id */
    private Long skuId;
	/** 货主id */
    private Long ownerId;
	/** 批次id */
    private Long locId;
	/** 供商id */
    private Long supplierId;
	/** 分配库位 */
    private Long allocLocationId;
	/** 实际库位 */
    private Long realLocationId;
	/** 容器id */
    private Long containerId;
	/** 上架人员id */
    private Long shelveUid;
	/** 上架人员名称 */
    private String shelveUname;
	/** 上架时间 */
    private Long shelveAt;
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
	
	public Long getTaskId(){
		return this.taskId;
	}
	
	public void setTaskId(Long taskId){
		this.taskId = taskId;
	}
	
	public Long getReceiveId(){
		return this.receiveId;
	}
	
	public void setReceiveId(Long receiveId){
		this.receiveId = receiveId;
	}
	
	public Long getReceiveCode(){
		return this.receiveCode;
	}
	
	public void setReceiveCode(Long receiveCode){
		this.receiveCode = receiveCode;
	}
	
	public Long getOrderId(){
		return this.orderId;
	}
	
	public void setOrderId(Long orderId){
		this.orderId = orderId;
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
	
	public Long getLocId(){
		return this.locId;
	}
	
	public void setLocId(Long locId){
		this.locId = locId;
	}
	
	public Long getSupplierId(){
		return this.supplierId;
	}
	
	public void setSupplierId(Long supplierId){
		this.supplierId = supplierId;
	}
	
	public Long getAllocLocationId(){
		return this.allocLocationId;
	}
	
	public void setAllocLocationId(Long allocLocationId){
		this.allocLocationId = allocLocationId;
	}
	
	public Long getRealLocationId(){
		return this.realLocationId;
	}
	
	public void setRealLocationId(Long realLocationId){
		this.realLocationId = realLocationId;
	}
	
	public Long getContainerId(){
		return this.containerId;
	}
	
	public void setContainerId(Long containerId){
		this.containerId = containerId;
	}
	
	public Long getShelveUid(){
		return this.shelveUid;
	}
	
	public void setShelveUid(Long shelveUid){
		this.shelveUid = shelveUid;
	}
	
	public String getShelveUname(){
		return this.shelveUname;
	}
	
	public void setShelveUname(String shelveUname){
		this.shelveUname = shelveUname;
	}
	
	public Long getShelveAt(){
		return this.shelveAt;
	}
	
	public void setShelveAt(Long shelveAt){
		this.shelveAt = shelveAt;
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
