package com.lsh.wms.model.pick;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

public class PickTaskDetail implements Serializable {

	/**  */
    private Long id;
	/** 波次id */
    private Long waveId;
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
	/** 订单需求量 */
    private BigDecimal reqQty;
	/** 配货库存量 */
    private BigDecimal allocQty;
	/** 分配的捡货分区 */
    private BigDecimal pickZoneId;
	/** 实际捡货量 */
    private BigDecimal pickQty;
	/** qc确认数量 */
    private BigDecimal qcQty;
	/** 最终出库量 */
    private BigDecimal deliveryQty;
	/** 分配的捡货位 */
    private Long srcLocationId;
	/** 分配的集货位 */
    private Long dstLocationId;
	/** 任务id */
    private Long pickTaskId;
	/** 容器id */
    private Long containerId;
	/** 拣货员id */
    private Long pickUid;
	/** 拣货员名称 */
    private String pickUname;
	/** 捡货时间 */
    private Long pickAt;
	/** 分配分拣位 */
    private Long allocPickLocation;
	/** 实际分拣位 */
    private Long realPickLocation;
	/** QC员id */
    private Long qcUid;
	/** QC员名称 */
    private String qcUname;
	/** QC时间 */
    private Long qcAt;
	/** 播种任务id */
    private Long sowTaskId;
	
	public Long getId(){
		return this.id;
	}
	
	public void setId(Long id){
		this.id = id;
	}
	
	public Long getWaveId(){
		return this.waveId;
	}
	
	public void setWaveId(Long waveId){
		this.waveId = waveId;
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
	
	public BigDecimal getPickZoneId(){
		return this.pickZoneId;
	}
	
	public void setPickZoneId(BigDecimal pickZoneId){
		this.pickZoneId = pickZoneId;
	}
	
	public BigDecimal getPickQty(){
		return this.pickQty;
	}
	
	public void setPickQty(BigDecimal pickQty){
		this.pickQty = pickQty;
	}
	
	public BigDecimal getQcQty(){
		return this.qcQty;
	}
	
	public void setQcQty(BigDecimal qcQty){
		this.qcQty = qcQty;
	}
	
	public BigDecimal getDeliveryQty(){
		return this.deliveryQty;
	}
	
	public void setDeliveryQty(BigDecimal deliveryQty){
		this.deliveryQty = deliveryQty;
	}
	
	public Long getSrcLocationId(){
		return this.srcLocationId;
	}
	
	public void setSrcLocationId(Long srcLocationId){
		this.srcLocationId = srcLocationId;
	}
	
	public Long getDstLocationId(){
		return this.dstLocationId;
	}
	
	public void setDstLocationId(Long dstLocationId){
		this.dstLocationId = dstLocationId;
	}
	
	public Long getPickTaskId(){
		return this.pickTaskId;
	}
	
	public void setPickTaskId(Long pickTaskId){
		this.pickTaskId = pickTaskId;
	}
	
	public Long getContainerId(){
		return this.containerId;
	}
	
	public void setContainerId(Long containerId){
		this.containerId = containerId;
	}
	
	public Long getPickUid(){
		return this.pickUid;
	}
	
	public void setPickUid(Long pickUid){
		this.pickUid = pickUid;
	}
	
	public String getPickUname(){
		return this.pickUname;
	}
	
	public void setPickUname(String pickUname){
		this.pickUname = pickUname;
	}
	
	public Long getPickAt(){
		return this.pickAt;
	}
	
	public void setPickAt(Long pickAt){
		this.pickAt = pickAt;
	}
	
	public Long getAllocPickLocation(){
		return this.allocPickLocation;
	}
	
	public void setAllocPickLocation(Long allocPickLocation){
		this.allocPickLocation = allocPickLocation;
	}
	
	public Long getRealPickLocation(){
		return this.realPickLocation;
	}
	
	public void setRealPickLocation(Long realPickLocation){
		this.realPickLocation = realPickLocation;
	}
	
	public Long getQcUid(){
		return this.qcUid;
	}
	
	public void setQcUid(Long qcUid){
		this.qcUid = qcUid;
	}
	
	public String getQcUname(){
		return this.qcUname;
	}
	
	public void setQcUname(String qcUname){
		this.qcUname = qcUname;
	}
	
	public Long getQcAt(){
		return this.qcAt;
	}
	
	public void setQcAt(Long qcAt){
		this.qcAt = qcAt;
	}
	
	public Long getSowTaskId(){
		return this.sowTaskId;
	}
	
	public void setSowTaskId(Long sowTaskId){
		this.sowTaskId = sowTaskId;
	}
	
	
}
