package com.lsh.wms.model.baseinfo;

import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

@Component
public class BaseinfoLocation extends IBaseinfoLocaltionModel implements Serializable  {

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
	/** 区域坐标，四维坐标-区位坐标 */
    private Long regionNo;
	/** 通道坐标，四维坐标-通道坐标 */
    private Long passageNo;
	/** 货架层坐标，四维坐标-层数坐标 */
    private Long shelfLevelNo;
	/** 货位同层坐标，四维坐标-同层 */
    private Long binPositionNo;
	/** 描述 */
    private String description;
	/** 创建日期 */
    private Long createdAt;
	/** 更新日期 */
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
