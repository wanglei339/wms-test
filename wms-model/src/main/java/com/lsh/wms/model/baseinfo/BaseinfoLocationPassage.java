package com.lsh.wms.model.baseinfo;

import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
public class BaseinfoLocationPassage extends BaseinfoLocation implements Serializable,IBaseinfoLocaltionModel {

	/**  */
    private Long id;
	/** 位置id */
    private Long locationId;
	/** 方位，0-南北，1-东西 */
    private Integer direction;
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
	/** 主表类型 */
    private Long type;
	/** 是否可用1可用，0删除 */
	private Integer isValid;

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
	
	public Integer getDirection(){
		return this.direction;
	}
	
	public void setDirection(Integer direction){
		this.direction = direction;
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

	@Override
	public Integer getIsValid() {
		return isValid;
	}

	@Override
	public void setIsValid(Integer isValid) {
		this.isValid = isValid;
	}
}
