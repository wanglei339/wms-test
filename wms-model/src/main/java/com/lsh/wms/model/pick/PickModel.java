package com.lsh.wms.model.pick;

import java.io.Serializable;
import java.util.Date;

public class PickModel implements Serializable {

	/**  */
    private Long id;
	/** 捡货规则id */
    private Long pickModelId;
	/** 捡货模型模版id */
    private Long pickModelTemplate;
	/** 捡货分区id */
    private Long pickZoneId;
	/** 拣货模式：1-按单拣，2-按单边拣边播, 3-先摘果后播种 */
    private Long pickModel;
	/** 配货权重，值越大表示越优先出货 */
    private Long pickWeight;
	/** 使用容器 */
    private Long containerType;
	/** 单个容器容纳的按捡货/出货单位计算的数量,比如几个订单,几个ea，几箱，几个托盘 */
    private Long containerUnitCapacity;
	/** 捡货过程中单个人单个任务可携带的容器数量-会影响单摘果和边拣边播方式 */
    private Long containerNumPerTask;
	/** 摘果播种式一段聚合规则－按集货组 */
    private Long fpmrSetGroupFalg;
	/** 摘果播种式一段聚合规则－超过某个数量的商品直接单商品摘果（摘到多个集货道）, 0－无效 */
    private Long fpmrBigItemThreshold;
	/** 摘果播种式一段聚合规则－按集货道 */
    private Long fpmrSetFlag;
	/** 摘果播种式一段聚合规则－过某个数量的商品直接单商品摘果（摘到单个集货道）0-无效 */
    private Long fpmrSmallItemThreshold;
	/** 摘果播种式二段聚合规则－按什么规则播种，1-按商品，2-按客户 */
    private Long fpmrSowMethod;
	
	public Long getId(){
		return this.id;
	}
	
	public void setId(Long id){
		this.id = id;
	}
	
	public Long getPickModelId(){
		return this.pickModelId;
	}
	
	public void setPickModelId(Long pickModelId){
		this.pickModelId = pickModelId;
	}
	
	public Long getPickModelTemplate(){
		return this.pickModelTemplate;
	}
	
	public void setPickModelTemplate(Long pickModelTemplate){
		this.pickModelTemplate = pickModelTemplate;
	}
	
	public Long getPickZoneId(){
		return this.pickZoneId;
	}
	
	public void setPickZoneId(Long pickZoneId){
		this.pickZoneId = pickZoneId;
	}
	
	public Long getPickModel(){
		return this.pickModel;
	}
	
	public void setPickModel(Long pickModel){
		this.pickModel = pickModel;
	}
	
	public Long getPickWeight(){
		return this.pickWeight;
	}
	
	public void setPickWeight(Long pickWeight){
		this.pickWeight = pickWeight;
	}
	
	public Long getContainerType(){
		return this.containerType;
	}
	
	public void setContainerType(Long containerType){
		this.containerType = containerType;
	}
	
	public Long getContainerUnitCapacity(){
		return this.containerUnitCapacity;
	}
	
	public void setContainerUnitCapacity(Long containerUnitCapacity){
		this.containerUnitCapacity = containerUnitCapacity;
	}
	
	public Long getContainerNumPerTask(){
		return this.containerNumPerTask;
	}
	
	public void setContainerNumPerTask(Long containerNumPerTask){
		this.containerNumPerTask = containerNumPerTask;
	}
	
	public Long getFpmrSetGroupFalg(){
		return this.fpmrSetGroupFalg;
	}
	
	public void setFpmrSetGroupFalg(Long fpmrSetGroupFalg){
		this.fpmrSetGroupFalg = fpmrSetGroupFalg;
	}
	
	public Long getFpmrBigItemThreshold(){
		return this.fpmrBigItemThreshold;
	}
	
	public void setFpmrBigItemThreshold(Long fpmrBigItemThreshold){
		this.fpmrBigItemThreshold = fpmrBigItemThreshold;
	}
	
	public Long getFpmrSetFlag(){
		return this.fpmrSetFlag;
	}
	
	public void setFpmrSetFlag(Long fpmrSetFlag){
		this.fpmrSetFlag = fpmrSetFlag;
	}
	
	public Long getFpmrSmallItemThreshold(){
		return this.fpmrSmallItemThreshold;
	}
	
	public void setFpmrSmallItemThreshold(Long fpmrSmallItemThreshold){
		this.fpmrSmallItemThreshold = fpmrSmallItemThreshold;
	}
	
	public Long getFpmrSowMethod(){
		return this.fpmrSowMethod;
	}
	
	public void setFpmrSowMethod(Long fpmrSowMethod){
		this.fpmrSowMethod = fpmrSowMethod;
	}
	
	
}
