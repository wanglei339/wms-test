package com.lsh.wms.model.baseinfo;

import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
public class BaseinfoLocationRegion extends BaseinfoLocation implements Serializable,IBaseinfoLocaltionModel {

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
    private Long type;
	/** 仓库的货主1是物美2是链商 */
	private Long ownerid;
	/** 是否可用1可用，0删除 */
	private Integer isValid;
	/** 区域策略 */
	private Integer regionStrategy=0;

	@Override
	public Long getType() {
		return type;
	}

	@Override
	public void setType(Long type) {
		this.type = type;
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

	public Long getOwnerid() {
		return ownerid;
	}

	public void setOwnerid(Long ownerid) {
		this.ownerid = ownerid;
	}

	@Override
	public Integer getIsValid() {
		return isValid;
	}

	@Override
	public void setIsValid(Integer isValid) {
		this.isValid = isValid;
	}

	public Integer getRegionStrategy() {
		return regionStrategy;
	}

	public void setRegionStrategy(Integer regionStrategy) {
		this.regionStrategy = regionStrategy;
	}
}
