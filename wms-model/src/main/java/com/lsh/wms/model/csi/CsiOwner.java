package com.lsh.wms.model.csi;

import java.io.Serializable;
import java.util.Date;

public class CsiOwner implements Serializable {

	/**  */
    private Long id;
	/** 货主id */
    private Long ownerId;
	/** 货主名称 */
    private Long ownerName;
	/** 货主描述 */
    private String ownerDesc;
	/** 国家名称 */
    private String contry;
	/** 省份名称 */
    private String province;
	/** 城市名称 */
    private String city;
	/** 地址 */
    private String address;
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
	
	public Long getOwnerId(){
		return this.ownerId;
	}
	
	public void setOwnerId(Long ownerId){
		this.ownerId = ownerId;
	}
	
	public Long getOwnerName(){
		return this.ownerName;
	}
	
	public void setOwnerName(Long ownerName){
		this.ownerName = ownerName;
	}
	
	public String getOwnerDesc(){
		return this.ownerDesc;
	}
	
	public void setOwnerDesc(String ownerDesc){
		this.ownerDesc = ownerDesc;
	}
	
	public String getContry(){
		return this.contry;
	}
	
	public void setContry(String contry){
		this.contry = contry;
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
