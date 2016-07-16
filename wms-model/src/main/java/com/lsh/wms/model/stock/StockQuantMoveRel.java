package com.lsh.wms.model.stock;

import com.lsh.base.common.utils.DateUtils;

import java.io.Serializable;
import java.util.Date;

public class StockQuantMoveRel implements Serializable {

	/**  */
	private Long id;
	/** sotock_move_id */
	private Long moveId;
	/** quant id */
	private Long quantId;
	/**  */
	private Long createdAt;
	/**  */
	private Long updatedAt;

	public StockQuantMoveRel() {
		this.updatedAt = DateUtils.getCurrentSeconds();
		this.createdAt = DateUtils.getCurrentSeconds();
	}

	public Long getId(){
		return this.id;
	}

	public void setId(Long id){
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
