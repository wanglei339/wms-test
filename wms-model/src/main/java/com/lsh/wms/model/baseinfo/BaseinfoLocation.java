package com.lsh.wms.model.baseinfo;

import java.io.Serializable;
import java.util.Date;

public class BaseinfoLocation implements Serializable {

	/**  */
    private Long id;
	/** 位置id */
    private Long locationId;
	/** 位置编码 */
    private String locationCode;
	/** 父级位置id */
    private Long fatherId;
	/** 存储位类型 */
    private Long type;
	/** 类型名 */
    private String typeName;
	/** 是否为叶子位置节点 */
    private Integer isLeaf;
	/** 是否可用 */
    private Integer isValid;
	/** 是否是存储用位置 */
    private Integer canStore;
	/** 是否被占用，0-未使用，1-已占用 */
    private Integer inUse;
	/** 可容纳容器数量 */
    private Long containerVol;
	/** 描述 */
    private String description;
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
	
	public Long getLocationId(){
		return this.locationId;
	}
	
	public void setLocationId(Long locationId){
		this.locationId = locationId;
	}
	
	public String getLocationCode(){
		return this.locationCode;
	}
	
	public void setLocationCode(String locationCode){
		this.locationCode = locationCode;
	}
	
	public Long getFatherId(){
		return this.fatherId;
	}
	
	public void setFatherId(Long fatherId){
		this.fatherId = fatherId;
	}
	
	public Long getType(){
		return this.type;
	}
	
	public void setType(Long type){
		this.type = type;
	}
	
	public String getTypeName(){
		return this.typeName;
	}
	
	public void setTypeName(String typeName){
		this.typeName = typeName;
	}
	
	public Integer getIsLeaf(){
		return this.isLeaf;
	}
	
	public void setIsLeaf(Integer isLeaf){
		this.isLeaf = isLeaf;
	}
	
	public Integer getIsValid(){
		return this.isValid;
	}
	
	public void setIsValid(Integer isValid){
		this.isValid = isValid;
	}
	
	public Integer getCanStore(){
		return this.canStore;
	}
	
	public void setCanStore(Integer canStore){
		this.canStore = canStore;
	}
	
	public Integer getInUse(){
		return this.inUse;
	}
	
	public void setInUse(Integer inUse){
		this.inUse = inUse;
	}
	
	public Long getContainerVol(){
		return this.containerVol;
	}
	
	public void setContainerVol(Long containerVol){
		this.containerVol = containerVol;
	}
	
	public String getDescription(){
		return this.description;
	}
	
	public void setDescription(String description){
		this.description = description;
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
