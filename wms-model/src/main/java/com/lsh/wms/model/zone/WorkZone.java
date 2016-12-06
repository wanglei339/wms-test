package com.lsh.wms.model.zone;

import java.io.Serializable;
import java.util.Date;

public class WorkZone implements Serializable {

	/**  */
    private Long id;
	/**  */
    private Long zoneId;
	/**  */
    private String zoneCode;
	/**  */
    private String zoneName;
	/** 管理的location，可以是多个，逗号分离 */
    private String locations;
	/** 类型 1-分拣,2-补货,3-盘点 */
    private Long type;
	/** 状态 */
    private Long status;
	/** 是否有效 */
    private Long isValid;
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
	
	public Long getZoneId(){
		return this.zoneId;
	}
	
	public void setZoneId(Long zoneId){
		this.zoneId = zoneId;
	}
	
	public String getZoneCode(){
		return this.zoneCode;
	}
	
	public void setZoneCode(String zoneCode){
		this.zoneCode = zoneCode;
	}
	
	public String getZoneName(){
		return this.zoneName;
	}
	
	public void setZoneName(String zoneName){
		this.zoneName = zoneName;
	}
	
	public String getLocations(){
		return this.locations;
	}
	
	public void setLocations(String locations){
		this.locations = locations;
	}
	
	public Long getType(){
		return this.type;
	}
	
	public void setType(Long type){
		this.type = type;
	}
	
	public Long getStatus(){
		return this.status;
	}
	
	public void setStatus(Long status){
		this.status = status;
	}
	
	public Long getIsValid(){
		return this.isValid;
	}
	
	public void setIsValid(Long isValid){
		this.isValid = isValid;
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
