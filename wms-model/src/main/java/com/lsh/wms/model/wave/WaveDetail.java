package com.lsh.wms.model.wave;

import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

public class WaveDetail implements Serializable {

	/**  */
    private Long id;
	/** 关联出库订单中的上游id, 不存在数据库中,现在还没有 */
	private String refObdDetailOtherId;
	/** 0代表生命周期结束了,也即是否在一个有效的波次周期内，波次完成或者取消这个值要标记为无效,否则会有问题 */
    private Long isAlive = 1L;
	/** 参考detail—id，分裂使用 */
	private Long refDetailId = 0L;
	/** 是否有效，比如被合盘的情况下，原记录被标记为无效 */
    private Long isValid = 1L;
	/** 波次id */
    private Long waveId = 0L;
	/** 订单id */
    private Long orderId = 0L;
	/** 商品码 */
	private Long itemId = 0L;
	/** 商品id */
    private Long skuId = 0L;
	/** 货主id */
    private Long ownerId = 0L;
	/** 批次id */
	private Long locId = 0L;
	/** 供商id */
	private Long supplierId = 0L;
	/** 0-新建，呵呵，冗余一下，以后看用不用吧 */
	private Long status = 1L;
	/** 订单需求量 */
	private BigDecimal reqQty = new BigDecimal("0.0000");
	/** 配货库存量 */
	private BigDecimal allocQty = BigDecimal.ZERO;
	/** 分配库存单位名称 */
	private String allocUnitName = "EA";
	/** 分配库存单位数量 */
	private BigDecimal allocUnitQty = new BigDecimal("0.0000");
	/** 实际捡货量 */
	private BigDecimal pickQty = new BigDecimal("0.0000");
	/** qc确认数量 */
	private BigDecimal qcQty = new BigDecimal("0.0000");
	/** 最终出库量 */
	private BigDecimal deliveryQty = new BigDecimal("0.0000");
	/** 捡货任务id */
	private Long pickTaskId =0L;
	/** 分配的捡货分区,通过分区信息取获取对应的区域路径，可获取到虾面的捡货位 */
    private Long pickZoneId = 0L;
	/** 分配分拣区域locationid */
	private Long pickAreaLocation = 0L;
	/** 分配分拣区域location,数据库里没有,用来传数据的 */
	private BaseinfoLocation pickArea;
	/** 拣货顺序 */
	private Long pickOrder = 0L;
	/** 拣货任务序号 */
	private Long pickTaskOrder;
	/** 分配分拣位 */
    private Long allocPickLocation = 0L;
	/** 实际分拣位 */
    private Long realPickLocation = 0L;
	/** 分配的集货位 */
    private Long allocCollectLocation = 0L;
	/** 实际的集货位 */
    private Long realCollectLocation = 0L;
	/** 容器id,非常重要的字断，务必维护好当前真实的商品所在的container信息，否则就惨了 */
    private Long containerId = 0L;
	/** 合板后的container_id关系 */
	private Long mergedContainerId = 0L;
	/** 拣货员id */
    private Long pickUid = 0L;
	/** 捡货时间 */
    private Long pickAt = 0L;
	/** 播种任务id */
    private Long sowTaskId = 0L;
	/** 播种员id */
    private Long sowUid = 0L;
	/** 播种时间 */
    private Long sowAt = 0L;
	/** QC任务id */
    private Long qcTaskId = 0L;
	/** QC员id */
    private Long qcUid = 0L;
	/** QC时间 */
    private Long qcAt = 0L;
	/** 合板任务id */
	private Long mergeTaskId = 0L;
	/** 合板人员id */
	private Long mergeUid = 0L;
	/** 合板时间 */
	private Long mergeAt = DateUtils.getCurrentSeconds();
	/** 0-正常，1-少货，2-多货，3-错货，4-残次，5-日期不好，6-其他异常 */
    private Long qcException = 0L;
	/** qc异常数量 */
    private BigDecimal qcExceptionQty = new BigDecimal("0.0000");
	/** 异常是否已处理，0-未处理，1-正常不需要处理，2-已处理，3-忽略异常 */
    private Long qcExceptionDone = 0L;
	/** 发货任务id */
    private Long shipTaskId = 0L;
	/** 发货员id */
    private Long shipUid = 0L;
	/** 发货时间 */
    private Long shipAt = 0L;
	/** 关联发货单id */
	private Long deliveryId = 0L;
	/**  */
    private Long createdAt = 0L;
	/**  */
    private Long updatedAt = 0L;
	/** qc的进入次数默认0，1-第一次2-复Q */
	private Integer qcTimes = 1;
	/** qc追责任0-无责任1-拣货人责任2-qc的责任 */
	private Long qcFault = 0L;
	/** 追责数量 */
	private BigDecimal qcFaultQty = new BigDecimal("0.0000");


	public Long getId(){
		return this.id;
	}

	public void setId(Long id){
		this.id = id;
	}

	public void setRefObdDetailOtherId(String refObdDetailOtherId){
		this.refObdDetailOtherId = refObdDetailOtherId;
	}

	public String getRefObdDetailOtherId(){
		return this.refObdDetailOtherId;
	}

	public Long getRefDetailId(){
		return this.refDetailId;
	}

	public void setRefDetailId(Long refDetailId){
		this.refDetailId = refDetailId;
	}

	public Long getIsAlive(){
		return this.isAlive;
	}

	public void setIsAlive(Long isAlive){
		this.isAlive = isAlive;
	}

	public Long getIsValid(){
		return this.isValid;
	}

	public void setIsValid(Long isValid){
		this.isValid = isValid;
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

	public Long getItemId() {
		return itemId;
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

	public Long getStatus(){
		return this.status;
	}

	public void setStatus(Long status){
		this.status = status;
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

	public BigDecimal getAllocUnitQty(){
		return this.allocUnitQty;
	}

	public void setAllocUnitQty(BigDecimal allocUnitQty){
		this.allocUnitQty = allocUnitQty;
	}

	public String getAllocUnitName(){
		return this.allocUnitName;
	}

	public void setAllocUnitName(String allocUnitName){
		this.allocUnitName = allocUnitName;
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

	public Long getPickTaskId(){
		return this.pickTaskId;
	}

	public void setPickTaskId(Long pickTaskId){
		this.pickTaskId = pickTaskId;
	}

	public Long getPickZoneId(){
		return this.pickZoneId;
	}

	public void setPickZoneId(Long pickZoneId){
		this.pickZoneId = pickZoneId;
	}

	public Long getPickAreaLocation(){
		return this.pickAreaLocation;
	}

	public void setPickAreaLocation(Long pickAreaLocation){
		this.pickAreaLocation = pickAreaLocation;
	}

	public Long getPickOrder(){
		return this.pickOrder;
	}

	public void setPickOrder(Long pickOrder){
		this.pickOrder = pickOrder;
	}

	public Long getPickTaskOrder(){
		return this.pickTaskOrder;
	}

	public void setPickTaskOrder(Long pickTaskOrder){
		this.pickTaskOrder = pickTaskOrder;
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

	public Long getAllocCollectLocation(){
		return this.allocCollectLocation;
	}

	public void setAllocCollectLocation(Long allocCollectLocation){
		this.allocCollectLocation = allocCollectLocation;
	}

	public Long getRealCollectLocation(){
		return this.realCollectLocation;
	}

	public void setRealCollectLocation(Long realCollectLocation){
		this.realCollectLocation = realCollectLocation;
	}

	public Long getContainerId(){
		return this.containerId;
	}

	public void setContainerId(Long containerId){
		this.containerId = containerId;
	}

	public Long getMergedContainerId(){
		return this.mergedContainerId;
	}

	public void setMergedContainerId(Long mergedContainerId){
		this.mergedContainerId = mergedContainerId;
	}
	
	public Long getPickUid(){
		return this.pickUid;
	}

	public void setPickUid(Long pickUid){
		this.pickUid = pickUid;
	}

	public Long getPickAt(){
		return this.pickAt;
	}

	public void setPickAt(Long pickAt){
		this.pickAt = pickAt;
	}

	public Long getSowTaskId(){
		return this.sowTaskId;
	}

	public void setSowTaskId(Long sowTaskId){
		this.sowTaskId = sowTaskId;
	}

	public Long getSowUid(){
		return this.sowUid;
	}

	public void setSowUid(Long sowUid){
		this.sowUid = sowUid;
	}

	public Long getSowAt(){
		return this.sowAt;
	}

	public void setSowAt(Long sowAt){
		this.sowAt = sowAt;
	}

	public Long getQcTaskId(){
		return this.qcTaskId;
	}

	public void setQcTaskId(Long qcTaskId){
		this.qcTaskId = qcTaskId;
	}

	public Long getQcUid(){
		return this.qcUid;
	}

	public void setQcUid(Long qcUid){
		this.qcUid = qcUid;
	}

	public Long getQcAt(){
		return this.qcAt;
	}

	public void setQcAt(Long qcAt){
		this.qcAt = qcAt;
	}

	public Long getMergeTaskId(){
		return this.mergeTaskId;
	}

	public void setMergeTaskId(Long mergeTaskId){
		this.mergeTaskId = mergeTaskId;
	}

	public Long getMergeUid(){
		return this.mergeUid;
	}

	public void setMergeUid(Long mergeUid){
		this.mergeUid = mergeUid;
	}

	public Long getMergeAt(){
		return this.mergeAt;
	}

	public void setMergeAt(Long mergeAt){
		this.mergeAt = mergeAt;
	}

	public Long getQcException(){
		return this.qcException;
	}

	public void setQcException(Long qcException){
		this.qcException = qcException;
	}

	public BigDecimal getQcExceptionQty(){
		return this.qcExceptionQty;
	}

	public void setQcExceptionQty(BigDecimal qcExceptionQty){
		this.qcExceptionQty = qcExceptionQty;
	}

	public Long getQcExceptionDone(){
		return this.qcExceptionDone;
	}

	public void setQcExceptionDone(Long qcExceptionDone){
		this.qcExceptionDone = qcExceptionDone;
	}

	public Long getShipTaskId(){
		return this.shipTaskId;
	}

	public void setShipTaskId(Long shipTaskId){
		this.shipTaskId = shipTaskId;
	}

	public Long getShipUid(){
		return this.shipUid;
	}

	public void setShipUid(Long shipUid){
		this.shipUid = shipUid;
	}

	public Long getShipAt(){
		return this.shipAt;
	}

	public void setShipAt(Long shipAt){
		this.shipAt = shipAt;
	}

	public Long getDeliveryId(){
		return this.deliveryId;
	}

	public void setDeliveryId(Long deliveryId){
		this.deliveryId = deliveryId;
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

	public Integer getQcTimes(){
		return this.qcTimes;
	}

	public void setQcTimes(Integer qcTimes){
		this.qcTimes = qcTimes;
	}

	public Long getQcFault(){
		return this.qcFault;
	}

	public void setQcFault(Long qcFault){
		this.qcFault = qcFault;
	}

	public BigDecimal getQcFaultQty(){
		return this.qcFaultQty;
	}

	public void setQcFaultQty(BigDecimal qcFaultQty){
		this.qcFaultQty = qcFaultQty;
	}

	public void setPickArea(BaseinfoLocation pickArea){
		this.pickArea = pickArea;
	}

	public BaseinfoLocation getPickArea(){
		return this.pickArea;
	}

}
