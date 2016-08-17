package com.lsh.wms.model.system;

import java.io.Serializable;
import java.util.Date;

public class RolePermission implements Serializable {

	/**  */
    private Integer id;
	/** 角色名 */
    private String role;
	/**  */
    private String permission;
	/**  */
    private Integer status;
	
	public Integer getId(){
		return this.id;
	}
	
	public void setId(Integer id){
		this.id = id;
	}
	
	public String getRole(){
		return this.role;
	}
	
	public void setRole(String role){
		this.role = role;
	}
	
	public String getPermission(){
		return this.permission;
	}
	
	public void setPermission(String permission){
		this.permission = permission;
	}
	
	public Integer getStatus(){
		return this.status;
	}
	
	public void setStatus(Integer status){
		this.status = status;
	}
	
	
}
