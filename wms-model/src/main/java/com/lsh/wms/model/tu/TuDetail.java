package com.lsh.wms.model.tu;

import java.io.Serializable;
import java.util.Date;

public class TuDetail implements Serializable {

	/**  */
    private Long id;
	/** 运单号 */
    private String tuId;
	/** 合板的板id */
    private Long mergedContainerId;
	/** 组盘后板上总箱数 */
    private String boxNum;
	/** 托盘数聚合 */
    private Integer containerNum;
	/** 组盘后板上的周转箱数 */
    private Long turnoverBoxNum;
	/** 是否余货，0不是，1是 */
    private Integer isRest;
	/** '是否贵品，0不是，1是', */
    private Integer isExpresive;
	/** 创建时间 */
    private Long createdAt;
	/** 更新时间 */
    private Long updatedAt;
	/** 装车人id|扫货的 */
    private Long loadUid;
	/** 装车扫板时间 */
    private Long loadAt;
	/** 门店id */
    private Long storeId;
	/** 是否有效, 0否, 1是 */
    private Integer isValid=1;
	
	public Long getId(){
		return this.id;
	}
	
	public void setId(Long id){
		this.id = id;
	}
	
	public String getTuId(){
		return this.tuId;
	}
	
	public void setTuId(String tuId){
		this.tuId = tuId;
	}
	
	public Long getMergedContainerId(){
		return this.mergedContainerId;
	}
	
	public void setMergedContainerId(Long mergedContainerId){
		this.mergedContainerId = mergedContainerId;
	}
	
	public String getBoxNum(){
		return this.boxNum;
	}
	
	public void setBoxNum(String boxNum){
		this.boxNum = boxNum;
	}
	
	public Integer getContainerNum(){
		return this.containerNum;
	}
	
	public void setContainerNum(Integer containerNum){
		this.containerNum = containerNum;
	}
	
	public Long getTurnoverBoxNum(){
		return this.turnoverBoxNum;
	}
	
	public void setTurnoverBoxNum(Long turnoverBoxNum){
		this.turnoverBoxNum = turnoverBoxNum;
	}
	
	public Integer getIsRest(){
		return this.isRest;
	}
	
	public void setIsRest(Integer isRest){
		this.isRest = isRest;
	}
	
	public Integer getIsExpresive(){
		return this.isExpresive;
	}
	
	public void setIsExpresive(Integer isExpresive){
		this.isExpresive = isExpresive;
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
	
	public Long getLoadUid(){
		return this.loadUid;
	}
	
	public void setLoadUid(Long loadUid){
		this.loadUid = loadUid;
	}
	
	public Long getLoadAt(){
		return this.loadAt;
	}
	
	public void setLoadAt(Long loadAt){
		this.loadAt = loadAt;
	}
	
	public Long getStoreId(){
		return this.storeId;
	}
	
	public void setStoreId(Long storeId){
		this.storeId = storeId;
	}
	
	public Integer getIsValid(){
		return this.isValid;
	}
	
	public void setIsValid(Integer isValid){
		this.isValid = isValid;
	}
	
	
}
