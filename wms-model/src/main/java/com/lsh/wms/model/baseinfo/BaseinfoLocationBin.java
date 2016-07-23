package com.lsh.wms.model.baseinfo;

import java.io.Serializable;
import java.util.Date;

public class BaseinfoLocationBin implements Serializable {

	/**  */
    private Long id;
	/** 位置id */
    private Long locationId;
	/** 商品的id */
    private Long itemId;
	/** 仓位体积 */
    private BigDecimal volume;
	/** 仓位类型，0-拣货位，1-仓储位，2-地堆区(stacking)，3—暂存区，4-集货区，5-退货区，6-残次区 */
    private Integer binType;
	/** 承重，默认单位kg，默认0，能承受东西很轻 */
    private BigDecimal weigh;
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
	
	public Integer getBinType(){
		return this.binType;
	}
	
	public void setBinType(Integer binType){
		this.binType = binType;
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
	
	
}
