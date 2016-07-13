package com.lsh.wms.model.csi;

import java.io.Serializable;
import java.util.Date;

public class CsiSupplier implements Serializable {

	/**  */
    private Long id;
	/** 供应商id */
    private Long supplierId = 0L;
	/** 供应商名称 */
	//long改为String
    private String supplierName;
	/** 货主id */
    private Long ownerId;
	/** 货主对供商的编号 */
    private String supplierCode;
	/** 电话号码 */
    private String phone;
	/** 传真 */
    private String fax;
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
	
	public Long getSupplierId(){
		return this.supplierId;
	}
	
	public void setSupplierId(Long supplierId){
		this.supplierId = supplierId;
	}
	
	public String getSupplierName(){
		return this.supplierName;
	}
	
	public void setSupplierName(String supplierName){
		this.supplierName = supplierName;
	}
	
	public Long getOwnerId(){
		return this.ownerId;
	}
	
	public void setOwnerId(Long ownerId){
		this.ownerId = ownerId;
	}
	
	public String getSupplierCode(){
		return this.supplierCode;
	}
	
	public void setSupplierCode(String supplierCode){
		this.supplierCode = supplierCode;
	}
	
	public String getPhone(){
		return this.phone;
	}
	
	public void setPhone(String phone){
		this.phone = phone;
	}
	
	public String getFax(){
		return this.fax;
	}
	
	public void setFax(String fax){
		this.fax = fax;
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
