package com.lsh.wms.model.baseinfo;

import java.io.Serializable;
import java.util.Date;

public class BaseinfoContainer implements Serializable {

	/**  */
    private Long id;
	/** 容器id */
    private Long containerId;
	/** 容器编码 */
    private String containerCode = "";
	/** 容器类型 */
    private Long type;
	/** 类型名 */
    private String typeName = "";
	/** 型号 */
    private String model = "";
	/** 容积，单位L */
    private Long capacity = 0L;
	/** 载重量，单位KG */
    private Long loadCapacity = 0L;
	/** 状态，1-正常，2-禁用，3-维修 */
    private String status = "1";
	/** 描述 */
    private String description = "";
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
	
	public Long getContainerId(){
		return this.containerId;
	}
	
	public void setContainerId(Long containerId){
		this.containerId = containerId;
	}
	
	public String getContainerCode(){
		return this.containerCode;
	}
	
	public void setContainerCode(String containerCode){
		this.containerCode = containerCode;
	}
	
	public Long getType(){
		return this.type;
	}
	
	public void setType(Long type){
		this.type = type;
	}
	
	public String getTypeName(){
		return this.typeName;
	}
	
	public void setTypeName(String typeName){
		this.typeName = typeName;
	}
	
	public String getModel(){
		return this.model;
	}
	
	public void setModel(String model){
		this.model = model;
	}
	
	public Long getCapacity(){
		return this.capacity;
	}
	
	public void setCapacity(Long capacity){
		this.capacity = capacity;
	}
	
	public Long getLoadCapacity(){
		return this.loadCapacity;
	}
	
	public void setLoadCapacity(Long loadCapacity){
		this.loadCapacity = loadCapacity;
	}
	
	public String getStatus(){
		return this.status;
	}
	
	public void setStatus(String status){
		this.status = status;
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
	
	
}
