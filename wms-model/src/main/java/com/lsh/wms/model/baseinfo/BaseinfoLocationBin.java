package com.lsh.wms.model.baseinfo;

import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
public class BaseinfoLocationBin extends BaseinfoLocation implements Serializable,IBaseinfoLocaltionModel {

	/**  */
    private Long id;
	/** 位置id */
    private Long locationId;
	/** 商品的id */
    private Long itemId;
	/** 仓位体积 */
    private BigDecimal volume;
	/** 承重，默认单位kg，默认0，能承受东西很轻 */
    private BigDecimal weigh;
	/** 描述 */
    private String description;
	/** 创建日期 */
    private Long createdAt;
	/** 更新日期 */
    private Long updatedAt;
	/** 主表类型 */
    private Long type;
	/** 0可用1不可用 */
    private String isUsed;
	/** 常温或者非常温 */
	private String zoonType;
	/** 所属仓库 */
	private String regionName;

	@Override
	public Long getType() {
		return type;
	}

	@Override
	public void setType(Long type) {
		this.type = type;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public String getZoonType() {
		return zoonType;
	}

	public void setZoonType(String zoonType) {
		this.zoonType = zoonType;
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
	
	public Long getItemId(){
		return this.itemId;
	}
	
	public void setItemId(Long itemId){
		this.itemId = itemId;
	}
	
	public BigDecimal getVolume(){
		return this.volume;
	}
	
	public void setVolume(BigDecimal volume){
		this.volume = volume;
	}

	public BigDecimal getWeigh(){
		return this.weigh;
	}
	
	public void setWeigh(BigDecimal weigh){
		this.weigh = weigh;
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


	public String getIsUsed(){
		return this.isUsed;
	}
	
	public void setIsUsed(String isUsed){
		this.isUsed = isUsed;
	}

	
	
}
