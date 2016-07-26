package com.lsh.wms.model.baseinfo;

import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
@Component
public class BaseinfoLocationRegion extends IBaseinfoLocaltionModel implements Serializable {

	/**  */
    private Long id;
	/** 位置id */
    private Long locationId;
	/** 长度默认单位 米 */
    private BigDecimal length;
	/** 宽度默认单位 米 */
    private BigDecimal width;
	/** 创建日期 */
    private Long createdAt;
	/** 更新日期 */
    private Long updatedAt;
	/** 描述 */
    private String description;
	/** 区域类型同主表type */
    private Integer type;
	/** 区域名字 */
    private String regionName;
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
	
	public BigDecimal getLength(){
		return this.length;
	}
	
	public void setLength(BigDecimal length){
		this.length = length;
	}
	
	public BigDecimal getWidth(){
		return this.width;
	}
	
	public void setWidth(BigDecimal width){
		this.width = width;
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
	
	public String getDescription(){
		return this.description;
	}
	
	public void setDescription(String description){
		this.description = description;
	}
	
	public Integer getType(){
		return this.type;
	}
	
	public void setType(Integer type){
		this.type = type;
	}
	
	public String getRegionName(){
		return this.regionName;
	}
	
	public void setRegionName(String regionName){
		this.regionName = regionName;
	}
	
	public String getIsUsed(){
		return this.isUsed;
	}
	
	public void setIsUsed(String isUsed){
		this.isUsed = isUsed;
	}
	
	
}
