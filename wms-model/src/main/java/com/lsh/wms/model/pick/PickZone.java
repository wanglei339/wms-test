package com.lsh.wms.model.pick;

import java.io.Serializable;
import java.util.Date;

public class PickZone implements Serializable {

	/**  */
    private Long id;
	/**  */
    private Long pickZoneId;
	/**  */
    private String pickZoneCode;
	/**  */
    private String pickZoneName;
	/** 管理的location，可以是多个，逗号分离 */
    private String locations;
	/** 最小捡货单位，1-ea，2-箱, 3-整托盘 */
    private Long pickUnit;
	/** 支持的分拣模式, 可以是多种，逗号分离 */
    private String pickModelSupport;
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
	
	public Long getPickZoneId(){
		return this.pickZoneId;
	}
	
	public void setPickZoneId(Long pickZoneId){
		this.pickZoneId = pickZoneId;
	}
	
	public String getPickZoneCode(){
		return this.pickZoneCode;
	}
	
	public void setPickZoneCode(String pickZoneCode){
		this.pickZoneCode = pickZoneCode;
	}
	
	public String getPickZoneName(){
		return this.pickZoneName;
	}
	
	public void setPickZoneName(String pickZoneName){
		this.pickZoneName = pickZoneName;
	}
	
	public String getLocations(){
		return this.locations;
	}
	
	public void setLocations(String locations){
		this.locations = locations;
	}
	
	public Long getPickUnit(){
		return this.pickUnit;
	}
	
	public void setPickUnit(Long pickUnit){
		this.pickUnit = pickUnit;
	}
	
	public String getPickModelSupport(){
		return this.pickModelSupport;
	}
	
	public void setPickModelSupport(String pickModelSupport){
		this.pickModelSupport = pickModelSupport;
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
