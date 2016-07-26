package com.lsh.wms.model.baseinfo;

import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
@Component
public class BaseinfoLocationShelf extends IBaseinfoLocaltionModel implements Serializable {

	/**  */
    private Long id;
	/** 位置id */
    private Long locationId;
	/** 货架种类(货架/阁楼) */
    private Integer shelfType;
	/** 货架层数 */
    private Long level;
	/** 货架进深 */
    private Long depth;
	/** 描述 */
    private String description;
	/** 创建日期 */
    private Long createdAt;
	/** 更新日期 */
    private Long updatedAt;
	/** 主表的type */
    private Integer type;
	/** 是否可用 */
    private String isUsed;
	
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
	
	public Integer getShelfType(){
		return this.shelfType;
	}
	
	public void setShelfType(Integer shelfType){
		this.shelfType = shelfType;
	}
	
	public Long getLevel(){
		return this.level;
	}
	
	public void setLevel(Long level){
		this.level = level;
	}
	
	public Long getDepth(){
		return this.depth;
	}
	
	public void setDepth(Long depth){
		this.depth = depth;
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
	
	public Integer getType(){
		return this.type;
	}
	
	public void setType(Integer type){
		this.type = type;
	}
	
	public String getIsUsed(){
		return this.isUsed;
	}
	
	public void setIsUsed(String isUsed){
		this.isUsed = isUsed;
	}
	
	
}
