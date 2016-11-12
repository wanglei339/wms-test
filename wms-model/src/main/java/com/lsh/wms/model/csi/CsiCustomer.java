package com.lsh.wms.model.csi;

import java.io.Serializable;
import java.util.Date;

public class CsiCustomer implements Serializable {

	/**  */
    private Long id;
	/** 货主id */
    private Long ownerId;
	/** 客户id */
    private Long customerId;
	/** 客户类型，重要字段，可自定义 */
    private String customerType = "";
	/** 客户名称 */
    private String customerName = "";
	/** 货主对客户的编号 */
    private String customerCode = "";
	/** 国家名称 */
    private String contry = "CN";
	/** 省份名称 */
    private String province = "北京";
	/** 城市名称 */
    private String city = "";
	/** 地址 */
    private String address = "";
	/** 状态 1-正常，2-无效 */
    private Long status = 1L;
	/** 0无效1-有效 */
    private Integer isValid = 1;
	/** 运输计划，＝运输线路 */
    private String transPlan = "";
	/** 优先级 */
    private Long priority = 0L;
	/** 播种顺序 */
    private Long seedQueue = 0L;
	/** 指定集货道 */
    private Long collectRoadId = 0L;
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
	
	public Long getCustomerId(){
		return this.customerId;
	}
	
	public void setCustomerId(Long customerId){
		this.customerId = customerId;
	}
	
	public String getCustomerType(){
		return this.customerType;
	}
	
	public void setCustomerType(String customerType){
		this.customerType = customerType;
	}
	
	public String getCustomerName(){
		return this.customerName;
	}
	
	public void setCustomerName(String customerName){
		this.customerName = customerName;
	}
	
	public String getCustomerCode(){
		return this.customerCode;
	}
	
	public void setCustomerCode(String customerCode){
		this.customerCode = customerCode;
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
	
	public Long getStatus(){
		return this.status;
	}
	
	public void setStatus(Long status){
		this.status = status;
	}
	
	public Integer getIsValid(){
		return this.isValid;
	}
	
	public void setIsValid(Integer isValid){
		this.isValid = isValid;
	}
	
	public String getTransPlan(){
		return this.transPlan;
	}
	
	public void setTransPlan(String transPlan){
		this.transPlan = transPlan;
	}
	
	public Long getPriority(){
		return this.priority;
	}
	
	public void setPriority(Long priority){
		this.priority = priority;
	}
	
	public Long getSeedQueue(){
		return this.seedQueue;
	}
	
	public void setSeedQueue(Long seedQueue){
		this.seedQueue = seedQueue;
	}
	
	public Long getCollectRoadId(){
		return this.collectRoadId;
	}
	
	public void setCollectRoadId(Long collectRoadId){
		this.collectRoadId = collectRoadId;
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
