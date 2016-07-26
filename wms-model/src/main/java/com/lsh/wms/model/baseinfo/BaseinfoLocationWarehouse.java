package com.lsh.wms.model.baseinfo;

import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
@Component
public class BaseinfoLocationWarehouse extends IBaseinfoLocaltionModel implements Serializable {

	/**  */
    private Long id;
	/** 位置id */
    private Long locationId;
	/** 仓库名 */
    private String warehouseName;
	/** 地址 */
    private String address;
	/** 电话 */
    private String phoneNo;
	/** 描述 */
    private String description;
	/** 创建日期 */
    private Long createdAt;
	/** 更新日期 */
    private Long updatedAt;
	/** 主表的type */
    private Integer type;
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
	
	public String getWarehouseName(){
		return this.warehouseName;
	}
	
	public void setWarehouseName(String warehouseName){
		this.warehouseName = warehouseName;
	}
	
	public String getAddress(){
		return this.address;
	}
	
	public void setAddress(String address){
		this.address = address;
	}
	
	public String getPhoneNo(){
		return this.phoneNo;
	}
	
	public void setPhoneNo(String phoneNo){
		this.phoneNo = phoneNo;
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
	
	public Integer getType(){
		return this.type;
	}
	
	public void setType(Integer type){
		this.type = type;
	}
	
	public String getIsUsed(){
		return this.isUsed;
	}
	
	public void setIsUsed(String isUsed){
		this.isUsed = isUsed;
	}
	
	
}
