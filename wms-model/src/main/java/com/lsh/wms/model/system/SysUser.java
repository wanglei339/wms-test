package com.lsh.wms.model.system;

import java.io.Serializable;
import java.util.Date;

public class SysUser implements Serializable {

	/**  */
    private Long id;
	/** 用户ID */
    private Long uid;
	/** 用户名 */
    private String username;
	/** 昵称 */
    private String screenname;

	/**角色ID*/
	private String role;
	/** 密码 */
    private String password;
	/** 盐 */
    private String salt;
	/** 用户状态 1-正常 2-禁用 */
    private Integer status;
	/** 员工ID */
    private Long staffId;
	/** 创建时间 */
    private Long createdAt;
	/** 更新时间 */
    private Long updatedAt;
	
	public Long getId(){
		return this.id;
	}
	
	public void setId(Long id){
		this.id = id;
	}
	
	public Long getUid(){
		return this.uid;
	}
	
	public void setUid(Long uid){
		this.uid = uid;
	}
	
	public String getUsername(){
		return this.username;
	}
	
	public void setUsername(String username){
		this.username = username;
	}
	
	public String getScreenname(){
		return this.screenname;
	}
	
	public void setScreenname(String screenname){
		this.screenname = screenname;
	}
	
	public String getPassword(){
		return this.password;
	}
	
	public void setPassword(String password){
		this.password = password;
	}
	
	public String getSalt(){
		return this.salt;
	}
	
	public void setSalt(String salt){
		this.salt = salt;
	}
	
	public Integer getStatus(){
		return this.status;
	}
	
	public void setStatus(Integer status){
		this.status = status;
	}
	
	public Long getStaffId(){
		return this.staffId;
	}
	
	public void setStaffId(Long staffId){
		this.staffId = staffId;
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

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
}
