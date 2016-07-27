package com.lsh.wms.model.taking;

import com.lsh.base.common.utils.DateUtils;

import java.io.Serializable;
import java.util.Date;

public class StockTakingHead implements Serializable {

	/**  */
    private Long id;
	/** 盘点计划id */
    private Long takingId;
	/** 盘点名字 */
    private String name = "";
	/** 发起者id */
    private Long planner = 0L;
	/** 盘点类型, 1-库存盘点， 2-库位盘点 */
    private Long takingType = 1L;
	/** 1-明盘, 2-暗盘 */
    private Long viewType = 1L;
	/** 任务汇总方法，0-按照商品id, 1-按照location */
    private Long clusterMethod = 0L;
	/** 默认差异盘点轮数 */
    private Long maxChkRnd = 1L;
	/** 盘点日期 */
    private Long date = 0L;
	/** 盘点任务状态， 1-draft， 2-assinged, 3-done, 4-confirmed, 4-cancel */
    private Long status = 1L;
	/** 盘哪个供应商的货 */
    private Long supplierId = 0L;
	/** 货主id */
    private Long ownerId = 0L;
	/** 存储库位id */
    private String locationList = "";
	/** 批次id */
    private Long lotId = 0L;
	/** 商品id */
    private Long  skuId= 0L;
	/** 盘点计划类型 */
	private Long planType = 0L;
	/**  任务要求时间 */
	private Long dueTime = 0L;
	/** 库区id*/
	private Long areaId = 0L;
	/** */
	private Long itemId = 0L;
	/**  */
    private Long createdAt = DateUtils.getCurrentSeconds();
	/**  */
    private Long updatedAt = DateUtils.getCurrentSeconds();


	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	private String details;

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
	
	public String getName(){
		return this.name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public Long getPlanner(){
		return this.planner;
	}
	
	public void setPlanner(Long planner){
		this.planner = planner;
	}
	
	public Long getTakingType(){
		return this.takingType;
	}
	
	public void setTakingType(Long takingType){
		this.takingType = takingType;
	}
	
	public Long getViewType(){
		return this.viewType;
	}
	
	public void setViewType(Long viewType){
		this.viewType = viewType;
	}
	
	public Long getClusterMethod(){
		return this.clusterMethod;
	}
	
	public void setClusterMethod(Long clusterMethod){
		this.clusterMethod = clusterMethod;
	}
	
	public Long getMaxChkRnd(){
		return this.maxChkRnd;
	}
	
	public void setMaxChkRnd(Long maxChkRnd){
		this.maxChkRnd = maxChkRnd;
	}
	
	public Long getDate(){
		return this.date;
	}
	
	public void setDate(Long date){
		this.date = date;
	}
	
	public Long getStatus(){
		return this.status;
	}
	
	public void setStatus(Long status){
		this.status = status;
	}
	
	public Long getSupplierId(){
		return this.supplierId;
	}
	
	public void setSupplierId(Long supplierId){
		this.supplierId = supplierId;
	}
	
	public Long getOwnerId(){
		return this.ownerId;
	}
	
	public void setOwnerId(Long ownerId){
		this.ownerId = ownerId;
	}

	public String getLocationList() {
		return locationList;
	}

	public void setLocationList(String locationList) {
		this.locationList = locationList;
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

	public Long getPlanType() {
		return planType;
	}

	public void setPlanType(Long planType) {
		this.planType = planType;
	}

	public Long getDueTime() {
		return dueTime;
	}

	public void setDueTime(Long dueTime) {
		this.dueTime = dueTime;
	}

	public Long getAreaId() {
		return areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}
}
