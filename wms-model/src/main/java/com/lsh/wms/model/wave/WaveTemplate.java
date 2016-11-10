package com.lsh.wms.model.wave;

import java.io.Serializable;
import java.util.Date;

public class WaveTemplate implements Serializable {

	/**  */
    private Long id;
	/**  */
    private Long waveTemplateId;
	/**  */
    private String waveTemplateName = "";
	/**  */
    private Long status = 1L;
	/**  */
    private String waveOrderType = "TOB-NORMAL";
	/** 是否依据线路聚合波次 */
    private Long clusterRoute=1L;
	/** 是否依据客户号聚合波次 */
    private Long clusterCustomer=0L;
	/** 是否启用订单相似度聚合策略 */
    private Long stgOrderSimilar=0L;
	/** 波次订单限制数量 */
    private Long orderLimit=99999L;
	/** 自动释放 0-否 1-是 */
    private Long autoRelease=0L;
	/** 释放时间设置 */
    private String releaseTimeList="";
	/** 波次产生目的, COMMON-普通, YG-优供, SUPERMARKET-大卖场 */
    private String waveDest="COMMON";
	/** 集货道组 */
    private Long collectLocations=0L;
	/** 是否动态分配集货道 */
    private Long collectDynamic=1L;
	/** 集货道分配方式，1-按线路,2-按客户 */
    private Long collectAllocModel=1L;
	/** 是否使用精细的集货位 */
	private Long collectBinUse=0L;
	/** 捡货模型模版id */
    private Long pickModelTemplateId=0L;
	/**  */
    private Long createdAt=0L;
	/**  */
    private Long updatedAt=0L;
	
	public Long getId(){
		return this.id;
	}
	
	public void setId(Long id){
		this.id = id;
	}
	
	public Long getWaveTemplateId(){
		return this.waveTemplateId;
	}
	
	public void setWaveTemplateId(Long waveTemplateId){
		this.waveTemplateId = waveTemplateId;
	}
	
	public String getWaveTemplateName(){
		return this.waveTemplateName;
	}
	
	public void setWaveTemplateName(String waveTemplateName){
		this.waveTemplateName = waveTemplateName;
	}
	
	public Long getStatus(){
		return this.status;
	}
	
	public void setStatus(Long status){
		this.status = status;
	}
	
	public String getWaveOrderType(){
		return this.waveOrderType;
	}
	
	public void setWaveOrderType(String waveOrderType){
		this.waveOrderType = waveOrderType;
	}
	
	public Long getClusterRoute(){
		return this.clusterRoute;
	}
	
	public void setClusterRoute(Long clusterRoute){
		this.clusterRoute = clusterRoute;
	}
	
	public Long getClusterCustomer(){
		return this.clusterCustomer;
	}
	
	public void setClusterCustomer(Long clusterCustomer){
		this.clusterCustomer = clusterCustomer;
	}
	
	public Long getStgOrderSimilar(){
		return this.stgOrderSimilar;
	}
	
	public void setStgOrderSimilar(Long stgOrderSimilar){
		this.stgOrderSimilar = stgOrderSimilar;
	}
	
	public Long getOrderLimit(){
		return this.orderLimit;
	}
	
	public void setOrderLimit(Long orderLimit){
		this.orderLimit = orderLimit;
	}
	
	public Long getAutoRelease(){
		return this.autoRelease;
	}
	
	public void setAutoRelease(Long autoRelease){
		this.autoRelease = autoRelease;
	}
	
	public String getReleaseTimeList(){
		return this.releaseTimeList;
	}
	
	public void setReleaseTimeList(String releaseTimeList){
		this.releaseTimeList = releaseTimeList;
	}
	
	public String getWaveDest(){
		return this.waveDest;
	}
	
	public void setWaveDest(String waveDest){
		this.waveDest = waveDest;
	}
	
	public Long getCollectLocations(){
		return this.collectLocations;
	}
	
	public void setCollectLocations(Long collectLocations){
		this.collectLocations = collectLocations;
	}
	
	public Long getCollectDynamic(){
		return this.collectDynamic;
	}
	
	public void setCollectDynamic(Long collectDynamic){
		this.collectDynamic = collectDynamic;
	}
	
	public Long getCollectAllocModel(){
		return this.collectAllocModel;
	}
	
	public void setCollectAllocModel(Long collectAllocModel){
		this.collectAllocModel = collectAllocModel;
	}

	public Long getCollectBinUse(){
		return this.collectBinUse;
	}

	public void setCollectBinUse(Long collectBinUse){
		this.collectBinUse = collectBinUse;
	}
	
	public Long getPickModelTemplateId(){
		return this.pickModelTemplateId;
	}
	
	public void setPickModelTemplateId(Long pickModelTemplateId){
		this.pickModelTemplateId = pickModelTemplateId;
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
