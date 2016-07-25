package com.lsh.wms.model.csi;

import java.io.Serializable;
import java.util.Date;

public class CsiOwner implements Serializable {

	/**  */
    private Long id;
	/** 货主id */
    private Long ownerId = 0L;
	/** 货主名称 */
	//long改为String
    private String ownerName;
	/** 货主描述 */
    private String ownerDesc;
	/** 国家名称 */
    private String country;
	/** 省份名称 */
    private String province;
	/** 城市名称 */
    private String city;
	/** 地址 */
    private String address;
	/**  */
    private Long createdAt = 0L;
	/**  */
    private Long updatedAt = 0L;
	
	public Long getId(){
		return this.id;
	}
	
	public void setId(Long id){
		this.id = id;
	}
	
	public Long getOwnerId(){
		return this.ownerId;
	}
	
	public void setOwnerId(Long ownerId){
		this.ownerId = ownerId;
	}
	
	public String getOwnerName(){
		return this.ownerName;
	}
	
	public void setOwnerName(String ownerName){
		this.ownerName = ownerName;
	}
	
	public String getOwnerDesc(){
		return this.ownerDesc;
	}
	
	public void setOwnerDesc(String ownerDesc){
		this.ownerDesc = ownerDesc;
	}
	
	public String getCountry(){
		return this.country;
	}
	
	public void setCountry(String country){
		this.country = country;
	}
	
	public String getProvince(){
		return this.province;
	}
	
	public void setProvince(String province){
		this.province = province;
	}
	
	public String getCity(){
		return this.city;
	}
	
	public void setCity(String city){
		this.city = city;
	}
	
	public String getAddress(){
		return this.address;
	}
	
	public void setAddress(String address){
		this.address = address;
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
