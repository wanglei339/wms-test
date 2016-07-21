package com.lsh.wms.model.baseinfo;

import java.io.Serializable;
import java.util.Date;

public class BaseinfoStaffInfo implements Serializable {

	/**  */
    private Long id;
	/** 员工id */
    private Long staffId;
	/** 工号 */
    private String staffNo;
	/** 姓名 */
    private String name;
	/** 1-男 2-女 */
    private String sex;
	/** 手机号码 */
    private String cellphone;
	/** 联系电话 */
    private String contactPhone;
	/** 联系地址 */
    private String contactAddress;
	/** 组别id */
    private Long groupId;
	/** 职级id */
    private Long levelId;
	/** 入职时间 */
    private Long entryTime;
	/** 1-在职 2-离职 */
    private String status;
	/** 1-正常 2-删除 */
    private Integer recordStatus;
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
	
	public Long getStaffId(){
		return this.staffId;
	}
	
	public void setStaffId(Long staffId){
		this.staffId = staffId;
	}
	
	public String getStaffNo(){
		return this.staffNo;
	}
	
	public void setStaffNo(String staffNo){
		this.staffNo = staffNo;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getSex(){
		return this.sex;
	}
	
	public void setSex(String sex){
		this.sex = sex;
	}
	
	public String getCellphone(){
		return this.cellphone;
	}
	
	public void setCellphone(String cellphone){
		this.cellphone = cellphone;
	}
	
	public String getContactPhone(){
		return this.contactPhone;
	}
	
	public void setContactPhone(String contactPhone){
		this.contactPhone = contactPhone;
	}
	
	public String getContactAddress(){
		return this.contactAddress;
	}
	
	public void setContactAddress(String contactAddress){
		this.contactAddress = contactAddress;
	}
	
	public Long getGroupId(){
		return this.groupId;
	}
	
	public void setGroupId(Long groupId){
		this.groupId = groupId;
	}
	
	public Long getLevelId(){
		return this.levelId;
	}
	
	public void setLevelId(Long levelId){
		this.levelId = levelId;
	}
	
	public Long getEntryTime(){
		return this.entryTime;
	}
	
	public void setEntryTime(Long entryTime){
		this.entryTime = entryTime;
	}
	
	public String getStatus(){
		return this.status;
	}
	
	public void setStatus(String status){
		this.status = status;
	}
	
	public Integer getRecordStatus(){
		return this.recordStatus;
	}
	
	public void setRecordStatus(Integer recordStatus){
		this.recordStatus = recordStatus;
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
