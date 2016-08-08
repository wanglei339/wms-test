package com.lsh.wms.model.baseinfo;

import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
public class BaseinfoLocationDock extends BaseinfoLocation implements Serializable,IBaseinfoLocaltionModel {

	/**  */
    private Long id;
	/** 位置id */
    private Long locationId;
	/** 码头名 */
    private String dockName;
	/** 是否存在地秤 */
    private Integer haveScales;
	/** 用途，0-进货，1-出货 */
    private Integer dockApplication;
	/** 方位，0-东，1-南，2-西，3-北 */
    private Integer direction;
	/** 长度默认单位 米 */
    private BigDecimal width;
	/** 高度默认单位 米 */
    private BigDecimal height;
	/** 描述 */
    private String description;
	/** 创建日期 */
    private Long createdAt;
	/** 更新日期 */
    private Long updatedAt;
	/** 主表类型 */
    private Long type;

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

	public String getDockName(){
		return this.dockName;
	}
	
	public void setDockName(String dockName){
		this.dockName = dockName;
	}
	
	public Integer getHaveScales(){
		return this.haveScales;
	}
	
	public void setHaveScales(Integer haveScales){
		this.haveScales = haveScales;
	}
	
	public Integer getDockApplication(){
		return this.dockApplication;
	}
	
	public void setDockApplication(Integer dockApplication){
		this.dockApplication = dockApplication;
	}
	
	public Integer getDirection(){
		return this.direction;
	}
	
	public void setDirection(Integer direction){
		this.direction = direction;
	}
	
	public BigDecimal getWidth(){
		return this.width;
	}
	
	public void setWidth(BigDecimal width){
		this.width = width;
	}
	
	public BigDecimal getHeight(){
		return this.height;
	}
	
	public void setHeight(BigDecimal height){
		this.height = height;
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
