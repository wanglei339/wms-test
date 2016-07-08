package com.lsh.wms.model.csi;

import java.io.Serializable;
import java.util.Date;

public class CsiCategory implements Serializable {

	/**  */
    private Long catId;
	/**  */
    private Long fCatId;
	/**  */
    private String name;
	/**  */
    private Long level;
	/**  */
    private Long status;
	/**  */
    private String isLeaf;
	
	public Long getCatId(){
		return this.catId;
	}
	
	public void setCatId(Long catId){
		this.catId = catId;
	}
	
	public Long getFCatId(){
		return this.fCatId;
	}
	
	public void setFCatId(Long fCatId){
		this.fCatId = fCatId;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public Long getLevel(){
		return this.level;
	}
	
	public void setLevel(Long level){
		this.level = level;
	}
	
	public Long getStatus(){
		return this.status;
	}
	
	public void setStatus(Long status){
		this.status = status;
	}
	
	public String getIsLeaf(){
		return this.isLeaf;
	}
	
	public void setIsLeaf(String isLeaf){
		this.isLeaf = isLeaf;
	}
	
	
}
