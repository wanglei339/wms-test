package com.lsh.wms.model.baseinfo;

import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

public class BaseinfoLocation implements Serializable,IBaseinfoLocaltionModel  {

	/**  */
    private Long id;
	/** 位置id */
    protected Long locationId;
	/** 位置编码 */
	protected String locationCode;
	/** 父级位置id */
	protected Long fatherId;
	/** 存储位类型 */
	protected Long type;
	/** 类型名 */
	protected String typeName;
	/** 是否为叶子位置节点 */
	protected Integer isLeaf;
	/** 是否可用 */
	protected Integer isValid;
	/** 是否是存储用位置 */
	protected Integer canStore;
	/** 可容纳容器数量 */
	protected Long containerVol;
	/** 区域坐标，四维坐标-区位坐标 */
	protected Long regionNo;
	/** 通道坐标，四维坐标-通道坐标 */
	protected Long passageNo;
	/** 货架层坐标，四维坐标-层数坐标 */
	protected Long shelfLevelNo;
	/** 货位同层坐标，四维坐标-同层 */
	protected Long binPositionNo;
	/** 所有的位置能够可用*/
	protected String isUsed;
	/** 区域名称,显示时候使用*/
	protected String regionName;

	/** 描述 */
	protected String description;
	/** 创建日期 */
	protected Long createdAt;
	/** 更新日期 */
	protected Long updatedAt;

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public String getIsUsed() {
		return isUsed;
	}

	public void setIsUsed(String isUsed) {
		this.isUsed = isUsed;
	}

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

	public Long getContainerVol(){
		return this.containerVol;
	}
	
	public void setContainerVol(Long containerVol){
		this.containerVol = containerVol;
	}
	
	public Long getRegionNo(){
		return this.regionNo;
	}
	
	public void setRegionNo(Long regionNo){
		this.regionNo = regionNo;
	}
	
	public Long getPassageNo(){
		return this.passageNo;
	}
	
	public void setPassageNo(Long passageNo){
		this.passageNo = passageNo;
	}
	
	public Long getShelfLevelNo(){
		return this.shelfLevelNo;
	}
	
	public void setShelfLevelNo(Long shelfLevelNo){
		this.shelfLevelNo = shelfLevelNo;
	}
	
	public Long getBinPositionNo(){
		return this.binPositionNo;
	}
	
	public void setBinPositionNo(Long binPositionNo){
		this.binPositionNo = binPositionNo;
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
