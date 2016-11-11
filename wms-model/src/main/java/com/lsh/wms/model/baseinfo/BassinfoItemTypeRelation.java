package com.lsh.wms.model.baseinfo;

import java.io.Serializable;
import java.util.Date;

public class BassinfoItemTypeRelation implements Serializable {

	/**  */
    private Long id;
	/** 课组ID（关系中ID较小的） */
    private Long itemTypeId;
	/** 对应互斥课组ID(关系中ID较大的) */
    private Long itemMutexId;
	
	public Long getId(){
		return this.id;
	}
	
	public void setId(Long id){
		this.id = id;
	}


	public Long getItemTypeId() {
		return itemTypeId;
	}

	public void setItemTypeId(Long itemTypeId) {
		this.itemTypeId = itemTypeId;
	}

	public Long getItemMutexId() {
		return itemMutexId;
	}

	public void setItemMutexId(Long itemMutexId) {
		this.itemMutexId = itemMutexId;
	}
}
