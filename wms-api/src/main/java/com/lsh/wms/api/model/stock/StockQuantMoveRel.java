package com.lsh.wms.api.model.stock;

import java.io.Serializable;
import java.util.Date;

public class StockQuantMoveRel implements Serializable {

	/**  */
    private String id;
	/** SOTOCK_MOVE_ID */
    private Long moveId;
	/** QUANT ID */
    private Long quantId;
	/**  */
    private String createdAt;
	/**  */
    private String updatedAt;
	
	public String getId(){
		return this.id;
	}
	
	public void setId(String id){
		this.id = id;
	}
	
	public Long getMoveId(){
		return this.moveId;
	}
	
	public void setMoveId(Long moveId){
		this.moveId = moveId;
	}
	
	public Long getQuantId(){
		return this.quantId;
	}
	
	public void setQuantId(Long quantId){
		this.quantId = quantId;
	}
	
	public String getCreatedAt(){
		return this.createdAt;
	}
	
	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}
	
	public String getUpdatedAt(){
		return this.updatedAt;
	}
	
	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}
	
	
}
